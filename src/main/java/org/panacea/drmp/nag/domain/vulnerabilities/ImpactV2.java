
package org.panacea.drmp.nag.domain.vulnerabilities;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class ImpactV2 {

    private String accessComplexity;
    private double accessComplexityScore;
    private String accessVector;
    private double accessVectorScore;
    private double authentication;
    private double baseScore;
    private List<CIAImpact> ciaImpacts;
    private String cvssVersion;
    private double exploitabilityScore;
    private double exploitability;
    private double impactScore;
    private double reportConfidence;
    private Boolean userInteractionRequired;

}
