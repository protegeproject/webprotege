package edu.stanford.bmir.protege.web.server.msg;

import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.OWLObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public class OWLMessageFormatter {

    public static String formatMessage(String message, Project project, Object ... objects) {
        Object [] primitiveFormattedObjects = formatToPrimitives(project, objects);
        return MessageFormat.format(message, primitiveFormattedObjects);
    }


    private static Object [] formatToPrimitives(Project project, Object ... objects) {
        List<Object> result = new ArrayList<Object>(objects.length);
        for(Object obj : objects) {
            Object formattedObj = formatToPrimitive(obj, project);
            result.add(formattedObj);
        }
        return result.toArray();
    }

    private static Object formatToPrimitive(Object o, Project project) {
        if(o instanceof OWLObject) {
            return project.getRenderingManager().getBrowserText((OWLObject) o);
        }
        else if(o instanceof Collection<?>) {
            return formatCollection((Collection<?>) o, project);
        }
        else {
            return o;
        }
    }

    private static String formatCollection(Collection<?> collection, Project project) {
        StringBuilder sb = new StringBuilder();
        for(Iterator<?> it = collection.iterator(); it.hasNext(); ) {
            if(!it.hasNext() && collection.size() > 1) {
                sb.append(" and ");
            }
            Object o = it.next();
            Object prim = formatToPrimitive(o, project);
            sb.append(prim);
            if(it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
