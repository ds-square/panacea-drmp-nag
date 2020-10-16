
package org.panacea.drmp.nag.domain.reachability;

import java.util.List;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class SourceDevice {

    private String hostName;
    private String id;
    private List<ReachedInterface> reachedInterface;

}
