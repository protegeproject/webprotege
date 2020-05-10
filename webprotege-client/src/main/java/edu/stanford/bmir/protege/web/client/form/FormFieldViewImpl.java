package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.CaseFormat;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeViewResources;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormFieldViewImpl extends Composite implements FormFieldView {

    private Optional<Tooltip> helpTooltip = Optional.empty();

    private HeaderClickedHandler headerClickedHandler = () -> {};

    interface FormFieldViewImplUiBinder extends UiBinder<HTMLPanel, FormFieldViewImpl> {

    }

    private static FormFieldViewImplUiBinder ourUiBinder = GWT.create(FormFieldViewImplUiBinder.class);

    private FormFieldId formFieldId = null;

    private Optionality required = Optionality.OPTIONAL;

    @UiField
    Label label;

    @UiField
    SimplePanel editorHolder;

    @UiField
    HTMLPanel helpIcon;

    @UiField
    Image expansionHandle;

    @UiField
    HTMLPanel fieldHeader;

    @UiField
    HTMLPanel content;

    FormControlStackPresenter editor;

    @Inject
    public FormFieldViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        fieldHeader.sinkEvents(Event.MOUSEEVENTS);
        fieldHeader.addDomHandler(event -> {
            headerClickedHandler.handleHeaderClicked();
        }, ClickEvent.getType());
    }

    @Override
    public void setId(FormFieldId elementId) {
        formFieldId = elementId;
    }

    @Override
    public void setRequired(Optionality required) {
        this.required = checkNotNull(required);
    }

    @Override
    public Optionality getRequired() {
        return required;
    }

    @Override
    public void setRequiredValueNotPresentVisible(boolean visible) {
        if(visible) {
            label.addStyleName(WebProtegeClientBundle.BUNDLE.style().formLabelError());
            editorHolder.addStyleName(WebProtegeClientBundle.BUNDLE.style().formEditorError());
        }
        else {
            label.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formLabelError());
            editorHolder.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formEditorError());
        }
    }

    @Override
    public Optional<FormFieldId> getId() {
        return Optional.ofNullable(formFieldId);
    }

    @Override
    public void setFormLabel(String formLabel) {
        label.setText(formLabel);
        label.setVisible(!formLabel.isEmpty());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormStackContainer() {
        return editorHolder;
    }

    @Override
    public FormControlStackPresenter getEditor() {
        return checkNotNull(editor);
    }

    @Override
    public void addStylePropertyValue(String cssProperty, String cssValue) {
        String camelCaseProperty = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, cssProperty);
        String camelCaseValue = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, cssValue);
        getElement().getStyle().setProperty(camelCaseProperty, camelCaseValue);
    }

    @Override
    public void collapse() {
        content.setVisible(false);
        expansionHandle.setUrl(TreeNodeViewResources.RESOURCES.collapsed().getSafeUri());
    }

    @Override
    public void expand() {
        expansionHandle.setUrl(TreeNodeViewResources.RESOURCES.expanded().getSafeUri());
        content.setVisible(true);
    }

    @Override
    public void setHeaderClickedHandler(@Nonnull HeaderClickedHandler headerClickedHandler) {
        this.headerClickedHandler = checkNotNull(headerClickedHandler);
    }

    @Override
    public void setHelpText(@Nonnull String helpText) {
        if(!helpTooltip.isPresent()) {
            Tooltip helpTooltip = Tooltip.createOnBottom(helpIcon, helpText);
            this.helpTooltip = Optional.of(helpTooltip);
        }
        this.helpTooltip.ifPresent(tt -> tt.updateTitleContent(helpText));
        helpIcon.setVisible(!helpText.trim().isEmpty());
    }

    @Override
    public void clearHelpText() {
        helpIcon.setTitle("");
        helpIcon.setVisible(false);
    }
}
