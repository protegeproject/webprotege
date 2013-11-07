package edu.stanford.bmir.protege.web.client.ui.obo;

import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public interface OBOTermSynonymEditor extends ValueEditor<OBOTermSynonym> {

//    private TextBox synonymField = new TextBox();
//
//
//
//    private DropDown<OBOTermSynonymScope> synonymScopeDropDown = new DropDown<OBOTermSynonymScope>(new DropDownModel<OBOTermSynonymScope>() {
//        public int getSize() {
//            return OBOTermSynonymScope.values().length;
//        }
//
//        public OBOTermSynonymScope getItemAt(int index) {
//            return OBOTermSynonymScope.values()[index];
//        }
//
//        public String getRendering(int index) {
//            return OBOTermSynonymScope.values()[index].getDisplayText();
//        }
//    });
//
//
//    private XRefListEditor xrefListEditor = new XRefListEditor();
//
//    private boolean dirty = false;
//
//    public OBOTermSynonymEditor() {
//        setValue(new OBOTermSynonym());
//        synonymField.getElement().setAttribute("placeholder", "Enter synonym name");
//        synonymField.addChangeHandler(new ChangeHandler() {
//            public void onChange(ChangeEvent event) {
//                dirty = true;
//            }
//        });
//        synonymScopeDropDown.addValueChangeHandler(new ValueChangeHandler<OBOTermSynonymScope>() {
//            public void onValueChange(ValueChangeEvent<OBOTermSynonymScope> oboTermSynonymScopeValueChangeEvent) {
//                dirty = true;
//            }
//        });
//    }
//
//    public void setValue(OBOTermSynonym synonym) {
//        xrefListEditor.setValues(synonym.getXRefs());
//        synonymField.setValue(synonym.getName());
//        synonymScopeDropDown.setSelectedItem(synonym.getScope());
//        dirty = false;
//    }
//
//    public OBOTermSynonym getValue() {
//        return new OBOTermSynonym(xrefListEditor.getValues(), getSynonymName(), getSynonymScope());
//    }
//
//    private OBOTermSynonymScope getSynonymScope() {
//        return synonymScopeDropDown.getSelectedItem();
//    }
//
//    private String getSynonymName() {
//        return synonymField.getText().trim();
//    }
//
//    public boolean isDirty() {
//        return dirty || xrefListEditor.isDirty();
//    }
//
//    public int getEditorCount() {
//        return 2;
//    }
//
//    public String getLabel(int index) {
//        if (index == 0) {
//            return "Synonym";
//        }
//        else {
//            return "Type";
//        }
//
//    }
//
//    public Widget getEditorWidget(int index) {
//        if (index == 0) {
//            return synonymField;
//        }
//        else {
//            return synonymScopeDropDown;
//        }
//    }
//
//    public boolean hasXRefs() {
//        return true;
//    }
//
//    public XRefListEditor getXRefListEditor() {
//        return xrefListEditor;
//    }

}
