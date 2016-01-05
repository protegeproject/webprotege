package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.DeclarationParser;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class ProjectViewPlace extends Place {

    private ProjectId projectId;

    private Optional<TabName> tabId;

    private Optional<OWLEntity> entity;

    public ProjectViewPlace(ProjectId projectId, Optional<TabName> tabId, Optional<OWLEntity> entity) {
        this.projectId = projectId;
        this.tabId = tabId;
        this.entity = entity;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Optional<TabName> getTabId() {
        return tabId;
    }

    public Optional<OWLEntity> getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectViewPlace)) {
            return false;
        }
        ProjectViewPlace other = (ProjectViewPlace) obj;
        return this.projectId.equals(other.projectId) && entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectViewPlace")
                .addValue(projectId)
                .addValue(tabId)
                .addValue(entity)
                .toString();
    }

    @Prefix("Edit")
    public static class Tokenizer implements PlaceTokenizer<ProjectViewPlace> {

        private static final String PROJECT_ID_VAR = "projectid=";

        private static final String TAB_VAR = "tab=";

        private static final String ENTITY_VAR = "entity=";

        @Override
        public ProjectViewPlace getPlace(String token) {
            String [] components = token.split("\\&");
            Optional<ProjectId> projectId = Optional.absent();
            Optional<TabName> tabId = Optional.absent();
            Optional<OWLEntity> entity = Optional.absent();
            for(String component : components) {
                String lowerCaseComponent = component.toLowerCase();
                if(lowerCaseComponent.startsWith(PROJECT_ID_VAR)) {
                    projectId = Optional.of(ProjectId.get(component.substring(PROJECT_ID_VAR.length())));
                }
                else if(lowerCaseComponent.startsWith(TAB_VAR)) {
                    tabId = Optional.of(new TabName(component.substring(TAB_VAR.length())));
                }
                else if(lowerCaseComponent.startsWith(ENTITY_VAR)) {
                    entity = new DeclarationParser(DataFactory.get()).parseEntity(component.substring(ENTITY_VAR.length()));
                }
            }
            return new ProjectViewPlace(projectId.get(), tabId, entity);
        }

        @Override
        public String getToken(ProjectViewPlace place) {
            StringBuilder sb = new StringBuilder();
            sb.append(PROJECT_ID_VAR);
            sb.append(place.getProjectId().getId());
            Optional<TabName> tabId = place.getTabId();
            if(tabId.isPresent()) {
                sb.append("&");
                sb.append(TAB_VAR);
                sb.append(tabId.get().getTabName());
            }
            Optional<OWLEntity> entity = place.getEntity();
            if(entity.isPresent()) {
                sb.append("&");
                sb.append(entity.get().getEntityType().getName());
                sb.append("(<");
                sb.append(entity.get().getIRI().toString());
                sb.append(">)");
            }
            return sb.toString();
        }
    }




}
