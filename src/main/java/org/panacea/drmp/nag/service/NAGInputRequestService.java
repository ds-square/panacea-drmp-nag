package org.panacea.drmp.nag.service;

import org.panacea.drmp.nag.domain.devices.DeviceInventory;
import org.panacea.drmp.nag.domain.reachability.ReachabilityInventory;
import org.panacea.drmp.nag.domain.vulnerabilities.VulnerabilityCatalog;


public interface NAGInputRequestService {

	DeviceInventory performDeviceInventoryRequest(String snapshotId);
	VulnerabilityCatalog performVulnerabilityCatalogRequest(String snapshotId);
	ReachabilityInventory performReachabilityInventoryRequest(String snapshotId);

}
