
package org.panacea.drmp.nag.domain.vulnerabilities;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class ImpactV3 {

    private String attackComplexity;
    private double attackComplexityScore;
    private String attackVector;
    private double attackVectorScore;
    private double baseScore;
    private List<CIAImpact> ciaImpacts;
    private String cvssVersion;
    private double exploitabilityCodeMaturity;
    private double exploitabilityScore;
    private double exploitability;
    private double impactScore;
    private double privilegesRequired;
    private double reportConfidence;
    private String scope;
    private double userInteraction;
}
