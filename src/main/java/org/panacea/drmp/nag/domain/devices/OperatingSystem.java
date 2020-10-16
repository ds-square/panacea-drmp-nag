
package org.panacea.drmp.nag.domain.devices;

import java.util.List;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class OperatingSystem {

    private String family;
    private String generation;
    private List<LocalService> localServices;
    private List<OsUser> osUsers;
    private List<String> osVulnerabilities;
    private String vendor;

}
