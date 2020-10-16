package org.panacea.drmp.nag.domain.graph;

import java.util.List;

public class NetworkLayerAttackGraphRepr {
	private String environment;
	private String fileType;
	private String snapshotId;
	private String snapshotTime;
	private List<NDPrivilege> nodes;
	private List<NExploitRepr> edges;

	public String getEnvironment() {
		return environment;
	}

	public String getFileType() {
		return fileType;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public String getSnapshotTime() {
		return snapshotTime;
	}


	public List<NDPrivilege> getNodes() {
		return nodes;
	}

	public List<NExploitRepr> getEdges() {
		return edges;
	}

	public NetworkLayerAttackGraphRepr(String environment, String snapshotId, String snapshotTime, List<NDPrivilege> nodes, List<NExploitRepr> edges) {
		this.environment = environment;
		this.fileType = "NetworkLayerAttackGraphRepr";
		this.snapshotId = snapshotId;
		this.snapshotTime = snapshotTime;
		this.nodes = nodes;
		this.edges = edges;
	}
}
