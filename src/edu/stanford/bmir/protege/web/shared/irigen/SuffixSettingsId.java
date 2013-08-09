package edu.stanford.bmir.protege.web.shared.irigen;


import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuffixSettingsId implements Serializable {

    private String lexicalForm;

    private String displayName;

    /**
     * For serialization only
     */
    private SuffixSettingsId() {
    }

    private SuffixSettingsId(String lexicalForm, String displayName) {
        this.lexicalForm = checkNotNull(lexicalForm);
        this.displayName = checkNotNull(displayName);
    }

    /**
     * Gets a {@link SuffixSettingsId} for the specified lexical form.  Note:  Only the specified lexical form determines
     * the equality of this object with other {@link SuffixSettingsId} objects.
     * @param lexicalForm The lexical form of the id.  Not {@code null}.
     * @param displayName The display name for the id.  Not {@code null}.
     * @return The {@link SuffixSettingsId} for the specified lexical form.  Not {@code null}.
     * @throws NullPointerException if any arguments are {@code null}.
     */
    public static SuffixSettingsId get(String lexicalForm, String displayName) {
        return new SuffixSettingsId(lexicalForm, displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int hashCode() {
        return "IRIGeneratorSchemeId".hashCode() + lexicalForm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SuffixSettingsId)) {
            return false;
        }
        SuffixSettingsId other = (SuffixSettingsId) obj;
        return this.lexicalForm.equals(other.lexicalForm);
    }
}
