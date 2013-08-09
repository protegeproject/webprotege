package edu.stanford.bmir.protege.web.shared.irigen.action;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class GetIRIGeneratorSettingsResult implements Result {

    private IRIGeneratorSettings iriGeneratorSettings;

    /**
     * For serialization purposes only
     */
    private GetIRIGeneratorSettingsResult() {
    }

    public GetIRIGeneratorSettingsResult(IRIGeneratorSettings iriGeneratorSettings) {
        this.iriGeneratorSettings = iriGeneratorSettings;
    }

    public IRIGeneratorSettings getIriGeneratorSettings() {
        return iriGeneratorSettings;
    }
}
