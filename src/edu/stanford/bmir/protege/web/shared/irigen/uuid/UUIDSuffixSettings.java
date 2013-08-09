package edu.stanford.bmir.protege.web.shared.irigen.uuid;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UUIDSuffixSettings extends SuffixSettings {

    public static final SuffixSettingsId Id = SuffixSettingsId.get("UUID", "Generate globally unique Id");

    private Optional<String> labelLang;

    public UUIDSuffixSettings() {
        this(Optional.<String>absent());
    }

    public UUIDSuffixSettings(Optional<String> lang) {
        super(Id);
        this.labelLang = lang;
    }

    public Optional<String> getLabelLang() {
        return labelLang;
    }

    @Override
    public int hashCode() {
        return "UUIDSchemeSpecificSettings".hashCode() + getId().hashCode() + labelLang.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UUIDSuffixSettings)) {
            return false;
        }
        UUIDSuffixSettings other = (UUIDSuffixSettings) obj;
        return this.getId().equals(other.getId()) && this.labelLang.equals(other.labelLang);
    }

//    @Override
//    public String generateExample(String base) {
//        return base + "AX1QD6nm4vKi5qr84PqgXI";
//    }
}
