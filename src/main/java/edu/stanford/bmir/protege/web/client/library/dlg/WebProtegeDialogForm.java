package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class WebProtegeDialogForm extends WebProtegePanel implements HasInitialFocusable {

    /**
     * The column that holds the widgets.
     */
    public static final int LABEL_COLUMN = 0;

    /**
     * The column that holds the labels
     */
    public static final int WIDGET_COLUMN = 1;

    public static final String LABEL_SUFFIX = ":";

    public static final String WEB_PROTEGE_VERTICAL_SPACER_STYLE_NAME = "web-protege-vertical-spacer";

    public static final String PLACEHOLDER_TEXT_ELEMENT_ATTRIBUTE_NAME = "placeholder";

    public static final int DEFAULT_TEXT_BOX_VISIBILE_LENGTH = 60;


    private final FlexTable formTable = new FlexTable();

    private final FormPanel formPanel;

    private Focusable initialFocusable;


    private String lastRadioButtonLabel = "";
    
    
    private List<WebProtegeDialogValidator> validators = new ArrayList<WebProtegeDialogValidator>();
    

    private List<WebProtegeDialogTextFieldValidationManager> inlineValidators = new ArrayList<WebProtegeDialogTextFieldValidationManager>();

    public WebProtegeDialogForm() {
        formPanel = new FormPanel();
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        SimplePanel formTableWrapper = new SimplePanel();
        formPanel.setWidget(formTableWrapper);
        formTableWrapper.setWidget(formTable);
        add(formPanel);
        formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
            }
        });
        addDialogValidator(new WebProtegeDialogValidator() {
            public ValidationState getValidationState() {
                ValidationState result = ValidationState.VALID;
                for(WebProtegeDialogTextFieldValidationManager manager : inlineValidators) {
                    ValidationState curState = manager.runValidation().getState();
                    if(curState == ValidationState.INVALID) {
                        result = curState;
                    }
                }
                return result;
            }

            public String getValidationMessage() {
                return "Please correct the highlighted errors on the form";
            }
        });
    }

    public void addDialogValidator(WebProtegeDialogValidator validator) {
        validators.add(validator);
    }

    public List<WebProtegeDialogValidator> getDialogValidators() {
        return new ArrayList<WebProtegeDialogValidator>(validators);
    }

    public void submit() {
        formPanel.submit();
    }

    public void addSubmitCompleteHandler(FormPanel.SubmitCompleteHandler handler) {
        formPanel.addSubmitCompleteHandler(handler);
    }
    
    public void addSubmitHandler(FormPanel.SubmitHandler submitHandler) {
        formPanel.addSubmitHandler(submitHandler);
    }
    
    public void setPostURL(String url) {
        formPanel.setAction(url);
    }
    
    public void setMultipartEncoding(boolean b) {
        if(b) {
            formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        }
        else {
            formPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
        }
    }

    /**
     * Adds a textbox to the form.
     * @param label The label for the text box. Not <code>null</code>.
     * @param placeholderText The placeholder text. May be <code>null</code>, or use the empty string for no placeholder.
     * @param initialValue The initial value for the TextBox.  May be <code>null</code>, or use the empty string for no
     * initial value.
     * @return The TextBox that was added to the form.
     */
    public TextBox addTextBox(String label, String placeholderText, String initialValue, WebProtegeDialogInlineValidator<ValueBoxBase<String>> validator) {
        TextBox textBox = new TextBox();
        return addTextBox(textBox, label, placeholderText, initialValue, validator);
    }

    public TextBox addTextBox(final TextBox textBox, String label, String placeholderText, String initialValue, final WebProtegeDialogInlineValidator validator) {
        setupTextBox(textBox, placeholderText, initialValue);
        addWidget(label, textBox);
        if(validator != null) {
            setupInlineValidator(textBox, validator);
        }
        return textBox;
    }

    private void setupInlineValidator(final TextBox textBox, final WebProtegeDialogInlineValidator<ValueBoxBase<String>> validator) {
        final WebProtegeDialogTextFieldValidationManager validatorManager = new WebProtegeDialogTextFieldValidationManager(textBox, validator);
        addWidget("", validatorManager.getErrorWidget());
        inlineValidators.add(validatorManager);
    }

    private void setupTextBox(TextBox textBox, String placeholderText, String initialValue) {
        if(placeholderText != null && !placeholderText.isEmpty()) {
            textBox.getElement().setAttribute(PLACEHOLDER_TEXT_ELEMENT_ATTRIBUTE_NAME, placeholderText);
            textBox.setTitle(placeholderText);
        }
        if(initialValue != null && !initialValue.isEmpty()) {
            textBox.setText(initialValue);
        }
        textBox.setVisibleLength(DEFAULT_TEXT_BOX_VISIBILE_LENGTH);
    }

    public InlineValidationResult runTextFieldValidation() {
        for(WebProtegeDialogTextFieldValidationManager manager : inlineValidators) {
            InlineValidationResult result = manager.runValidation();
            if(result.isInvalid()) {
                return result;
            }
        }
        return InlineValidationResult.getValid();
    }

    public SuggestBox addSuggestBox(String label, String placeholderText, String initialValue, SuggestOracle suggestOracle, WebProtegeDialogInlineValidator validator) {
        TextBox textBox = new TextBox();
        setupTextBox(textBox, placeholderText, initialValue);

        return new SuggestBox(suggestOracle, textBox);

    }


    /**
     * Adds a {@link PasswordTextBox} to the form.
     * @param label The label for the text box. Not <code>null</code>.
     * @param placeholderText The placeholder text.  May be <code>null</code>, or use the empty string for no placeholder.
     * @param initialValue The initial value for the PasswordTextBox.  May be <code>null</code>, or use the empty string
     * for no initial value.
     * @return The PasswordTextBox that was added to the form.
     */
    public PasswordTextBox addPasswordTextBox(String label, String placeholderText, String initialValue) {
        PasswordTextBox passwordTextBox = new PasswordTextBox();
        addTextBox(passwordTextBox, label, placeholderText, initialValue, new NonEmptyWebProtegeDialogTextFieldValidator("Please enter a password"));
        return passwordTextBox;
    }
    
    
    public RadioButton addRadioButton(String label, String groupName, String radioButtonText) {
        RadioButton radioButton = new RadioButton(groupName, radioButtonText);
        if(lastRadioButtonLabel.equals(label)) {
            addWidget("", radioButton);
        }
        else {
            lastRadioButtonLabel = label;
            addWidget(label, radioButton);
        }
        return radioButton;
    }
    
    public RadioButton addRadioButtonAnnotated(String label, String groupname, String radioButtonText, String annotation) {
        RadioButton radioButton = addRadioButton(label, groupname, radioButtonText);
        Widget annotationLabel = new HTML(annotation);
        annotationLabel.addStyleName("web-protege-annotated-radio-button-description-label");
        addWidget("", annotationLabel);
        return radioButton;
    }

    public void addWidget(String label, Widget widget) {
        int insertionRow = formTable.getRowCount();
        if (label != null) {
            String labelText = getLabelText(label);
            if (!label.isEmpty()) {
                formTable.setWidget(insertionRow, LABEL_COLUMN, new WebProtegeDialogLabel(labelText));
            }
        }
        formTable.setWidget(insertionRow, WIDGET_COLUMN, widget);
        formTable.getCellFormatter().setVerticalAlignment(insertionRow, LABEL_COLUMN, HasVerticalAlignment.ALIGN_TOP);
        if(insertionRow == 0 && widget instanceof Focusable) {
            initialFocusable = ((Focusable) widget);
            initialFocusable.setFocus(true);
        }
        formTable.setCellSpacing(5);
    }

    public void addVerticalSpacer() {
        int insertionRow = formTable.getRowCount();
        SimplePanel labelSpacer = createSpacer();
        formTable.setWidget(insertionRow, LABEL_COLUMN, labelSpacer);
    }

    private static SimplePanel createSpacer() {
        SimplePanel spacer = new SimplePanel();
        spacer.addStyleName(WEB_PROTEGE_VERTICAL_SPACER_STYLE_NAME);
        return spacer;
    }

    /**
     * Gets the text for a label on the form.
     * @param label The user requested label.
     * @return The label text with a suffix determined by the {@link #LABEL_SUFFIX} field value.
     */
    private String getLabelText(String label) {
        return label.endsWith(LABEL_SUFFIX) ? label : label + LABEL_SUFFIX;
    }
    
    public java.util.Optional<Focusable> getInitialFocusable() {
        return java.util.Optional.ofNullable(initialFocusable);
    }
}
