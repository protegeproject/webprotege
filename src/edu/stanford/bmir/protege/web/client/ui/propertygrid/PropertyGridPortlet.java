package edu.stanford.bmir.protege.web.client.ui.propertygrid;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Cls;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.WebProtegeIRI;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermEditor;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermEditorView;
import edu.stanford.bmir.protege.web.client.ui.obo.XRefListEditor;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class PropertyGridPortlet extends AbstractEntityPortlet implements EntityPortlet {

    private PropertyGridView propertyGridView;
    
    public PropertyGridPortlet(Project project) {
        super(project);
        addStyleName("web-protege-laf");
    }


    @Override
    public void reload() {
        EntityData entityData = getEntity();
        if(entityData != null) {
            propertyGridView.setSubject(new Cls(new WebProtegeIRI(entityData.getName())));
        }
        else {
            propertyGridView.clearSubject();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Property values");
        if(entityData != null) {
            sb.append(" for ");
            sb.append(entityData.getBrowserText());
        }
        setTitle(sb.toString());
    }

    @Override
    public void initialize() {
        propertyGridView = new PropertyGridView(getProject());
        add(new OBOTermEditorView(new OBOTermEditor() {
            public int getEditorCount() {
                return 1;
            }

            public String getLabel(int index) {
                return "Property values";
            }

            public Widget getEditorWidget(int index) {
                return propertyGridView;
            }

            public boolean hasXRefs() {
                return false;
            }

            public XRefListEditor getXRefListEditor() {
                return null;
            }

            public boolean isDirty() {
                return propertyGridView.isDirty();
            }
        }));
    }

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }

    @Override
    public void setEntity(EntityData newEntity) {
        super.setEntity(newEntity);
    }


}
