package edu.stanford.bmir.protege.web.shared.crud.supplied;

import com.google.common.base.Optional;
import com.google.gwt.http.client.URL;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixKit extends EntityCrudKit<SuppliedNameSuffixSettings> {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    private static final SuppliedNameSuffixKit INSTANCE = new SuppliedNameSuffixKit();

    private SuppliedNameSuffixKit() {
        super(ID, "Supplied name");
    }

    public static SuppliedNameSuffixKit get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings();
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSuffixSettings() {
        return new SuppliedNameSuffixSettings();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(!(prefix.endsWith("#") || prefix.endsWith("/"))) {
            return Optional.of("It is recommended that your prefix ends with a forward slash i.e. <b>/</b> (or a #)");
        }
        else {
            return Optional.absent();
        }
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, SuppliedNameSuffixSettings suffixSettings) {
        return IRI.create(URL.encode(prefixSettings.getIRIPrefix()), "Person");
    }
}
