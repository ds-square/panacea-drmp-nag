
package org.panacea.drmp.nag.domain.devices;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class NetworkInterface {

    @JsonProperty("ipAddress")
    private String iPAddress;
    private String macAddress;
    private String mask;
    private String name;
    private List<Port> ports;
    private String version;

}
