package edu.stanford.bmir.protege.web.server.owlapi;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2012
 */
public class OWLAPIProjectConfiguration {

    private static final String OWL_ENTITY_EDITOR_KIT_FACTORY_CLASS_KEY = "project.entity-editor-kit";

    private static final String PROJECT_TYPE_NAME_KEY = "project.type-name";


    private OWLAPIEntityEditorKitFactory entityEditorKitFactory;
    
    private OWLAPIProjectType projectType;


    public OWLAPIProjectConfiguration(OWLAPIProjectAttributes attributes) {
        entityEditorKitFactory = createOWLEntityEditorKitFactory(attributes);
        projectType = getProjectType(attributes);
    }

    public OWLAPIProjectConfiguration(OWLAPIEntityEditorKitFactory entityEditorKitFactory, OWLAPIProjectType projectType) {
        this.entityEditorKitFactory = entityEditorKitFactory;
        this.projectType = projectType;
    }

    private OWLAPIProjectType getProjectType(OWLAPIProjectAttributes attributes) {
        String defaultTypeName = OWLAPIProjectType.getDefaultProjectType().getProjectTypeName();
        return new OWLAPIProjectType(attributes.getStringAttribute(PROJECT_TYPE_NAME_KEY, defaultTypeName));
    }

    public OWLAPIProjectAttributes getProjectAttributes() {
        OWLAPIProjectAttributes projectAttributes = new OWLAPIProjectAttributes();
        projectAttributes.setStringAttribute(OWL_ENTITY_EDITOR_KIT_FACTORY_CLASS_KEY, entityEditorKitFactory.getClass().getName());
        projectAttributes.setStringAttribute(PROJECT_TYPE_NAME_KEY, projectType.getProjectTypeName());
        return projectAttributes;
    }


    @SuppressWarnings("unchecked")
    private OWLAPIEntityEditorKitFactory createOWLEntityEditorKitFactory(OWLAPIProjectAttributes attributes) {
        try {
            String clsName = attributes.getStringAttribute(OWL_ENTITY_EDITOR_KIT_FACTORY_CLASS_KEY, DefaultEntityEditorKitFactory.class.getName());
            Class<OWLAPIEntityEditorKitFactory> cls = (Class<OWLAPIEntityEditorKitFactory>) Class.forName(clsName);
            return cls.newInstance();

        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public OWLAPIEntityEditorKitFactory getOWLEntityEditorKitFactory() {
        return entityEditorKitFactory;
    }

    public OWLAPIProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(OWLAPIProjectType projectType) {
        this.projectType = projectType;
    }
}
