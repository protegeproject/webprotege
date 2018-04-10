package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.vocab.Namespaces;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
@Deprecated
@ProjectSingleton
public class WebProtegeIRIShortFormProvider implements IRIShortFormProvider {

    private static final String PREFIXED_NAME_SEPARATOR = ":";

    private final HasAnnotationAssertionAxioms annotationAssertionAxiomProvider;

    private final HasLang languageProvider;

    private final LocalNameExtractor localNameExtractor;

    private final ImmutableList<IRI> labellingIRIs;

    private final ImmutableMap<String, String> prefix2PrefixNameMap;

    @Inject
    public WebProtegeIRIShortFormProvider(ImmutableList<IRI> labellingIRIs,
                                          HasAnnotationAssertionAxioms annotationAssertionAxiomProvider,
                                          HasLang languageProvider,
                                          LocalNameExtractor localNameExtractor) {

        this.labellingIRIs = checkNotNull(labellingIRIs);
        this.annotationAssertionAxiomProvider = checkNotNull(annotationAssertionAxiomProvider);
        this.languageProvider = checkNotNull(languageProvider);
        this.localNameExtractor = localNameExtractor;

        ImmutableMap.Builder<String, String> prefixIRI2PrefixNameMapBuilder = ImmutableMap.builder();
        for (Namespaces ns : Namespaces.values()) {
            if (ns.isInUse()) {
                prefixIRI2PrefixNameMapBuilder.put(ns.getPrefixIRI(), ns.getPrefixName() + PREFIXED_NAME_SEPARATOR);
            }
        }
        prefix2PrefixNameMap = prefixIRI2PrefixNameMapBuilder.build();
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull IRI iri) {
        String localName = localNameExtractor.getLocalName(iri);
        if (!localName.isEmpty()) {
            String iriString = iri.toString();
            String prefix = iriString.substring(0, iriString.length() - localName.length());
            String prefixName = prefix2PrefixNameMap.get(prefix);
            if(prefixName != null) {
                return prefixName + localName;
            }
        }
        int matchedIndex = Integer.MAX_VALUE;
        boolean matchedDefaultLang = false;
        OWLAnnotationValue renderingValue = null;
        // Just ask for the language once (bad coding!)
        final String defaultLanguage = languageProvider.getLang();
        for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxiomProvider.getAnnotationAssertionAxioms(iri)) {
            // Think this is thread safe.  The list is immutable and each indexOf call creates a fresh iterator
            // object to find the index.
            int index = labellingIRIs.indexOf(ax.getProperty().getIRI());
            if (index <= matchedIndex && index > -1) {
                if (index < matchedIndex) {
                    matchedIndex = index;
                    renderingValue = ax.getValue();
                }
                if (index == matchedIndex) {
                    final OWLAnnotationValue value = ax.getValue();
                    if (value instanceof OWLLiteral) {
                        OWLLiteral litValue = (OWLLiteral) value;
                        String lang = litValue.getLang();
                        if (!lang.isEmpty()) {
                            if (lang.equals(defaultLanguage)) {
                                matchedDefaultLang = true;
                                renderingValue = litValue;
                            }
                            else if (!matchedDefaultLang) {
                                renderingValue = litValue;
                            }
                        }

                    }
                }


            }
        }
        if (renderingValue instanceof OWLLiteral) {
            return ((OWLLiteral) renderingValue).getLiteral();
        }
        else {
            try {
                if (localName.isEmpty()) {
                    String decodedIri = URLDecoder.decode(iri.toString(), "UTF-8");
                    return "<" + decodedIri + ">";
                }
                else {
                    return URLDecoder.decode(localName, "UTF-8");
                }
            } catch (IllegalArgumentException | UnsupportedEncodingException e) {
                return iri.toQuotedString();
            }
        }
    }
}
