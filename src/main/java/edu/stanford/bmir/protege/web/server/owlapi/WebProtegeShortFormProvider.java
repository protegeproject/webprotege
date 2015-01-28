package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.QNameShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public class WebProtegeShortFormProvider implements ShortFormProvider {

    private OWLOntology rootOntology;

    private HasLang languageProvider;

    private final List<IRI> annotationPropertyIRIs;

    private final Map<String, String> builtinPrefixes = new HashMap<String, String>();



    public WebProtegeShortFormProvider(OWLOntology rootOntology, HasLang languageProvider) {
        this.rootOntology = checkNotNull(rootOntology);
        this.languageProvider = checkNotNull(languageProvider);
        builtinPrefixes.put("owl", Namespaces.OWL.toString());
        builtinPrefixes.put("rdfs", Namespaces.RDFS.toString());
        builtinPrefixes.put("rdf", Namespaces.RDF.toString());
        builtinPrefixes.put("xsd", Namespaces.XSD.toString());
        builtinPrefixes.put("skos", Namespaces.SKOS.toString());
        builtinPrefixes.put("dc", DublinCoreVocabulary.NAME_SPACE);
        builtinPrefixes.put("foaf:", "http://xmlns.com/foaf/0.1/");
        builtinPrefixes.put("dcterms:", "http://purl.org/dc/terms/");

//        languages = new ArrayList<String>();
//        // TODO: Configurable.
//        languages.add("en");
//        languages.add("de");
//        languages.add("es");
//        languages.add("fr");

        List<IRI> annotationPropertyIRIs = new ArrayList<IRI>();
        annotationPropertyIRIs.add(SKOSVocabulary.PREFLABEL.getIRI());
        annotationPropertyIRIs.add(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        annotationPropertyIRIs.add(IRI.create("http://bibframe.org/vocab/label"));
        annotationPropertyIRIs.add(IRI.create("http://bibframe.org/vocab/title"));
        this.annotationPropertyIRIs = Collections.unmodifiableList(annotationPropertyIRIs);
    }

    private boolean startsWithBuiltInPrefix(OWLEntity entity) {
        IRI iri = entity.getIRI();
        for(String s : builtinPrefixes.values()) {
            if(iri.toString().startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    public synchronized String getShortForm(OWLEntity owlEntity) {
        try {
            if(owlEntity.isBuiltIn() || startsWithBuiltInPrefix(owlEntity)) {
                QNameShortFormProvider qNameShortFormProvider = new QNameShortFormProvider(builtinPrefixes);
                return qNameShortFormProvider.getShortForm(owlEntity);
            }
            int matchedIndex = Integer.MAX_VALUE;
//        int matchedLangIndex = Integer.MAX_VALUE;
            boolean matchedDefaultLang = false;
            OWLAnnotationValue renderingValue = null;
            // Just ask for the language once (bad coding!)
            final String defaultLanguage = languageProvider.getLang();
            for(OWLOntology ontology : rootOntology.getImportsClosure()) {
                for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(owlEntity.getIRI())) {
                    // Think this is thread safe.  The list is immutable and each indexOf call creates a fresh iterator
                    // object to find the index.
                    int index = annotationPropertyIRIs.indexOf(ax.getProperty().getIRI());
                    if(index <= matchedIndex && index > -1) {
                        if (index < matchedIndex) {
                            matchedIndex = index;
                            renderingValue = ax.getValue();
                        }
                        if(index == matchedIndex || index == Integer.MAX_VALUE) {
                            final OWLAnnotationValue value = ax.getValue();
                            if (value instanceof OWLLiteral) {
                                OWLLiteral litValue = (OWLLiteral) value;
                                String lang = litValue.getLang();
                                if(lang != null) {
                                    if(lang.equals(defaultLanguage)) {
                                        matchedDefaultLang = true;
                                        renderingValue = litValue;
                                    }
                                    else if(!matchedDefaultLang) {
                                        renderingValue = litValue;
                                    }
                                }

                            }
                        }


                    }
                }
            }
            String result;
            if(renderingValue instanceof OWLLiteral) {
                result = ((OWLLiteral) renderingValue).getLiteral();
            }
            else {
                // Had this as an instance variable, but creating a new instance each time is definitely thread safe.
                SimpleShortFormProvider simpleShortFormProvider = new SimpleShortFormProvider();
                result = URLDecoder.decode(simpleShortFormProvider.getShortForm(owlEntity), "UTF-8");
            }
            if(result.contains(" ")) {
                result = getQuoted(result);
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getQuoted(String result) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("'");
//        sb.append(result);
//        sb.append("'");
//        return sb;
        return result;
    }

    public void dispose() {
    }


}
