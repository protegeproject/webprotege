package edu.stanford.bmir.protege.web.server.owlapi;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/06/2012
 */
public class DefaultEntityEditorKitFactory extends OWLAPIEntityEditorKitFactory {

    @Override
    public OWLAPIEntityEditorKit createEntityEditorKit(OWLAPIProject project) {
        return new DefaultEntityEditorKit(project);
    }
}
