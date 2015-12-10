package edu.stanford.bmir.protege.web.server.events;

import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.HasContainsEntityInSignature;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class BrowserTextChangedEventComputer implements EventTranslator {

    private Map<OWLEntity, String> shortFormMap = Maps.newHashMap();

    private final ProjectId projectId;

    private final ShortFormProvider shortFormProvider;

    private final HasGetChangeSubjects hasChangeSubject;

    private final HasContainsEntityInSignature hasContainsEntityInSignature;

    @Inject
    public BrowserTextChangedEventComputer(ProjectId projectId,
                                           ShortFormProvider shortFormProvider,
                                           HasGetChangeSubjects hasChangeSubject,
                                           HasContainsEntityInSignature hasContainsEntityInSignature) {
        this.projectId = projectId;
        this.shortFormProvider = shortFormProvider;
        this.hasChangeSubject = hasChangeSubject;
        this.hasContainsEntityInSignature = hasContainsEntityInSignature;
    }

    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {
        for(OWLOntologyChange change : submittedChanges) {
            for(OWLEntity entity : hasChangeSubject.getChangeSubjects(change)) {
                if(hasContainsEntityInSignature.containsEntityInSignature(entity)) {
                    String shortForm = shortFormProvider.getShortForm(entity);
                    shortFormMap.put(entity, shortForm);
                }
            }
        }
    }

    @Override
    public void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, List<ProjectEvent<?>> projectEventList) {
        for(OWLOntologyChange change : appliedChanges) {
            for(OWLEntity entity : hasChangeSubject.getChangeSubjects(change)) {
                String shortForm = shortFormProvider.getShortForm(entity);
                String oldShortForm = shortFormMap.get(entity);
                if(oldShortForm == null || !shortForm.equals(oldShortForm)) {
                    projectEventList.add(new BrowserTextChangedEvent(entity, shortForm, projectId));
                }
            }
        }
    }
}
