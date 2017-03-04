package edu.stanford.bmir.protege.web.client.library.richtext;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public interface RichTextEditorView extends HasText, IsWidget {

    FontStyle getFontStyle();

    void toggleFontStyle();

    FontWeight getFontWeight();

    void toggleFontWeight();

    TextDecoration getTextDecoration();

    void toggleTextDecoration();

    void insertUnorderedList();

    void insertOrderedList();

    void createLink(String linkURL);


    void setFormattingChangedHandler(FormattingChangedHandler handler);

    Widget getWidget();


}
