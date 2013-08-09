package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.irigen.EntityIRIGenerator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public abstract class OWLEntityCrudKit {

    /**
     * Gets the factory that was used to create this {@link OWLEntityCrudKit}.
     * @return
     */
    public abstract OWLEntityEditorKitFactory getFactory();

    /**
     * Gets the settings for this {@link OWLEntityCrudKit}.
     * @return The settings for this {@link OWLEntityCrudKit}.  Not {@code null}.
     */
    public abstract OWLEntityManagerSettings getSettings();



    public abstract String getShortForm(OWLEntity entity, OWLAPIProject project);

    public abstract ShortFormSetter setShortForm(OWLEntity entity, OWLAPIProject project, String shortForm);

    public abstract EntityIRIGenerator getIRIGenerator();



}
