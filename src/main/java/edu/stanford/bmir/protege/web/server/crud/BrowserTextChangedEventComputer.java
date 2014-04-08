package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.HasContainsEntityInSignature;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.List;
import java.util.Map;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class BrowserTextChangedEventComputer {

    private Map<OWLEntity, String> shortFormMap = Maps.newHashMap();

    private ShortFormProvider shortFormProvider;

    private HasGetChangeSubjects hasChangeSubject;

    private HasContainsEntityInSignature hasContainsEntityInSignature;

    public BrowserTextChangedEventComputer(HasGetChangeSubjects hasChangeSubject,
                                           ShortFormProvider shortFormProvider,
                                           HasContainsEntityInSignature hasContainsEntityInSignature) {
        this.hasChangeSubject = hasChangeSubject;
        this.shortFormProvider = shortFormProvider;
        this.hasContainsEntityInSignature = hasContainsEntityInSignature;
    }

    public void prepareForChanges(List<OWLOntologyChange> changes) {
        for(OWLOntologyChange change : changes) {
            for(OWLEntity entity : hasChangeSubject.getChangeSubjects(change)) {
                if(hasContainsEntityInSignature.containsEntityInSignature(entity)) {
                    String shortForm = shortFormProvider.getShortForm(entity);
                    shortFormMap.put(entity, shortForm);
                }
            }
        }
    }

    public List<BrowserTextChangedEvent> getShortFormChanges(List<OWLOntologyChange> changes, ProjectId projectId) {
        List<BrowserTextChangedEvent> result = Lists.newArrayList();
        for(OWLOntologyChange change : changes) {
            for(OWLEntity entity : hasChangeSubject.getChangeSubjects(change)) {
                String shortForm = shortFormProvider.getShortForm(entity);
                String oldShortForm = shortFormMap.get(entity);
                if(oldShortForm == null || !shortForm.equals(oldShortForm)) {
                    result.add(new BrowserTextChangedEvent(entity, shortForm, projectId));
                }
            }
        }
        return result;
    }

}
