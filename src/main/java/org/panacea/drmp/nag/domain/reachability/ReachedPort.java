
package org.panacea.drmp.nag.domain.reachability;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class ReachedPort {

    private int port;
    private String protocol;
    private List<String> service;

}
