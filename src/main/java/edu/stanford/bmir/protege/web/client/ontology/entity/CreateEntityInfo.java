package edu.stanford.bmir.protege.web.client.ontology.entity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2013
 */
public class CreateEntityInfo {

    private Set<String> browserTexts;


    public CreateEntityInfo(List<String> browserTexts) {
        this.browserTexts = new LinkedHashSet<String>(browserTexts);
    }

    public Set<String> getBrowserTexts() {
        return new LinkedHashSet<String>(browserTexts);
    }

    @Override
    public int hashCode() {
        return "CreateEntityInfo".hashCode() + browserTexts.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof CreateEntityInfo)) {
            return false;
        }
        CreateEntityInfo other = (CreateEntityInfo) obj;
        return this.browserTexts.equals(other.browserTexts);
    }
}
