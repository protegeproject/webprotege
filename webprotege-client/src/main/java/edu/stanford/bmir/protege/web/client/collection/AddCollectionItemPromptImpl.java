package edu.stanford.bmir.protege.web.client.collection;

import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class AddCollectionItemPromptImpl implements AddCollectionItemPrompt {

    @Inject
    public AddCollectionItemPromptImpl() {
    }

    @Override
    public void showPrompt(@Nonnull Consumer<String> callback) {
        InputBox.showDialog("Add element",
                            callback::accept);
    }
}
