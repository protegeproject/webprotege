package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Iterator;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public class CreateClassesChangeDescriptionGenerator implements ChangeDescriptionGenerator<Set<OWLClass>> {



    @Override
    public String generateChangeDescription(ChangeApplicationResult<Set<OWLClass>> result) {
        final Optional<Set<OWLClass>> subject = result.getSubject();
        if(!subject.isPresent()) {
            return "Created class";
        }
        Set<OWLClass> clses = subject.get();
        StringBuilder sb = new StringBuilder();
        for(Iterator<OWLClass> it = clses.iterator(); it.hasNext(); ) {
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(" as subclasses of ");
        return sb.toString();
    }
}
