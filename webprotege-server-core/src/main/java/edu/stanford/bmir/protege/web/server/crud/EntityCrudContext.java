package edu.stanford.bmir.protege.web.server.crud;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudContext implements HasDataFactory {

    private final ProjectId projectId;

    private final OWLOntology targetOntology;

    private final OWLDataFactory dataFactory;

    private final PrefixedNameExpander prefixedNameExpander;

    private final UserId userId;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nullable
    private DictionaryLanguage dictionaryLanguage;

    @AutoFactory
    public EntityCrudContext(@Provided @Nonnull ProjectId projectId,
                             @Nonnull UserId userId,
                             @Provided @Nonnull OWLOntology targetOntology,
                             @Provided @Nonnull OWLDataFactory dataFactory,
                             @Nonnull PrefixedNameExpander prefixedNameExpander,
                             @Provided @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.targetOntology = checkNotNull(targetOntology);
        this.dataFactory = checkNotNull(dataFactory);
        this.prefixedNameExpander = checkNotNull(prefixedNameExpander);
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
    }

    @Nonnull
    public OWLOntology getTargetOntology() {
        return targetOntology;
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    @Nonnull
    public PrefixedNameExpander getPrefixedNameExpander() {
        return prefixedNameExpander;
    }

    @Nonnull
    public DictionaryLanguage getDictionaryLanguage() {
        if(dictionaryLanguage == null) {
            dictionaryLanguage = projectDetailsRepository.findOne(projectId)
                                                                .map(ProjectDetails::getDefaultDictionaryLanguage)
                                                                .orElse(DictionaryLanguage.rdfsLabel(""));
        }
        return dictionaryLanguage;
    }
}
