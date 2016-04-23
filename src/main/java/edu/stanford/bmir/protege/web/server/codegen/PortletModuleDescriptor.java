package edu.stanford.bmir.protege.web.server.codegen;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public class PortletModuleDescriptor {

    private String moduleFullyQualifiedClassName;

    public PortletModuleDescriptor(String moduleFullyQualifiedClassName) {
        this.moduleFullyQualifiedClassName = moduleFullyQualifiedClassName;
    }

    public String getModuleFullyQualifiedClassName() {
        return moduleFullyQualifiedClassName;
    }

    @Override
    public int hashCode() {
        return "PortletModuleDescriptor".hashCode() + moduleFullyQualifiedClassName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PortletModuleDescriptor)) {
            return false;
        }
        PortletModuleDescriptor other = (PortletModuleDescriptor) o;
        return this.moduleFullyQualifiedClassName.equals(other.moduleFullyQualifiedClassName);
    }
}
