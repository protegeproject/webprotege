package edu.stanford.bmir.protege.web.client.collection;

import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class AddCollectionItemPromptImpl implements AddCollectionItemPrompt {

    @Nonnull
    private final InputBox inputBox;

    @Inject
    public AddCollectionItemPromptImpl(@Nonnull InputBox inputBox) {
        this.inputBox = checkNotNull(inputBox);
    }

    @Override
    public void showPrompt(@Nonnull Consumer<String> callback) {
        inputBox.showDialog("Add element",
                            callback::accept);
    }
}
