package org.panacea.drmp.nag.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.nag.domain.devices.DeviceInventory;
import org.panacea.drmp.nag.domain.reachability.ReachabilityInventory;
import org.panacea.drmp.nag.domain.vulnerabilities.VulnerabilityCatalog;
import org.panacea.drmp.nag.service.NAGInputRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service

public class NAGInputRequestServiceImpl implements NAGInputRequestService {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${reachabilityInventory.endpoint}")
	private String reachabilityInventoryURL;

	@Value("${reachabilityInventory.fn}")
	private String reachabilityInventoryFn;

	@Value("${deviceInventory.endpoint}")
	private String deviceInventoryURL;

	@Value("${deviceInventory.fn}")
	private String deviceInventoryFn;

	@Value("${vulnerabilityCatalog.endpoint}")
	private String vulnerabilityCatalogURL;

	@Value("${vulnerabilityCatalog.fn}")
	private String vulnerabilityCatalogFn;



	public DeviceInventory performDeviceInventoryRequest(String snapshotId){
		log.info("[NAG] GETting " + deviceInventoryURL + '/' + snapshotId); //+'/'+deviceInventoryFn);
		ResponseEntity<DeviceInventory> responseEntity = restTemplate.exchange(
				deviceInventoryURL + '/' + snapshotId,//+'/'+deviceInventoryFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<DeviceInventory>() {
				});

		DeviceInventory deviceInventory = responseEntity.getBody();

		return deviceInventory;
	}


	public VulnerabilityCatalog performVulnerabilityCatalogRequest(String snapshotId) {
		log.info("[NAG] GETting " + vulnerabilityCatalogURL + '/' + snapshotId);
		ResponseEntity<VulnerabilityCatalog> responseEntity = restTemplate.exchange(
				vulnerabilityCatalogURL + '/' + snapshotId, //+'/'+ vulnerabilityCatalogFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<VulnerabilityCatalog>() {
				});
		VulnerabilityCatalog vulnerabilityCatalog = responseEntity.getBody();

		return vulnerabilityCatalog;
	}

	public ReachabilityInventory performReachabilityInventoryRequest(String snapshotId) {
		log.info("[NAG] GETting " + reachabilityInventoryURL);
		ResponseEntity<ReachabilityInventory> responseEntity = restTemplate.exchange(
				reachabilityInventoryURL + '/' + snapshotId, //+'/'+reachabilityInventoryFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<ReachabilityInventory>() {
				});
		ReachabilityInventory reachabilityInventory = responseEntity.getBody();

		return reachabilityInventory;
	}
}
