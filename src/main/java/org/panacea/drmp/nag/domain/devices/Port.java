
package org.panacea.drmp.nag.domain.devices;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class Port {

    private long number;
    private Service service;
    private String state;
    private String transportProtocol;

}
