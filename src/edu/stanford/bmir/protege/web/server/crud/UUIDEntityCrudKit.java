package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.IdUtil;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UUIDEntityCrudKit implements EntityCrudKit {

    /**
     * A start char for local names.  Some UUIDs might start with a number.  Unfortunately, NCNames (non-colonised names)
     * in XML cannot start with numbers.  For everything apart from properties this is o.k. but for properties it means
     * that it might not be possible to save an ontology in RDF/XML.  We therefore prefix each local name with a valid
     * NCName start char - "R".  The character "R" was chosen so as not to encode the type into the name.  I initially
     * considered C for classes, P properties etc. however with punning this would get ugly.
     */
    private static final String START_CHAR = "R";

    private EntityCrudKitPrefixSettings prefixSettings;

    private UUIDSuffixSettings suffixSettings;

    public UUIDEntityCrudKit() {
        this(new EntityCrudKitPrefixSettings(), new UUIDSuffixSettings());
    }

    public UUIDEntityCrudKit(EntityCrudKitPrefixSettings prefixSettings, UUIDSuffixSettings uuidSuffixKitSettings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = uuidSuffixKitSettings;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return suffixSettings.getKitId();
    }

    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public EntityCrudKitSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context) {
        for(OWLOntology ontology : context.getTargetOntology().getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                if(ax.getProperty().isLabel() && ax.getValue() instanceof OWLLiteral) {
                    OWLLiteral literal = (OWLLiteral) ax.getValue();
                    return literal.getLiteral();
                }
            }
        }
        return entity.getIRI().toString();
    }

    @Override
    public <E extends OWLEntity> void create(EntityType<E> entityType, final EntityShortForm shortForm, final EntityCrudContext context, final OntologyChangeList.Builder<E> builder) {
        OWLDataFactory dataFactory = context.getDataFactory();
        final OWLOntology targetOntology = context.getTargetOntology();
        final IRI iri = createIRI(prefixSettings.getIRIPrefix(), targetOntology);
        final E entity =  dataFactory.getOWLEntity(entityType, iri);
        builder.addAxiom(targetOntology, dataFactory.getOWLDeclarationAxiom(entity));
        final OWLLiteral labellingLiteral = getLabellingLiteral(shortForm, context);
        OWLAnnotationAssertionAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getRDFSLabel(), entity.getIRI(), labellingLiteral);
        builder.addAxiom(targetOntology, ax);
    }

    @Override
    public <E extends OWLEntity> void update(E entity, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        final OWLDataFactory df = context.getDataFactory();
        OWLLiteral browserTextLiteral = getLabellingLiteral(shortForm, context);
        OntologyChangeList.Builder<E> builder = new OntologyChangeList.Builder<E>();

        OWLAxiom freshAx = df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), entity.getIRI(), browserTextLiteral);
        final OWLOntology targetOntology = context.getTargetOntology();
        for(OWLOntology ont : targetOntology.getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if(ax.getProperty().isLabel()) {
                    builder.removeAxiom(ont, ax);
                    builder.addAxiom(ont, freshAx);
                }
            }
        }
        if(builder.isEmpty()) {
            builder.addAxiom(targetOntology, freshAx);
        }
    }

    private static IRI createIRI(String base, OWLOntology ontology) {
        while (true) {
            String base62Fragment = IdUtil.getBase62UUID();
            StringBuilder sb = new StringBuilder();
            sb.append(base);
            sb.append(START_CHAR);
            sb.append(base62Fragment);
            IRI iri = IRI.create(sb.toString());
            if(!ontology.containsEntityInSignature(iri)) {
                return iri;
            }
        }
    }


    private static OWLLiteral getLabellingLiteral(EntityShortForm shortForm, EntityCrudContext context) {
        OWLDataFactory dataFactory = context.getDataFactory();
        return dataFactory.getOWLLiteral(shortForm.getShortForm(), context.getTargetLanguage().or(""));
    }

}
