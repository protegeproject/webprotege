package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermIdManager {

    private static final IRI OBO_NAMESPACE_IRI = Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasOboNamespace.getIRI();

    private static Pattern SEPARATOR_PATTERN = Pattern.compile("([^#_|_]+)(#_|_)(.+)");


    @Nonnull
    private final OboUtil oboUtil;

    private final ChangeManager changeManager;

    @Inject
    public TermIdManager(@Nonnull OboUtil oboUtil, ChangeManager changeManager) {
        this.oboUtil = oboUtil;
        this.changeManager = changeManager;
    }

    @Nonnull
    public OBOTermId getTermId(@Nonnull OWLEntity entity) {
        // IRI
        IRI iri = entity.getIRI();
        String id = toOBOId(iri);
        // rdfs:label
        String label = oboUtil.getStringAnnotationValue(iri, OWLRDFVocabulary.RDFS_LABEL.getIRI(), id);
        // namespace
        String namespace = oboUtil.getStringAnnotationValue(iri, OBO_NAMESPACE_IRI, "");
        return new OBOTermId(id, label, namespace);
    }


    private String toOBOId(IRI iri) {
        String value = iri.toString();
        String localPart;
        if (value.startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX)) {
            localPart = value.substring(Obo2OWLConstants.DEFAULT_IRI_PREFIX.length());
        }
        else if (value.startsWith(Obo2OWLConstants.OIOVOCAB_IRI_PREFIX)) {
            localPart = value.substring(Obo2OWLConstants.OIOVOCAB_IRI_PREFIX.length());
        }
        else {
            localPart = iri.getFragment();
        }
        Matcher matcher = SEPARATOR_PATTERN.matcher(localPart);
        if (matcher.matches()) {
            return matcher.group(1) + ":" + matcher.group(3);
        }
        else {
            return value;
        }
    }


    public void setTermId(@Nonnull UserId userId,
                          @Nonnull OWLEntity entity,
                          @Nonnull OBOTermId termId) {
        OBOTermId existingTermId = getTermId(entity);
        IRI iri = entity.getIRI();
        List<OWLOntologyChange> changes = new ArrayList<>();
        StringBuilder description = new StringBuilder();
        if (!existingTermId.getName().equals(termId.getName())) {
            // Update label
            changes.addAll(oboUtil.replaceStringAnnotationValue(iri, OWLRDFVocabulary.RDFS_LABEL.getIRI(), termId.getName()));
            description.append("Set term name to ");
            description.append(termId.getName());
            description.append(" ");
        }
        if (!existingTermId.getNamespace().equals(termId.getNamespace())) {
            changes.addAll(oboUtil.replaceStringAnnotationValue(iri, OBO_NAMESPACE_IRI, termId.getNamespace()));
            description.append("Set term namespace for ").append(termId.getName()).append(" to ");
            description.append(termId.getNamespace());
        }
        if (!changes.isEmpty()) {
            changeManager.applyChanges(userId,
                                       new FixedChangeListGenerator<>(changes),
                                       new FixedMessageChangeDescriptionGenerator<>(description.toString().trim()));
        }
    }


}
