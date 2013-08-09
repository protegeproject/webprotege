package edu.stanford.bmir.protege.web.shared.irigen.supplied;

import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuppliedSuffixSettings extends SuffixSettings {

    public static final SuffixSettingsId Id = SuffixSettingsId.get("Supplied", "Use supplied entity name");

    public SuppliedSuffixSettings() {
        super(Id);
    }

//    @Override
//    public String generateExample(String base) {
//        return base + "MyClass";
//    }
}
