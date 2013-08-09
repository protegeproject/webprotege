package edu.stanford.bmir.protege.web.shared.irigen;

import edu.stanford.bmir.protege.web.shared.irigen.obo.OBOSuffixSettingsEditorKit;
import edu.stanford.bmir.protege.web.shared.irigen.supplied.SuppliedSuffixSettingsEditorKit;
import edu.stanford.bmir.protege.web.shared.irigen.uuid.UUIDSuffixSettingsEditorKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuffixSettingsEditorKitManager {

    private SuffixSettingsEditorKitManager() {

    }

    public static SuffixSettingsEditorKitManager get() {
        return new SuffixSettingsEditorKitManager();
    }

    public List<SuffixSettingsEditorKit> getFactories() {
        List<SuffixSettingsEditorKit> editorKits = new ArrayList<SuffixSettingsEditorKit>();
        editorKits.add(new UUIDSuffixSettingsEditorKit());
        editorKits.add(new OBOSuffixSettingsEditorKit());
        editorKits.add(new SuppliedSuffixSettingsEditorKit());
        return editorKits;
    }

    @SuppressWarnings("unchecked")
    public <S extends SuffixSettings> SuffixSettingsEditorKit<S> getFactory(SuffixSettingsId schemeId) {
        for(SuffixSettingsEditorKit<?> editorKit : getFactories()) {
            if(editorKit.getSchemeId().equals(schemeId)) {
                return (SuffixSettingsEditorKit<S>) editorKit;
            }
        }
        throw new RuntimeException("No factory for scheme: " + schemeId);
    }
}
