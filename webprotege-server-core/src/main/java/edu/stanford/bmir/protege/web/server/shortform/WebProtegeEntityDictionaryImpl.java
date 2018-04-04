package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
public class WebProtegeEntityDictionaryImpl implements WebProtegeEntityDictionary {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private String defaultLang = "";

    private final Predicate<OWLAnnotationAssertionAxiom> labellingPredicate;

    private final Table<IRI, String, String> shortFormsByIri = HashBasedTable.create();

    private final Multimap<String, IRI> irisByShortForm = HashMultimap.create();

    private final Set<String> langs = new HashSet<>();

    private final LocalNameExtractor localNameExtractor;

    public WebProtegeEntityDictionaryImpl(@Nonnull ProjectId projectId,
                                          @Nonnull OWLOntology rootOntology,
                                          @Nonnull Predicate<OWLAnnotationAssertionAxiom> labellingPredicate,
                                          LocalNameExtractor localNameExtractor) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
        this.labellingPredicate = checkNotNull(labellingPredicate);
        this.localNameExtractor = checkNotNull(localNameExtractor);
    }

    @Override
    public Collection<String> getLangs() {
        return new ArrayList<>(langs);
    }

    public void rebuild() {
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION, Imports.EXCLUDED).stream())
                    .filter(ax -> ax.getValue().isLiteral())
                    .filter(ax -> ax.getSubject().isIRI())
                    .filter(labellingPredicate::apply)
                    .forEach(this::index);

    }

    private void index(OWLAnnotationAssertionAxiom ax) {
        ax.ifLiteral(value -> {
            IRI iri = (IRI) ax.getSubject();
            String literal = value.getLiteral();
            String lang = value.getLang();
            shortFormsByIri.put(iri,
                                lang,
                                literal);
            irisByShortForm.put(literal, iri);
            langs.add(lang);
        });
    }

    private void drop(@Nonnull IRI iri) {
        Collection<String> langs = new ArrayList<>(shortFormsByIri.row(iri).keySet());
        langs.forEach(lang -> shortFormsByIri.remove(iri, lang));
    }

    private void index(@Nonnull IRI iri) {
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAnnotationAssertionAxioms(iri).stream())
                    .filter(ax -> ax.getValue().isLiteral())
                    .filter(ax -> ax.getSubject().isIRI())
                    .filter(labellingPredicate::apply)
                    .forEach(this::index);

    }

    @Override
    public void setDefaultLang(@Nonnull String lang) {
        defaultLang = checkNotNull(lang);
    }

    @Nonnull
    @Override
    public String getDefaultLang() {
        return defaultLang;
    }

    @Nonnull
    @Override
    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return irisByShortForm.get(shortForm).stream()
                              .flatMap(iri -> rootOntology.getEntitiesInSignature(iri, Imports.INCLUDED).stream())
                              .collect(toSet());
    }

    @Nonnull
    @Override
    public String getShortFormForLangOrEmptyLang(@Nonnull OWLEntity entity, @Nonnull String lang) {
        String shortForm = shortFormsByIri.get(entity.getIRI(), lang);
        if(shortForm != null) {
            return shortForm;
        }
        else {
            return getIriShortForm(entity.getIRI());
        }
    }

    @Nonnull
    @Override
    public String getShortFormForDefaultOrEmptyLang(@Nonnull OWLEntity entity) {
        String defaultLangShortForm = shortFormsByIri.get(entity.getIRI(), defaultLang);
        if(defaultLangShortForm != null) {
            return defaultLangShortForm;
        }
        if(defaultLang.isEmpty()) {
            return getIriShortForm(entity.getIRI());
        }
        String emptyLangShortForm = shortFormsByIri.get(entity.getIRI(), "");
        if(emptyLangShortForm != null) {
            return emptyLangShortForm;
        }
        return getIriShortForm(entity.getIRI());
    }

    private String getIriShortForm(@Nonnull IRI iri) {
        String localName = localNameExtractor.getLocalName(iri);
        if(localName.isEmpty()) {
            return iri.toString();
        }
        else {
            return localName;
        }
    }

    @Nonnull
    @Override
    public Collection<String> getShortForms(@Nonnull String lang) {
        return shortFormsByIri.column(lang).values();
    }

    @Override
    public void handleChanges(List<? extends OWLOntologyChange> changes) {
        Set<IRI> affectedIris = changes.stream()
                                  .filter(OWLOntologyChange::isAxiomChange)
                                  .map(OWLOntologyChange::getAxiom)
                                  .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                                  .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                                  .filter(ax -> ax.getValue().isLiteral())
                                  .filter(ax -> ax.getSubject().isIRI())
                                  .filter(labellingPredicate::apply)
                                  .map(ax -> (IRI) ax.getSubject())
                                  .collect(toSet());
        affectedIris.forEach(this::drop);
        affectedIris.forEach(this::index);
    }


}
