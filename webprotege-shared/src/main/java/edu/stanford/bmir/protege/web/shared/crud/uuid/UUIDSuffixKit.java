package edu.stanford.bmir.protege.web.shared.crud.uuid;

import com.google.gwt.http.client.URL;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
@ApplicationSingleton
public final class UUIDSuffixKit extends EntityCrudKit<UUIDSuffixSettings> {

    public static final String EXAMPLE_SUFFIX = "RtvBaCCEyk09YwGRQljc2z";

    private static EntityCrudKitId ID = EntityCrudKitId.get("UUID");

    @Inject
    public UUIDSuffixKit() {
        super(ID, "Auto-generated Universally Unique Id (UUID)");
    }

    public static EntityCrudKitId getId() {
        return ID;
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
