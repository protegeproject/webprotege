package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSetting;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.PROJECT_CHANGED;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2018
 */
public class WebhookSettingsViewImpl extends Composite implements WebhookSettingsView {

    interface WebhookSettingsViewImplUiBinder extends UiBinder<HTMLPanel, WebhookSettingsViewImpl> {

    }

    private static WebhookSettingsViewImplUiBinder ourUiBinder = GWT.create(WebhookSettingsViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<String> webhooks;

    @Inject
    public WebhookSettingsViewImpl() {
        webhooks = new ValueListFlexEditorImpl<>(WebhookViewImpl::new);
        webhooks.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWebhookUrls(@Nonnull List<WebhookSetting> webhookSettings) {
        webhooks.setValue(webhookSettings.stream()
                                         .map(WebhookSetting::getPayloadUrl)
                                         .collect(toList()));
    }

    @Nonnull
    @Override
    public List<WebhookSetting> getWebhookUrls() {
        return webhooks.getValue().map(whs -> whs.stream().map(wh -> WebhookSetting.get(wh,
                                                                                 Sets.newHashSet(
                                                                                         PROJECT_CHANGED,
                                                                                         COMMENT_POSTED
                                                                                 ))).collect(toList())).orElse(Collections.emptyList());
    }
}