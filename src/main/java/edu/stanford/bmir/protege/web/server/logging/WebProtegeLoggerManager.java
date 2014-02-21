package edu.stanford.bmir.protege.web.server.logging;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public class WebProtegeLoggerManager {


    public static WebProtegeLogger get(Class<?> cls) {
        return new DefaultLogger(cls);
    }




}
