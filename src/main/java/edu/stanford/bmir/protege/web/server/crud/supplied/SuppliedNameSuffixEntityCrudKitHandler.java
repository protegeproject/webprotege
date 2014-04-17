package edu.stanford.bmir.protege.web.server.crud.supplied;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.PrefixedNameExpander;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import org.semanticweb.owlapi.io.XMLUtils;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixEntityCrudKitHandler implements EntityCrudKitHandler<SuppliedNameSuffixSettings> {

    private SuppliedNameSuffixSettings suffixSettings;

    private EntityCrudKitPrefixSettings prefixSettings;

    public SuppliedNameSuffixEntityCrudKitHandler() {
        this(new EntityCrudKitPrefixSettings(), new SuppliedNameSuffixSettings());
    }

    public SuppliedNameSuffixEntityCrudKitHandler(EntityCrudKitPrefixSettings prefixSettings, SuppliedNameSuffixSettings settings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = settings;
    }

    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return SuppliedNameSuffixKit.get().getKitId();
    }

    @Override
    public SuppliedNameSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public EntityCrudKitSettings<SuppliedNameSuffixSettings> getSettings() {
        return new EntityCrudKitSettings<SuppliedNameSuffixSettings>(prefixSettings, suffixSettings);
    }

    @Override
    public <E extends OWLEntity> E create(EntityType<E> entityType, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        IRI iri = createEntityIRI(shortForm, context.getPrefixedNameExpander());
        final OWLDataFactory dataFactory = context.getDataFactory();
        E entity = dataFactory.getOWLEntity(entityType, iri);
        OWLDeclarationAxiom ax = dataFactory.getOWLDeclarationAxiom(entity);
        changeListBuilder.addAxiom(context.getTargetOntology(), ax);
        return entity;
    }

    private IRI createEntityIRI(EntityShortForm shortForm, PrefixedNameExpander prefixedNameExpander) {
        try {
            WhiteSpaceTreatment whiteSpaceTreatment = suffixSettings.getWhiteSpaceTreatment();
            String transformedShortForm = whiteSpaceTreatment.transform(shortForm.getShortForm());
            Optional<IRI> expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(transformedShortForm);
            if(expandedPrefixName.isPresent()) {
                return expandedPrefixName.get();
            }
            String escapedShortForm = URLEncoder.encode(transformedShortForm, "UTF-8");
            return IRI.create(prefixSettings.getIRIPrefix() + escapedShortForm);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported!");
        }
    }

    @Override
    public <E extends OWLEntity> void update(E entity, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        OWLEntityRenamer renamer = new OWLEntityRenamer(context.getTargetOntology().getOWLOntologyManager(), context.getTargetOntology().getImportsClosure());
        List<OWLOntologyChange> changeList = renamer.changeIRI(entity, createEntityIRI(shortForm, context.getPrefixedNameExpander()));
        changeListBuilder.addAll(changeList);
    }

    @Override
    public <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context) {
        try {
            String iriString = entity.getIRI().toString();
            final String iriPrefix = prefixSettings.getIRIPrefix();
            String name = XMLUtils.getNCNameSuffix(iriPrefix);
            if(name == null) {
                return iriString;
            }
            return URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported!");
        }
    }
}
