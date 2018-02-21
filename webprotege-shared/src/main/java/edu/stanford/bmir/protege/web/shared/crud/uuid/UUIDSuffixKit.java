package edu.stanford.bmir.protege.web.shared.crud.uuid;

import com.google.gwt.http.client.URL;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public final class UUIDSuffixKit extends EntityCrudKit<UUIDSuffixSettings> {

    public static final String EXAMPLE_SUFFIX = "RtvBaCCEyk09YwGRQljc2z";

    private static EntityCrudKitId ID = EntityCrudKitId.get("UUID");

    private static final UUIDSuffixKit INSTANCE = new UUIDSuffixKit();

    private UUIDSuffixKit() {
        super(ID, "Auto-generated Universally Unique Id (UUID)");
    }

    public static UUIDSuffixKit get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings();
    }

    @Override
    public UUIDSuffixSettings getDefaultSuffixSettings() {
        return new UUIDSuffixSettings();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(!(prefix.endsWith("#") || prefix.endsWith("/"))) {
            return Optional.of("It is recommended that your prefix ends with a forward slash i.e. <b>/</b> (or a #)");
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, UUIDSuffixSettings suffixSettings) {
        return IRI.create(URL.encode(prefixSettings.getIRIPrefix()), EXAMPLE_SUFFIX);
    }
}
