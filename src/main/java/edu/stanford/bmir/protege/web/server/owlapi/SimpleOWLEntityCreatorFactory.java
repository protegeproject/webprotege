package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.IdUtil;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class SimpleOWLEntityCreatorFactory extends OWLEntityCreatorFactory {

    private static Map<String, String> WELL_KNOWN_PREFIXES;

    /**
     * A start char for local names.  Some UUIDs might start with a number.  Unfortunately, NCNames (non-colonised names)
     * in XML cannot start with numbers.  For everything apart from properties this is o.k. but for properties it means
     * that it might not be possible to save an ontology in RDF/XML.  We therefore prefix each local name with a valid
     * NCName start char - "R".  The character "R" was chosen so as not to encode the type into the name.  I initially
     * considered C for classes, P properties etc. however with punning this would get ugly.
     */
    private static final String START_CHAR = "R";

    static {
        // This is thread safe - hash maps are for multiple readers
        WELL_KNOWN_PREFIXES = new HashMap<String, String>();
        WELL_KNOWN_PREFIXES.put("owl", Namespaces.OWL.toString());
        WELL_KNOWN_PREFIXES.put("rdf", Namespaces.RDF.toString());
        WELL_KNOWN_PREFIXES.put("rdfs", Namespaces.RDFS.toString());
        WELL_KNOWN_PREFIXES.put("skos", Namespaces.SKOS.toString());
        WELL_KNOWN_PREFIXES.put("dc", DublinCoreVocabulary.NAME_SPACE);
        WELL_KNOWN_PREFIXES.put("dbpedia", "http://dbpedia.org/resource/");
        WELL_KNOWN_PREFIXES.put("dbp", "http://dbpedia.org/property/");
        WELL_KNOWN_PREFIXES.put("dbo", "http://dbpedia.org/ontology/");
        WELL_KNOWN_PREFIXES.put("foaf", "http://xmlns.com/foaf/0.1/");
        WELL_KNOWN_PREFIXES.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        WELL_KNOWN_PREFIXES.put("vcard", "http://www.w3.org/2006/vcard/ns#");
        WELL_KNOWN_PREFIXES.put("yago", "http://dbpedia.org/class/yago/");
        WELL_KNOWN_PREFIXES.put("sc", "http://purl.org/science/owl/sciencecommons/");
        WELL_KNOWN_PREFIXES.put("fb", "http://rdf.freebase.com/ns/");
        WELL_KNOWN_PREFIXES.put("geonames", "http://www.geonames.org/ontology#");
        WELL_KNOWN_PREFIXES.put("sc", "http://rdfs.org/sioc/ns#");
        WELL_KNOWN_PREFIXES.put("gr", "http://purl.org/goodrelations/v1#");
        WELL_KNOWN_PREFIXES.put("dcterms", "http://purl.org/dc/terms/");
        WELL_KNOWN_PREFIXES.put("cc", "http://creativecommons.org/ns#");
    }
    
    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType) {
        OWLOntology rootOntology = project.getRootOntology();
        IRI entityIRI = null;
        if(isAbsoluteWebIRI(shortName)) {
            entityIRI = IRI.create(shortName);
        }

        int colonIndex = shortName.indexOf(":");
        if(colonIndex != -1) {
            String prefixName = shortName.substring(0, colonIndex);
            String prefix = WELL_KNOWN_PREFIXES.get(prefixName);
            if(prefix != null) {
                entityIRI = IRI.create(prefix, shortName.substring(colonIndex + 1));
            }
        }
        if(entityIRI == null) {
            OWLOntologyID id = rootOntology.getOntologyID();
            String base = getDefaultIRIBase(id, entityType);
            String fragment = createFragment(base, rootOntology, entityType);
            entityIRI = IRI.create(base + fragment);

        }

        OWLDataFactory dataFactory = project.getDataFactory();
        E entity = dataFactory.getOWLEntity(entityType, entityIRI);
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(entity);
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddAxiom(rootOntology, declarationAxiom));

        OWLLiteral labellingLiteral = dataFactory.getOWLLiteral(shortName, project.getDefaultLanguage());
        OWLAnnotationAssertionAxiom labellingAssertion = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getRDFSLabel(), entityIRI, labellingLiteral);
        changes.add(new AddAxiom(rootOntology, labellingAssertion));
        return new OWLEntityCreator<E>(entity, changes);
    }

    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> setBrowserText(OWLAPIProject project, UserId userId, E entity, String browserText) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        final OWLDataFactory df = project.getDataFactory();
        OWLLiteral browserTextLiteral = df.getOWLLiteral(browserText, df.getRDFPlainLiteral());
        OWLAxiom freshAx = df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), entity.getIRI(), browserTextLiteral);
        for(OWLOntology ont : project.getRootOntology().getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if(ax.getProperty().isLabel()) {
                    changes.add(new RemoveAxiom(ont, ax));
                    changes.add(new AddAxiom(ont, freshAx));
                }
            }
        }
        if(changes.isEmpty()) {
            changes.add(new AddAxiom(project.getRootOntology(), freshAx));
        }
        return new OWLEntityCreator<E>(entity, changes);
    }

    private String createFragment(String base, OWLOntology ontology, EntityType<?> entityType) {
        while (true) {
            String base62Fragment = IdUtil.getBase62UUID();
            StringBuilder sb = new StringBuilder();
            sb.append(base);
            sb.append(START_CHAR);
            sb.append(base62Fragment);
            IRI iri = IRI.create(sb.toString());
            if(!ontology.containsEntityInSignature(iri)) {
                StringBuilder fragBuilder = new StringBuilder();
                fragBuilder.append(START_CHAR);
                fragBuilder.append(base62Fragment);
                return fragBuilder.toString();
            }
        }
    }


    private boolean isAbsoluteWebIRI(String shortName) {
        return !shortName.contains(" ") && shortName.startsWith("http:");
    }
}
