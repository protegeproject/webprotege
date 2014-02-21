package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.common.WebProtegeFormLabel;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermEditorView extends FlowPanel {

    private List<OBOTermEditorGroup> groupList = new ArrayList<OBOTermEditorGroup>();

//    private FlexTable masterTable = new FlexTable();

    private FlowPanel masterPanel = new FlowPanel();

    public OBOTermEditorView(OBOTermEditor editor) {
        this(Arrays.asList(new OBOTermEditorGroup(Arrays.asList(editor))));
    }
    
    public OBOTermEditorView(OBOTermEditorGroup group) {
        this(Arrays.asList(group));
    }

    public OBOTermEditorView(List<OBOTermEditorGroup> groupList) {
        this.groupList = groupList;
        addStyleName("web-protege-laf");
//        add(masterTable);
        add(masterPanel);
        rebuild();
    }

    public void setSubject(OWLEntity subject) {
        rebuild();
    }


    public void rebuild() {
        masterPanel.clear();

        for (OBOTermEditorGroup group : groupList) {
            // The outer element for a group of editors is a DIV
            FlowPanel editorGroupPanel = new FlowPanel();
            editorGroupPanel.addStyleName("web-protege-form-layout-editor-group");

            // In group
            for (OBOTermEditor termEditor : group.getTermEditors()) {

                FlowPanel termEditorPanel = new FlowPanel();
                termEditorPanel.addStyleName("web-protege-term-editor");
                editorGroupPanel.add(termEditorPanel);
                
                // A list of DIV elements - one per labelled editor
                FlowPanel editorGroupForm = new FlowPanel();
                editorGroupForm.addStyleName("web-protege-form-layout-editor-group-form");
                termEditorPanel.add(editorGroupForm);

                for (int i = 0; i < termEditor.getEditorCount(); i++) {

                    FlowPanel labelledEditorPanel = new FlowPanel();
                    labelledEditorPanel.addStyleName("web-protege-form-layout-labelled-editor");

                    editorGroupForm.add(labelledEditorPanel);

                    WebProtegeFormLabel editorLabel = new WebProtegeFormLabel(termEditor.getLabel(i));
                    editorLabel.addStyleName("web-protege-form-layout-label");
                    labelledEditorPanel.add(editorLabel);
                    FlowPanel editorPanel = new FlowPanel();
                    Widget widget = termEditor.getEditorWidget(i);
                    widget.addStyleName("web-protege-form-layout-editor-input");
                    editorPanel.add(widget);
                    editorPanel.addStyleName("web-protege-form-layout-editor");
                    labelledEditorPanel.add(editorPanel);
                }

                if(termEditor.hasXRefs()) {
                    FlowPanel sidePanel = new FlowPanel();
                    sidePanel.addStyleName("web-protege-form-layout-editor-group-sidebar");
                    termEditorPanel.add(sidePanel);
                    sidePanel.add(termEditor.getXRefListEditor());
                }
            }
            masterPanel.add(editorGroupPanel);
        }
    }


}
