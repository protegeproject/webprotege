package edu.stanford.bmir.protege.web.server.msg;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
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

    public static String formatMessage(String message, RenderingManager renderingManager, Object ... objects) {
        Object [] primitiveFormattedObjects = formatToPrimitives(renderingManager, objects);
        return MessageFormat.format(message, primitiveFormattedObjects);
    }


    private static Object [] formatToPrimitives(RenderingManager renderingManager, Object ... objects) {
        List<Object> result = new ArrayList<Object>(objects.length);
        for(Object obj : objects) {
            Object formattedObj = formatToPrimitive(obj, renderingManager);
            result.add(formattedObj);
        }
        return result.toArray();
    }

    private static Object formatToPrimitive(Object o, RenderingManager renderingManager) {
        if(o instanceof OWLObject) {
            return renderingManager.getBrowserText((OWLObject) o);
        }
        else if(o instanceof Collection<?>) {
            return formatCollection((Collection<?>) o, renderingManager);
        }
        else {
            return o;
        }
    }

    private static String formatCollection(Collection<?> collection, RenderingManager renderingManager) {
        StringBuilder sb = new StringBuilder();
        for(Iterator<?> it = collection.iterator(); it.hasNext(); ) {
            if(!it.hasNext() && collection.size() > 1) {
                sb.append(" and ");
            }
            Object o = it.next();
            Object prim = formatToPrimitive(o, renderingManager);
            sb.append(prim);
            if(it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
