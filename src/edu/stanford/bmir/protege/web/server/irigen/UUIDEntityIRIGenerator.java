package edu.stanford.bmir.protege.web.server.irigen;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.IdUtil;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettings;
import edu.stanford.bmir.protege.web.shared.irigen.uuid.UUIDSuffixSettings;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
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
 * Date: 08/08/2013
 */
public class UUIDEntityIRIGenerator extends EntityIRIGenerator {

    private static Map<String, String> WELL_KNOWN_PREFIXES;

    /**
     * A start char for local names.  Some UUIDs might start with a number.  Unfortunately, NCNames (non-colonised names)
     * in XML cannot start with numbers.  For everything apart from properties this is o.k. but for properties it means
     * that it might not be possible to save an ontology in RDF/XML.  We therefore prefix each local name with a valid
     * NCName start char - "T".  The character "T" was chosen so as not to encode the type into the name.  I initially
     * considered C for classes, P properties etc. however with punning this would get ugly.
     */
    private static final String START_CHAR = "T";

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
    public IRI getEntityIRI(IRIGeneratorSettings iriGeneratorSettings, String shortForm, EntityType<?> entityType, OWLAPIProject project) {
        final SuffixSettings suffixSettings = iriGeneratorSettings.getSchemeSpecificSettings();
        if(!(suffixSettings instanceof UUIDSuffixSettings)) {
            throw new RuntimeException("Invalid settings");
        }
        UUIDSuffixSettings uuidSuffixSettings = (UUIDSuffixSettings) suffixSettings;
        Optional<String> labelLang = uuidSuffixSettings.getLabelLang();
        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();

        return null;
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

                fragBuilder.append(base62Fragment);
                return fragBuilder.toString();
            }
        }
    }


    private boolean isAbsoluteWebIRI(String shortName) {
        return !shortName.contains(" ") && shortName.startsWith("http:");
    }
}

