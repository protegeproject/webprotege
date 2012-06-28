package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Collection;

import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NoteInputPanel;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.AbstractFieldWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceTextFieldWidget;

public class ICDTitleWidget extends InstanceTextFieldWidget {

    public ICDTitleWidget(Project project) {
        super(project);
    }

    @Override
    protected void deletePropertyValue(final EntityData subject, String propName, ValueType propValueType,
            EntityData oldEntityData, Object oldDisplayedValue, String operationDescription) {
        MessageBox.alert("Change Title Error",
                "The title of an ICD entity cannot be empty<BR>Please specifiy a valid title.",
                new AlertCallback() {
                    public void execute() {
                        if (subject.equals(getSubject())) {
                            displayValues();
                        }
                    }
                });
    }

    @Override
    protected void replacePropertyValue(final EntityData subject, final String propName, final ValueType propValueType,
            final EntityData oldEntityData, final EntityData newEntityData, final Object oldDisplayedValue, final String operationDescription) {
        final EntityData oldInstanceEntityData = findInstanceForValue(oldEntityData);

        MessageBox.confirm("Change Title Warning",
            "<DIV style=\"text-align: center\">" +
            "<SPAN style=\"color:BLACK\"><b>WARNING! You should change the title of a category " +
            "only if you are NOT CHANGING the original meaning of the title " +
            "(for example if there is a typo in the existing title or there is " +
            "a better or more commonly accepted name for this category).</b><BR></SPAN><BR>" +
            "To create or retire a category, or to reorganize the category hierarchy, " +
            "please use the <b>\"Manage Hierachy\"</b> tab!<BR><BR>" +
            "Does the modified title preserve the old meaning of the category?</DIV>",
            new MessageBox.ConfirmCallback() {
                public void execute(String btnID) {
                    if (btnID.equalsIgnoreCase("Yes")) {
                        if (oldInstanceEntityData != null) {
                            propertyValueUtil.replacePropertyValue(getProject().getProjectName(), oldInstanceEntityData.getName(),
                                    getDisplayProperty(), null, oldEntityData.getName(), newEntityData.getName(),
                                    GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                                    new ReplaceInstancePropertyValueHandler(subject, oldInstanceEntityData,
                                            oldEntityData, newEntityData, getValues()));
                            requestComment(oldInstanceEntityData);
                        }
                    }
                    else {
                        if (subject.equals(getSubject())) {
                            displayValues();
                        }
                    }
                }
            });
    }

    private void requestComment(EntityData entity) {
        Collection<EntityData> values = getValues();
        String annotEntityName = null;
        if (values.size() > 0) {
            annotEntityName = values.iterator().next().getName();
        }

        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window() {
            @Override
            public void close() {
                ICDTitleWidget.this.refresh();
                super.close();
            }
        };
        window.setTitle("Reason for changing the title");
        window.setWidth(600);
        window.setHeight(400);//(480);
        window.setMinWidth(300);
        window.setMinHeight(350);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);

        //window.setCloseAction(Window.HIDE);
        window.setPlain(true);

        NoteInputPanel nip = new NoteInputPanel(getProject(), "Please enter a reason for your change:", false,
                new EntityData(annotEntityName), window);
        nip.setSubject("[Reason for title change] ");
        nip.setNoteType("Explanation");
        window.add(nip);
        window.show();
    }


    @Override
    protected Anchor createDeleteHyperlink() {
        Anchor deleteLink = new Anchor("&nbsp<img src=\"images/delete_grey.png\" " + AbstractFieldWidget.DELETE_ICON_STYLE_STRING + "></img>", true);
        deleteLink.setWidth("22px");
        deleteLink.setHeight("22px");
        deleteLink.setTitle("Delete title value is not allowed");
        return deleteLink;
    }
}
