package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptor;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public interface GeneratedValueDescriptorPresenter {

    void start(@Nonnull AcceptsOneWidget container);

    String getName();

    void setValue(GeneratedValueDescriptor valueDescriptor);

    Optional<GeneratedValueDescriptor> getValue();
}
