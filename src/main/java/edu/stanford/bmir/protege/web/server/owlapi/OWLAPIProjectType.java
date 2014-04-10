package edu.stanford.bmir.protege.web.server.owlapi;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/05/2012
 */
public final class OWLAPIProjectType {


    private static final OWLAPIProjectType DEFAULT_PROJECT_TYPE = new OWLAPIProjectType("OWL Project");

    private static final OWLAPIProjectType OBO_PROJECT_TYPE = new OWLAPIProjectType("OBO Project");

    private String projectTypeName;

    private OWLAPIProjectType() {
    }

    public static OWLAPIProjectType getDefaultProjectType() {
        return DEFAULT_PROJECT_TYPE;
    }
    
    public static OWLAPIProjectType getOBOProjectType() {
        return OBO_PROJECT_TYPE;
    }

    public static OWLAPIProjectType getProjectType(String typeName) {
        if(typeName.equals(OBO_PROJECT_TYPE.getProjectTypeName())) {
            return OBO_PROJECT_TYPE;
        }
        else {
            return DEFAULT_PROJECT_TYPE;
        }
    }

    public OWLAPIProjectType(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }


    @Override
    public int hashCode() {
        return projectTypeName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLAPIProjectType)) {
            return false;
        }
        return ((OWLAPIProjectType) obj).projectTypeName.equals(this.projectTypeName);
    }
}
