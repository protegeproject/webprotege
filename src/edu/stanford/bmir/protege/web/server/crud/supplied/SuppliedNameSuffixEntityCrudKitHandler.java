package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

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
    public <E extends OWLEntity> void create(EntityType<E> entityType, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        IRI iri = createEntityIRI(shortForm);
        final OWLDataFactory dataFactory = context.getDataFactory();
        E entity = dataFactory.getOWLEntity(entityType, iri);
        OWLDeclarationAxiom ax = dataFactory.getOWLDeclarationAxiom(entity);
        changeListBuilder.addAxiom(context.getTargetOntology(), ax);
    }

    private IRI createEntityIRI(EntityShortForm shortForm) {
        return IRI.create(prefixSettings.getIRIPrefix() + shortForm.getShortForm().replaceAll(" ", "%20"));
    }

    @Override
    public <E extends OWLEntity> void update(E entity, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        OWLEntityRenamer renamer = new OWLEntityRenamer(context.getTargetOntology().getOWLOntologyManager(), context.getTargetOntology().getImportsClosure());
        List<OWLOntologyChange> changeList = renamer.changeIRI(entity, createEntityIRI(shortForm));
        changeListBuilder.addAll(changeList);
    }

    @Override
    public <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context) {
        String iriString = entity.getIRI().toString();
        final String iriPrefix = prefixSettings.getIRIPrefix();
        if(iriString.startsWith(iriPrefix)) {
            return iriString.substring(iriPrefix.length());
        }
        else {
            return iriString;
        }
    }
}
