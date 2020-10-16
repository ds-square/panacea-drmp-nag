
package org.panacea.drmp.nag.domain.devices;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class LocalService {

    private List<String> cpe;
    private String name;
    private List<OsUser> users;
    private String version;
    private List<String> vulnerabilities;

}
