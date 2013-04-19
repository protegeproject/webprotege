package edu.stanford.bmir.protege.web.client.ui.library.richtext;

import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxDialog;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class RichTextEditorPresenter {

    private RichTextToolbar toolbar;

    private RichTextEditorView editorView;

    public RichTextEditorPresenter(final RichTextToolbar toolbar, final RichTextEditorView editorView) {
        this.toolbar = toolbar;
        this.editorView = editorView;

        editorView.setFormattingChangedHandler(new FormattingChangedHandler() {
            @Override
            public void handleFormattingChanged() {
                updateToolbarWithFormatting();
            }
        });

        toolbar.setRichTextToolbarHandler(new RichTextToolbarHandler() {
            @Override
            public void handleToggleFontWeight() {
                editorView.toggleFontWeight();
            }

            @Override
            public void handleToggleFontStyle() {
                editorView.toggleFontStyle();
            }

            @Override
            public void handleToggleTextDecoration() {
                editorView.toggleTextDecoration();
            }

            @Override
            public void handleInsertUnorderedList() {
                editorView.insertUnorderedList();
            }

            @Override
            public void handleInsertOrderedList() {
                editorView.insertOrderedList();
            }

            @Override
            public void handleCreateLink() {
                InputBoxDialog.showDialog("Enter link URL", new InputBoxHandler() {
                    @Override
                    public void handleAcceptInput(String input) {
                        editorView.createLink(input);
                    }
                });
            }
        });

    }

    private void updateToolbarWithFormatting() {
        toolbar.setFontStyle(editorView.getFontStyle());
        toolbar.setFontWeight(editorView.getFontWeight());
        toolbar.setTextDecoration(editorView.getTextDecoration());

    }



}
