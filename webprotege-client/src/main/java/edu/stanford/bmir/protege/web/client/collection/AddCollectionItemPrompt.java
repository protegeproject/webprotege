package edu.stanford.bmir.protege.web.client.collection;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public interface AddCollectionItemPrompt {

    void showPrompt(@Nonnull Consumer<String> callback);
}
