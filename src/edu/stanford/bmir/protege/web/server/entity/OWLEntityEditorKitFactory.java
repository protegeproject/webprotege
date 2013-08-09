package edu.stanford.bmir.protege.web.server.entity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public abstract class OWLEntityEditorKitFactory {

    /**
     * Create an {@link OWLEntityCrudKit} with the default settings.
     * @return An {@link OWLEntityCrudKit} with the default settings. Not {@code null}.
     */
    public abstract OWLEntityCrudKit createOWLEntityEditorKit();

    /**
     * Create an {@link OWLEntityCrudKit} with the specified settings.
     * @param settings The settings.  Not {@code null}.  These settings will only ever be the settings that are obtained
     * from instances of {@link OWLEntityCrudKit} created by this factory.
     * @return An {@link OWLEntityCrudKit} that has been setup with the specified settings.
     */
    public abstract OWLEntityCrudKit createOWLEntityEditorKit(OWLEntityManagerSettings settings);



    public abstract OWLEntityManagerSettings getDefaultSettings();



}
