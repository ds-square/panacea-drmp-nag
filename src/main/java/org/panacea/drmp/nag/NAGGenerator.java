package org.panacea.drmp.nag;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.nag.domain.devices.*;
import org.panacea.drmp.nag.domain.graph.NDPrivilege;
import org.panacea.drmp.nag.domain.graph.NExploitRepr;
import org.panacea.drmp.nag.domain.graph.NetworkLayerAttackGraphRepr;
import org.panacea.drmp.nag.domain.graph.NetworkVulnerability;
import org.panacea.drmp.nag.domain.notifications.DataNotification;
import org.panacea.drmp.nag.domain.reachability.*;
import org.panacea.drmp.nag.domain.vulnerabilities.ImpactV2;
import org.panacea.drmp.nag.domain.vulnerabilities.ImpactV3;
import org.panacea.drmp.nag.domain.vulnerabilities.Vulnerability;
import org.panacea.drmp.nag.domain.vulnerabilities.VulnerabilityCatalog;
import org.panacea.drmp.nag.exception.NAGException;
import org.panacea.drmp.nag.service.NAGInputRequestService;
import org.panacea.drmp.nag.service.NAGPostOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class NAGGenerator {

	@Autowired
	NAGInputRequestService nagInputRequestService;

	@Autowired
	NAGPostOutputService nagPostOutputService;

	private DeviceInventory deviceInventory;
	private VulnerabilityCatalog vulnerabilityCatalog;
	private ReachabilityInventory reachabilityInventory;


	@Synchronized
	public void generateNAG(DataNotification notification) {
		if (notification == null) {
			return;
		}
		try {
			this.getInput(notification.getSnapshotId());
		} catch (NAGException e) {
			log.info(e.getMessage());
		}
		// Build data structures

		Map<String, Vulnerability> vulnerabilities = new HashMap<String, Vulnerability>();
		Map<String, Device> devices = new HashMap<String, Device>();
		Map<VulnPortKey, Set<String>> networkHostVulnerabilities = new HashMap<VulnPortKey, Set<String>>();

		Set<String> uuids = new HashSet<String>();
		List<NDPrivilege> nodes = new ArrayList<>();

		Map<EdgeKey, NExploitRepr> edges = new HashMap<EdgeKey, NExploitRepr>();

		//Build indexed hashmap of all vulnerabilities
		List<Vulnerability> vulnerabilityList = vulnerabilityCatalog.getVulnerabilities();
		if (vulnerabilityList == null) {
			vulnerabilityList = new ArrayList<>();
		}
		for (Vulnerability i : vulnerabilityList) {
			if (i == null) {
				continue;
			}
			vulnerabilities.put(i.getCveId(), i);

		}

		//Build hashmap of exposed vulnerabilities on hosts: <host,port> <cvelist>
		for (Device d : deviceInventory.getDevices()) {
			String dev = d.getId();
			devices.put(dev, d);
			for (NetworkInterface i : d.getNetworkInterfaces()) {
				for (Port p : i.getPorts()) {
					if (p.getService() != null) {
						List<String> vulnList = p.getService().getVulnerabilities();
						if (vulnList != null) {
							HashSet<String> vList = new HashSet<String>(vulnList);
							if (!vList.isEmpty()) {
								networkHostVulnerabilities.put(new VulnPortKey(dev, p.getNumber()), vList);
							}
						}
					}
				}
			}
		}

		//log.info(networkHostVulnerabilities.toString());


		//Examine Reachability
		for(SourceDevice sd: reachabilityInventory.getSourceDevices()) {

			String sourceId = sd.getId();
			Device d = devices.get(sourceId);
			//log.info("[NAGGenerator] Reading Device "+sourceId);
			List<String> localVulns = new ArrayList<>();
			if (d.getOperatingSystem() != null) {
				OperatingSystem OS = d.getOperatingSystem();

				//1.local services vulnerabilities + os vulnerabilities
				if (OS != null) {
					if (OS.getOsVulnerabilities() != null) {
						localVulns = OS.getOsVulnerabilities();
					}
					if (OS.getLocalServices() != null) {
						for (LocalService ls : OS.getLocalServices()) {
							if (ls.getVulnerabilities() != null) {
								localVulns.addAll(ls.getVulnerabilities());
							}
						}
					}
				}
			}
			NExploitRepr e;
			String source;
			String dest;

			if(localVulns.size()>0) {
				for (String cve : localVulns) {
					//log.info("[NAGGenerator] " + sourceId + "- analyzing local vulnerability " + cve);
					Vulnerability v = vulnerabilities.get(cve);
					if (v == null) {
						log.error(cve);
						continue;
					}
					AV av = null;
					Object impact = v.getImpactV3();
					if (impact == null) {
						impact = v.getImpactV2();
						av = AV.valueOf(((ImpactV2) impact).getAccessVector());
					} else {
						if (((ImpactV3) impact).getAttackVector() != null) {
							av = AV.valueOf(((ImpactV3) impact).getAttackVector());
						} else {
							impact = v.getImpactV2();
							av = AV.valueOf(((ImpactV2) impact).getAccessVector());
						}
					}
					Privilege preCondition = Privilege.valueOf(v.getPreCondition());
					Privilege postCondition = Privilege.valueOf(v.getPostCondition());

					NetworkVulnerability dv = new NetworkVulnerability(v.getCveId(), "local");

					if (av == AV.PHYSICAL && postCondition.level >= Privilege.NONE.level) {

						//Create edge between none@source, post@source
						source = generateNodeUUID(sourceId, Privilege.NONE.name());
						if (!uuids.contains(source)) {
							nodes.add(createNode(sourceId, source, Privilege.NONE));
							uuids.add(source);
						}
						dest = generateNodeUUID(sourceId, postCondition.name());
						if (!uuids.contains(dest)) {
							nodes.add(createNode(sourceId, dest, postCondition));
							uuids.add(dest);
						}

						EdgeKey ek = new EdgeKey(source, dest);
						e = edges.get(ek);
						if (e != null) {// Edge already exists, update vulnerability
							e.addVulnerability(dv);
							//log.info("Updating local edge " + ek.toString() + " - " + edges.get(ek));
						} else {
							e = new NExploitRepr(source,dest);
							e.addVulnerability(dv);
							edges.put(ek,e);
							//log.info("Adding local edge " + e.toString() + " - " + edges.get(ek));
						}
					} else { // put edge between host.pre and host.post (can be N->N, U->U, U->R, R->R)

						source = generateNodeUUID(sourceId, preCondition.name());
						dest = generateNodeUUID(sourceId, postCondition.name());

						if (!uuids.contains(source)) {
							nodes.add(createNode(sourceId, source, preCondition));
							uuids.add(source);
						}

						if (!uuids.contains(dest)) {
							nodes.add(createNode(sourceId, dest, postCondition));
							uuids.add(dest);
						}

						EdgeKey ek = new EdgeKey(source,dest);

						e = edges.get(ek);
						if (e != null) {
							e.addVulnerability(dv);
							//log.info("Updating local edge2 " + ek.toString() + " - " + edges.get(ek));
						} else {
							e = new NExploitRepr(source,dest);
							e.addVulnerability(dv);
							edges.put(ek,e);
							//log.info("Adding local edge " + e.toString() + " - " + edges.get(ek));
						}
					}
				}
			}
			else {
				//log.info("[NAGGenerator] >> node " + sourceId + " does not have local vulnerabilities.");
			}

			//2. Remote / local network vulnerabilities

			// for each interface i on source device
			for (ReachedInterface i: sd.getReachedInterface()) {

				//for each reached node from i
				String sourceLanID = i.getLanID();

				for (ReachedDevice r: i.getReachedDevices()) {
					String destLanID = r.getLanID();
					String destId = r.getDeviceId();
					//log.info("[NAGGenerator] "+ sourceId+ "-"+i.getName()+" - Analyzing network vulnerabilities on "+ destId);
					for (ReachedPort p : r.getReachedPorts()) {
						Set<String> reachedVulnerabilities = networkHostVulnerabilities.get(new VulnPortKey(destId, p.getPort()));
						if (reachedVulnerabilities != null) {
							for (String cve : reachedVulnerabilities) {
								Vulnerability v = vulnerabilities.get(cve);

								if (v == null) {
									log.info("[NAGGenerator] >> Vulnerability " + cve + " is not present in Vulnerability Inventory");
								} else {
									//EDGE LOGIC
									//log.info("[NAGGenerator] Analyzing network vulnerability "+cve);
									Object impact = v.getImpactV3();
									AV av;

									if (impact == null) {
										impact = v.getImpactV2();
										av = AV.valueOf(((ImpactV2) impact).getAccessVector());
									} else {
										av = AV.valueOf(((ImpactV3) impact).getAttackVector());
									}

									//Privilege preCondition = Privilege.valueOf(v.getPreCondition());

									Privilege postCondition = Privilege.valueOf(v.getPostCondition());

									NetworkVulnerability dv = new NetworkVulnerability(v.getCveId(), "network");
									dv.setFromInt(i.getName());
									dv.setToInt(r.getIfaceName());
									dv.setService(p.getService());
									dv.setPort(p.getPort());
									dv.setProtocol(p.getProtocol());

									if (av == AV.NETWORK || av == AV.ADJACENT_NETWORK && sourceLanID.equals(destLanID)) {

										source = generateNodeUUID(sourceId, Privilege.USER.name());
										dest = generateNodeUUID(destId, postCondition.name());
										if (!uuids.contains(source)) {
											nodes.add(createNode(sourceId, source, Privilege.USER));
											uuids.add(source);
										}

										if (!uuids.contains(dest)) {
											nodes.add(createNode(destId, dest, postCondition));
											uuids.add(dest);
										}


										EdgeKey ek = new EdgeKey(source,dest);

										e = edges.get(ek);
										if (e != null) {
											e.addVulnerability(dv);
											//log.info("Updating edge " + ek.toString() + " - " + edges.get(ek));
										} else {
											e = new NExploitRepr(source, dest);
											e.addVulnerability(dv);
											edges.put(ek, e);
											//log.info("Adding edge " + e.toString() + " - " + edges.get(ek));
										}

										source = generateNodeUUID(sourceId, Privilege.ROOT.name());
										if (!uuids.contains(source)) {
											nodes.add(createNode(sourceId, source, Privilege.ROOT));
											uuids.add(source);
										}


										ek = new EdgeKey(source, dest);

										e = edges.get(ek);
										if (e != null) {
											e.addVulnerability(dv);
											//log.info("Updating edge2 " + ek.toString() + " - " + edges.get(ek));
										} else {
											e = new NExploitRepr(source,dest);
											e.addVulnerability(dv);
											edges.put(ek,e);
											//log.info("Adding edge2 " + e.toString() + " - " + edges.get(ek));
										}

									}
								}
							}
						}
//						else{
//							log.info("[NAGGenerator] >> reached node " + destId + " does not have network vulnerabilities.");
//						}
					}
				}
			}
		}

		NetworkLayerAttackGraphRepr graph = new NetworkLayerAttackGraphRepr(notification.getEnvironment(),notification.getSnapshotId(),notification.getSnapshotTime(),nodes,new ArrayList(edges.values()));


		/*ObjectMapper objectMapper = new ObjectMapper();
		try {
			//log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(graph));
		}
		catch(JsonProcessingException e){
			e.printStackTrace();
		}*/


		nagPostOutputService.postNetworkLayerAttackGraphRepr(graph);
	}

	private NDPrivilege createNode(String deviceId, String uuid, Privilege privilegeLevel) {
		return new NDPrivilege(deviceId, uuid, privilegeLevel.name());
	}

	private void printEdges(List<NExploitRepr> edges) {
		edges.forEach(e -> log.info("[EDGE] " + e.getSource() + "-" + e.getDestination() + " --- " + e.getVulnerabilities().toString()));
	}

	private void getInput(String version) {

		// get input data from REST service
		this.deviceInventory = nagInputRequestService.performDeviceInventoryRequest(version);
		this.vulnerabilityCatalog = nagInputRequestService.performVulnerabilityCatalogRequest(version);
		this.reachabilityInventory = nagInputRequestService.performReachabilityInventoryRequest(version);

		//log.info("[Reachability] "+this.reachabilityInventory);
		//log.info("[Vulnerability] "+this.vulnerabilityCatalog);
		//log.info("[Device] "+this.deviceInventory);


	}


	private String generateNodeUUID(String deviceId, String privilege) {
		return privilege + '@' + deviceId;
	}


	enum Privilege {
		NONE(0),
		USER(1),
		ROOT(2);
		public int level;

		Privilege(int level) {
			this.level = level;
		}



	}

	enum AV {
		NETWORK(0),
		ADJACENT_NETWORK(1),
		LOCAL(2),
		PHYSICAL(3);
		public int level;

		AV(int type) {
			this.level = type;
		}


	}

	}



class EdgeKey {
	String src;
	String dest;

	public EdgeKey(String src, String dest) {
		this.src = src;
		this.dest = dest;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EdgeKey edgeKey = (EdgeKey) o;
		return src.equals(edgeKey.src) &&
				dest.equals(edgeKey.dest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(src, dest);
	}
}



class VulnPortKey{
	String host;
	long port;

	public VulnPortKey(String host, long port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return "VulnPortKey{" +
				"host='" + host + '\'' +
				", port=" + port +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VulnPortKey that = (VulnPortKey) o;
		return port == that.port &&
				host.equals(that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(host, port);
	}
}


