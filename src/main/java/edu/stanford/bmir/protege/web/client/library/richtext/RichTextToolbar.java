package edu.stanford.bmir.protege.web.client.library.richtext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface RichTextToolbar {

    void setRichTextToolbarHandler(RichTextToolbarHandler handler);

    void setTextDecoration(TextDecoration textDecoration);

    void setFontWeight(FontWeight fontWeight);

    void setFontStyle(FontStyle fontStyle);

}
