
package org.panacea.drmp.nag.domain.devices;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class Device {

    private String hostName;
    private String id;
    private List<NetworkInterface> networkInterfaces;
    private OperatingSystem operatingSystem;
    private String type;


}
