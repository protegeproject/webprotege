package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
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


    private final HasAnnotationAssertionAxioms annotationAssertionAxiomProvider;

    private final HasLang languageProvider;

    private final ImmutableList<IRI> labellingIRIs;

    private final ImmutableMap<String, String> prefix2PrefixNameMap;

    public WebProtegeShortFormProvider(ImmutableList<IRI> labellingIRIs, HasAnnotationAssertionAxioms annotationAssertionAxiomProvider, HasLang languageProvider) {

        this.labellingIRIs = checkNotNull(labellingIRIs);
        this.annotationAssertionAxiomProvider = checkNotNull(annotationAssertionAxiomProvider);
        this.languageProvider = checkNotNull(languageProvider);

        ImmutableMap.Builder<String, String> prefixIRI2PrefixNameMapBuilder = ImmutableMap.builder();
        for (Namespaces ns : Namespaces.values()) {
            if (ns.isInUse()) {
                prefixIRI2PrefixNameMapBuilder.put(ns.getPrefixIRI(), ns.getPrefixName());
            }
        }
        prefix2PrefixNameMap = prefixIRI2PrefixNameMapBuilder.build();
    }

    private Optional<String> getBuiltInPrefix(OWLEntity entity) {
        IRI iri = entity.getIRI();
        String iriNS = iri.getNamespace();
        if (prefix2PrefixNameMap.containsKey(iriNS)) {
            return Optional.of(iriNS);
        } else {
            return Optional.empty();
        }
    }

    public synchronized String getShortForm(OWLEntity owlEntity) {
        try {
            if (owlEntity instanceof HasPrefixedName) {
                return ((HasPrefixedName) owlEntity).getPrefixedName();
            }
            Optional<String> builtInPrefix = getBuiltInPrefix(owlEntity);
            if (builtInPrefix.isPresent()) {
                return prefix2PrefixNameMap.get(builtInPrefix.get()) + ":" + owlEntity.getIRI().getFragment();
            }
            int matchedIndex = Integer.MAX_VALUE;
            boolean matchedDefaultLang = false;
            OWLAnnotationValue renderingValue = null;
            // Just ask for the language once (bad coding!)
            final String defaultLanguage = languageProvider.getLang();
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxiomProvider.getAnnotationAssertionAxioms(owlEntity.getIRI())) {
                // Think this is thread safe.  The list is immutable and each indexOf call creates a fresh iterator
                // object to find the index.
                int index = labellingIRIs.indexOf(ax.getProperty().getIRI());
                if (index <= matchedIndex && index > -1) {
                    if (index < matchedIndex) {
                        matchedIndex = index;
                        renderingValue = ax.getValue();
                    }
                    if (index == matchedIndex || index == Integer.MAX_VALUE) {
                        final OWLAnnotationValue value = ax.getValue();
                        if (value instanceof OWLLiteral) {
                            OWLLiteral litValue = (OWLLiteral) value;
                            String lang = litValue.getLang();
                            if (lang != null) {
                                if (lang.equals(defaultLanguage)) {
                                    matchedDefaultLang = true;
                                    renderingValue = litValue;
                                } else if (!matchedDefaultLang) {
                                    renderingValue = litValue;
                                }
                            }

                        }
                    }


                }
            }
            String result;
            if (renderingValue instanceof OWLLiteral) {
                result = ((OWLLiteral) renderingValue).getLiteral();
            } else {
                // Had this as an instance variable, but creating a new instance each time is definitely thread safe.
                SimpleShortFormProvider simpleShortFormProvider = new SimpleShortFormProvider();
                result = URLDecoder.decode(simpleShortFormProvider.getShortForm(owlEntity), "UTF-8");
            }
            if (result.contains(" ")) {
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
