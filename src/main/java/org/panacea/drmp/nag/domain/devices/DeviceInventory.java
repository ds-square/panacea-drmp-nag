
package org.panacea.drmp.nag.domain.devices;

import java.util.List;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class DeviceInventory {

    private List<Device> devices;
    private String environment;
    private String fileType;
    private String snapshotId;
    private String snapshotTime;

}
