package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class CopyFormDescriptorsFromProjectResult implements Result {

    private ImmutableList<FormDescriptor> copiedFormDescriptors;

    public CopyFormDescriptorsFromProjectResult(ImmutableList<FormDescriptor> copiedFormDescriptors) {
        this.copiedFormDescriptors = checkNotNull(copiedFormDescriptors);
    }

    private CopyFormDescriptorsFromProjectResult() {
    }

    @Nonnull
    public ImmutableList<FormDescriptor> getCopiedFormDescriptors() {
        return copiedFormDescriptors;
    }
}
