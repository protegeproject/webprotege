package edu.stanford.bmir.protege.web.client.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.shared.form.DeprecateEntityByFormResult;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
public class DeprecateEntityAsyncCallback extends DispatchServiceCallbackWithProgressDisplay<DeprecateEntityByFormResult> {

    @Nonnull
    private final String entityRendering;

    @AutoFactory
    public DeprecateEntityAsyncCallback(@Provided DispatchErrorMessageDisplay errorMessageDisplay,
                                        @Provided ProgressDisplay progressDisplay,
                                        @Nonnull String entityRendering) {
        super(errorMessageDisplay, progressDisplay);
        this.entityRendering = checkNotNull(entityRendering);
    }

    @Override
    public String getProgressDisplayTitle() {
        return "Deprecating " + entityRendering;
    }

    @Override
    public String getProgressDisplayMessage() {
        return "Deprecating " + entityRendering + ". Please wait.";
    }
}
