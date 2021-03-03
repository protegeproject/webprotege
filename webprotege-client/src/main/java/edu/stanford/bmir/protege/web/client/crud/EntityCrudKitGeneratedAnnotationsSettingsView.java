package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public interface EntityCrudKitGeneratedAnnotationsSettingsView extends IsWidget {

    void setValues(@Nonnull ImmutableList<GeneratedAnnotationDescriptor> values);

    @Nonnull
    ImmutableList<GeneratedAnnotationDescriptor> getValues();
}
