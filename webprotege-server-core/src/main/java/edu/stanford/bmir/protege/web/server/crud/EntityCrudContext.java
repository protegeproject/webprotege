package edu.stanford.bmir.protege.web.server.crud;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudContext {

    private final ProjectId projectId;

    private final PrefixedNameExpander prefixedNameExpander;

    private final UserId userId;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nullable
    private DictionaryLanguage dictionaryLanguage;

    @Nonnull
    private OWLOntologyID targetOntologyId;

    @AutoFactory
    public EntityCrudContext(@Provided @Nonnull ProjectId projectId,
                             @Nonnull UserId userId,
                             @Nonnull PrefixedNameExpander prefixedNameExpander,
                             @Provided @Nonnull ProjectDetailsRepository projectDetailsRepository,
                             @Nonnull OWLOntologyID targetOntologyId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.prefixedNameExpander = checkNotNull(prefixedNameExpander);
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
        this.targetOntologyId = checkNotNull(targetOntologyId);
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public PrefixedNameExpander getPrefixedNameExpander() {
        return prefixedNameExpander;
    }

    @Nonnull
    public OWLOntologyID getTargetOntologyId() {
        return targetOntologyId;
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
