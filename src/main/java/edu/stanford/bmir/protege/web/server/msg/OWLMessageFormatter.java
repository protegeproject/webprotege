package edu.stanford.bmir.protege.web.server.msg;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 25/02/2013
 */
public class OWLMessageFormatter {

    public static String formatMessage(String message, RenderingManager renderingManager, Object... objects) {
        Object[] primitiveFormattedObjects = formatToPrimitives(renderingManager, objects);
        return MessageFormat.format(message, primitiveFormattedObjects);
    }


    private static Object[] formatToPrimitives(RenderingManager renderingManager, Object... objects) {
        List<Object> result = new ArrayList<>(objects.length);
        for (Object obj : objects) {
            Object formattedObj = formatToPrimitive(obj, renderingManager);
            result.add(formattedObj);
        }
        return result.toArray();
    }

    private static Object formatToPrimitive(Object o, RenderingManager renderingManager) {
        if (o instanceof OWLObject) {
            return renderingManager.getBrowserText((OWLObject) o);
        }
        else if (o instanceof Collection<?>) {
            return formatCollection((Collection<?>) o, renderingManager);
        }
        else {
            return o;
        }
    }

    private static String formatCollection(Collection<?> collection, RenderingManager renderingManager) {
        return collection.stream()
                         .map(e -> formatToPrimitive(e, renderingManager))
                         .sorted(Comparator.comparing(e -> e.toString().toLowerCase()))
                         .map(Object::toString)
                         .collect(joining(", "));
    }

}
