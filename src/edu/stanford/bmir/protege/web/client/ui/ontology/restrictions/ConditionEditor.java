package edu.stanford.bmir.protege.web.client.ui.ontology.restrictions;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ConditionSuggestion;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ConditionEditor extends Panel {

    private Project project;

    private ListBox suggestionsBox;
    private TextArea conditionArea;
    private HTML hintArea;
    private KeyUpHandler keyPressHandler;

    private int previousCursorPosition;
    private boolean isValid;

    public ConditionEditor(Project project) {
        this.project = project;
        buildUI();
    }

    private void buildUI() {
        setLayout(new AnchorLayout());

        suggestionsBox = new ListBox();
        suggestionsBox.setVisibleItemCount(3);
        suggestionsBox.setStylePrimaryName("condition_editor");

        suggestionsBox.addDoubleClickHandler(new DoubleClickHandler() {
            public void onDoubleClick(DoubleClickEvent event) {
                onSuggestionAccepted();
            }
        });
        suggestionsBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                int keyCode = event.getNativeEvent().getKeyCode();
                if (keyCode == KeyCodes.KEY_ENTER) {
                    onSuggestionAccepted();
                }
            }
        });

        conditionArea = new TextArea();
//        conditionArea.setStylePrimaryName("condition_editor");
        conditionArea.setTitle("Press <Tab> or <Ctrl>+<Space> to select from the list of suggestions on the right.");

        hintArea = new HTML();
        hintArea.setStylePrimaryName("condition_editor");
        setHint(true, "");

        Panel horizPanel = new Panel();
        horizPanel.setLayout(new AnchorLayout());
        horizPanel.add(conditionArea, new AnchorLayoutData("70% 100%"));
        horizPanel.add(suggestionsBox, new AnchorLayoutData("30% 100%"));

        Panel verticalPanel = new Panel();
        verticalPanel.setLayout(new AnchorLayout());
        verticalPanel.add(horizPanel, new AnchorLayoutData("100% 90%"));
        verticalPanel.add(hintArea, new AnchorLayoutData("100% 10%"));

        add(verticalPanel, new AnchorLayoutData("100% 100%"));

        keyPressHandler = getKeyPressHandler();
        conditionArea.addKeyUpHandler(keyPressHandler);
        conditionArea.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_TAB) {
                    event.preventDefault();
                }
            }
        });
    }


    public void setConditionText(String text) {
        isValid = true;
        conditionArea.setText(text);
        setHint(true, "");
        suggestionsBox.clear();
    }

    public void requestFocus() {
        conditionArea.setFocus(true);
    }

    protected KeyUpHandler getKeyPressHandler() {
        return new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                onKeyUpInConditionEditor(event);
            }
        };
    }

    //TODO untangle this
    protected void onKeyUpInConditionEditor(KeyUpEvent event) {
        previousCursorPosition = conditionArea.getCursorPos();
        char charCode =(char) event.getNativeKeyCode();
        int keyCode = event.getNativeKeyCode();
        if (((event.isControlKeyDown() && Character.isSpace(charCode)) || keyCode == KeyCodes.KEY_TAB) && suggestionsBox.getItemCount() > 0) {
            suggestionsBox.setSelectedIndex(0);
            suggestionsBox.setFocus(true);
        } else{
            // MH: What's special about the space character?
//            if (!Character.isSpace(charCode)) {
                getSuggestions(conditionArea.getText(), previousCursorPosition);
//            }
        }
    }

    protected void getSuggestions(String text, int cursorPosition) {
        suggestionsBox.clear();
        hintArea.setText("Checking expression...");
        OntologyServiceManager.getInstance().getConditionAutocompleteSuggestions(project.getProjectName(),
                text, cursorPosition, new GetSuggestionsHandler(text));
    }

    protected void setHint(boolean valid, String text) {
        hintArea.setStylePrimaryName(valid ? "hint_color_green" : "hint_color_red");
        if (valid) {
            hintArea.setText("Expression is valid");
        } else {
            if (text == null || text.length() == 0) {
                hintArea.setText("Expression is incomplete");
            } else {
                hintArea.setText(text);
            }
        }
    }

    protected void onSuggestionAccepted() {
        if (suggestionsBox.getSelectedIndex() < 0) {
            return;
        }

        //replace text in condition area
        String previousValue = conditionArea.getText();
        int idStart = UIUtil.getIdentifierStart(previousValue, previousCursorPosition);
        int idEnd = UIUtil.getIdentifierEnd(previousValue, previousCursorPosition);

        String replaceValue = suggestionsBox.getItemText(suggestionsBox.getSelectedIndex());
        String newValue = previousValue.substring(0, idStart) + replaceValue + previousValue.substring(idEnd, previousValue.length());
        if (newValue.length() == idStart + replaceValue.length()) {
            newValue += " ";
        }
        conditionArea.setValue(newValue);
        conditionArea.setFocus(true);
        conditionArea.setCursorPos(idStart + replaceValue.length() + 1);

        //refresh after replace
        previousCursorPosition = conditionArea.getCursorPos() ;
        getSuggestions(conditionArea.getText(), previousCursorPosition);
    }

    public boolean isValid() {
        return isValid;
    }

    public String getCondition() {
        return conditionArea.getText();
    }

    /*
     * Remote calls
     */

    class GetSuggestionsHandler extends AbstractAsyncHandler<ConditionSuggestion> {

        private String originalCondition;

        public GetSuggestionsHandler(String originalCondition) {
            this.originalCondition = originalCondition;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at retrieving autocomplete suggestions for " + conditionArea.getText());
        }

        @Override
        public void handleSuccess(ConditionSuggestion conditionSuggestion) {

            // MH:  TODO: This mixes up autocompletion and error checking.  They should be separate.  I can have a
            // TODO:      valid expression, but if my cursor is in the middle of the expression then an error will be shown.
            suggestionsBox.clear();
            hintArea.setText("");

            if (!originalCondition.equals(conditionArea.getText())) { //suggestions for other text
                return;
            }
            isValid = conditionSuggestion.isValid();
            setHint(conditionSuggestion.isValid(), conditionSuggestion.getMessage());

            List<EntityData> suggestions = conditionSuggestion.getSuggestions();
            if (suggestions != null) {
                for (EntityData suggestion : suggestions) {
                    suggestionsBox.addItem(suggestion.getBrowserText());
                }
            }
        }

    }


}
