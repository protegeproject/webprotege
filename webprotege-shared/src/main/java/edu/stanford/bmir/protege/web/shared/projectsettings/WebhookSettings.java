package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class WebhookSettings implements IsSerializable {

    @JsonProperty("webhookSettings")
    @Nonnull
    public abstract ImmutableList<WebhookSetting> getWebhookSettings();

    @Nonnull
    @JsonCreator
    public static WebhookSettings get(@Nonnull @JsonProperty("webhookSettings") List<WebhookSetting> webhookSettings) {
        return new AutoValue_WebhookSettings(ImmutableList.copyOf(webhookSettings));
    }
}
