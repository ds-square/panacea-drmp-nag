
package org.panacea.drmp.nag.domain.reachability;

import java.util.List;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class ReachabilityInventory {

    private String environment;
    private String fileType;
    private List<SourceDevice> sourceDevices;
    private String snapshotId;
    private String snapshotTime;

}
