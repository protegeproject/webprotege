package edu.stanford.bmir.protege.web.client.usage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.checkbox.AxiomTypeGroupCheckBox;
import edu.stanford.bmir.protege.web.client.ui.library.checkbox.EntityTypeGroupCheckBox;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public class UsageFilterEditorViewImpl extends Composite implements UsageFilterEditorView {

    private final List<AxiomTypeGroupCheckBox> axiomGroupCheckBoxes;

    private final List<EntityTypeGroupCheckBox> entityGroupCheckBoxes;

    interface UsageFilterEditorViewImplUiBinder extends UiBinder<HTMLPanel, UsageFilterEditorViewImpl> {

    }

    private static UsageFilterEditorViewImplUiBinder ourUiBinder = GWT.create(UsageFilterEditorViewImplUiBinder.class);

    @UiField
    protected CheckBox showDefiningAxiomsCheckBox;

    @UiField
    protected Anchor entityTypeSelectAll;

    @UiField
    protected Anchor entityTypeSelectNone;

    @UiField
    protected Anchor axiomTypeSelectAll;

    @UiField
    protected Anchor axiomTypeSelectNone;

    public UsageFilterEditorViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        showDefiningAxiomsCheckBox.setValue(true);
        axiomGroupCheckBoxes = new ArrayList<AxiomTypeGroupCheckBox>();
        getWidgets(rootElement, axiomGroupCheckBoxes);
        selectAllAxiomTypes(true);
        entityGroupCheckBoxes = new ArrayList<EntityTypeGroupCheckBox>();
        getEntityGroupCheckBoxes(rootElement, entityGroupCheckBoxes);
        selectAllEntityTypes(true);
    }

    @UiHandler("entityTypeSelectAll")
    protected void handleSelectAllEntityTypes(ClickEvent clickEvent) {
        selectAllEntityTypes(true);
    }

    @UiHandler("entityTypeSelectNone")
    protected void handleSelectNoneEntityTypes(ClickEvent clickEvent) {
        selectAllEntityTypes(false);
    }


    @UiHandler("axiomTypeSelectAll")
    protected void handleSelectAllAxiomTypes(ClickEvent clickEvent) {
        selectAllAxiomTypes(true);
    }

    @UiHandler("axiomTypeSelectNone")
    protected void handleSelectNoneAxiomTypes(ClickEvent clickEvent) {
        selectAllAxiomTypes(false);
    }


    private void selectAllAxiomTypes(boolean b) {
        for(AxiomTypeGroupCheckBox checkBox : axiomGroupCheckBoxes) {
            checkBox.setValue(b);
        }
    }


    private void selectAllEntityTypes(boolean b) {
        for(EntityTypeGroupCheckBox checkBox : entityGroupCheckBoxes) {
            checkBox.setValue(b);
        }
    }

    private void getEntityGroupCheckBoxes(Widget parent, List<EntityTypeGroupCheckBox> checkBoxes) {
        if(parent instanceof EntityTypeGroupCheckBox) {
            checkBoxes.add((EntityTypeGroupCheckBox) parent);
        }
        if(parent instanceof HasWidgets) {
            for(Widget child : ((HasWidgets) parent)) {
                getEntityGroupCheckBoxes(child, checkBoxes);
            }
        }
    }


    private void getWidgets(Widget parent, List<AxiomTypeGroupCheckBox> axiomTypeGroupCheckBoxes) {
        if(parent instanceof AxiomTypeGroupCheckBox) {
            axiomTypeGroupCheckBoxes.add((AxiomTypeGroupCheckBox) parent);
        }
        if(parent instanceof HasWidgets) {
            for(Widget child : ((HasWidgets) parent)) {
                getWidgets(child, axiomTypeGroupCheckBoxes);
            }
        }
    }

    @Override
    public UsageFilter getUsageFilter() {
        Set<AxiomType<?>> axiomTypes = getAxiomTypes();
        Set<EntityType<?>> entityTypes = getEntityTypes();
        return new UsageFilter(showDefiningAxiomsCheckBox.getValue(), entityTypes, axiomTypes);
    }

    private Set<EntityType<?>> getEntityTypes() {
        Set<EntityType<?>> entityTypes = new HashSet<EntityType<?>>();
        for(EntityTypeGroupCheckBox checkBox : entityGroupCheckBoxes) {
            if (checkBox.getValue()) {
                entityTypes.addAll(checkBox.getEntityTypeGroup().get().getEntityTypes());
            }
        }
        return entityTypes;
    }

    private Set<AxiomType<?>> getAxiomTypes() {
        Set<AxiomType<?>> axiomTypes = new HashSet<AxiomType<?>>();
        for(AxiomTypeGroupCheckBox checkBox : axiomGroupCheckBoxes) {
            if(checkBox.getValue()) {
                axiomTypes.addAll(checkBox.getAxiomTypeGroup().get().getAxiomTypes());
            }
        }
        return axiomTypes;
    }
}