package edu.stanford.bmir.protege.web.server.search;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import org.apache.commons.lang.StringUtils;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.util.ProtegeStreams.entityStream;
import static edu.stanford.bmir.protege.web.shared.search.SearchField.displayName;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 */
class EntitySearcher {

    @Nonnull
    private final OWLOntology rootOntology;

    private HasGetRendering renderingSupplier;

    @Nonnull
    private final Set<EntityType<?>> entityTypes;

    @Nonnull
    private final String[] searchWords;

    private final Counter counter = new Counter();

    private final List<EntitySearchResult> results = new ArrayList<>();

    public EntitySearcher(@Nonnull OWLOntology rootOntology,
                          @Nonnull HasGetRendering renderingSupplier,
                          @Nonnull Set<EntityType<?>> entityTypes,
                          @Nonnull String searchString) {
        this.rootOntology = checkNotNull(rootOntology);
        this.renderingSupplier = checkNotNull(renderingSupplier);
        this.entityTypes = new HashSet<>(entityTypes);
        this.searchWords = searchString.split("\\s+");
    }

    public int getTotalSearchResultsCount() {
        return counter.getCounter();
    }

    public List<EntitySearchResult> getResults() {
        return results;
    }

    public EntitySearcher invoke() {
        counter.reset();
        StringBuilder searchWordsPattern = new StringBuilder();
        for (int i = 0; i < searchWords.length; i++) {
            searchWordsPattern.append(Pattern.quote(searchWords[i]));
            if (i < searchWords.length - 1) {
                searchWordsPattern.append("|");
            }
        }
        Pattern searchPattern = Pattern.compile(searchWordsPattern.toString(), CASE_INSENSITIVE);

        results.clear();
        entityStream(entityTypes, rootOntology, Imports.INCLUDED)
                .map(e -> {
                    OWLEntityData rendering = renderingSupplier.getRendering(e);
                    boolean matchedRendering = true;
                    for (String searchWord : searchWords) {
                        if (!StringUtils.containsIgnoreCase(rendering.getBrowserText(),
                                                            searchWord)) {
                            matchedRendering = false;
                            break;
                        }
                    }
                    boolean matchedIRI = true;
                    IRI entityIri = rendering.getEntity().getIRI();
                    if(!matchedRendering && entityIri.toString().startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX)) {
                        Optional<String> remainder = entityIri.getRemainder();
                        if (remainder.isPresent()) {
                            for (String searchWord : searchWords) {
                                if (!StringUtils.containsIgnoreCase(remainder.get(),
                                                                    searchWord)) {
                                    matchedIRI = false;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        matchedIRI = false;
                    }
                    if (matchedRendering || matchedIRI) {
                        return new Wrapper(searchWords, rendering, matchedRendering);
                    }
                    else {
                        return null;
                    }

                })
                .filter(e -> e != null)
                .peek(e -> counter.increment())
                .sorted()
                .limit(50)
                .map(ren -> {
                    OWLEntityData ed = ren.entityData;
                    String rendering = ed.getBrowserText();
                    StringBuilder highlighted = new StringBuilder();
                    highlightSearchResult(searchPattern, rendering, highlighted);
                    if(!ren.isMatchedRendering()) {
                        // Matched the IRI remainder
                        highlighted.append("<div class=\"searchedIri\">");
                        IRI iri = ed.getEntity().getIRI();
                        highlightSearchResult(searchPattern, iri.toString(), highlighted);
                        highlighted.append("</div>");
                    }
                    return new EntitySearchResult(ed,
                                                  displayName(),
                                                  highlighted.toString());
                })
                .forEach(results::add);
        return this;
    }

    private void highlightSearchResult(Pattern searchPattern, String rendering, StringBuilder highlighted) {
        Matcher matcher = searchPattern.matcher(rendering);
        int cur = 0;
        while (matcher.find()) {
            int start = matcher.start();
            highlighted.append(rendering.substring(cur, start));
            highlighted.append("<strong>");
            int end = matcher.end();
            highlighted.append(rendering.substring(start, end));
            highlighted.append("</strong>");
            cur = end;
        }
        if (cur < rendering.length()) {
            highlighted.append(rendering.substring(cur));
        }
    }


    private static class Counter {

        private int counter;

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }

        public void reset() {
            counter = 0;
        }
    }

    private static class Wrapper implements Comparable<Wrapper> {


        private final String [] searchWords;

        private final OWLEntityData entityData;

        private final boolean matchedRendering;

        public Wrapper(@Nonnull String[] searchWords,
                       @Nonnull OWLEntityData entityData,
                       boolean matchedRendering) {
            this.searchWords = searchWords;
            this.entityData = entityData;
            this.matchedRendering = matchedRendering;
        }

        public boolean isMatchedRendering() {
            return matchedRendering;
        }

        @Override
        public int compareTo(@Nonnull Wrapper other) {
            for(String searchWord : searchWords) {
                int usStart = StringUtils.indexOfIgnoreCase(entityData.getBrowserText(), searchWord);
                int otherStart = StringUtils.indexOfIgnoreCase(other.entityData.getBrowserText(), searchWord);
                if(usStart < otherStart) {
                    return -1;
                }
                else if(otherStart < usStart) {
                    return 1;
                }
            }
            return entityData.getBrowserText().compareToIgnoreCase(other.entityData.getBrowserText());
        }
    }
}
