
package org.panacea.drmp.nag.domain.reachability;

import java.util.List;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class ReachedInterface {

    private String address;
    private String lanID;
    private String name;
    private List<ReachedDevice> reachedDevices;

}
