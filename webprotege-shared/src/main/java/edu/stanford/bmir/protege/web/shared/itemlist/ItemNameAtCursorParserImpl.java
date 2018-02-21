package edu.stanford.bmir.protege.web.shared.itemlist;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public class ItemNameAtCursorParserImpl implements ItemNameAtCursorParser {

    @Override
    public String parseItemNameAtCursor(String itemNames, int cursorPosition) {
        if(cursorPosition < 0) {
            throw new IllegalArgumentException("cursor position must not be less than zero");
        }
        if(cursorPosition > itemNames.length()) {
            throw new IllegalArgumentException("cursor position must not be larger than string length");
        }
        if(itemNames.isEmpty()) {
            return "";
        }
        int lastIndexOfNewLineBeforeCursor = itemNames.lastIndexOf('\n', cursorPosition - 1);
        return itemNames.substring(lastIndexOfNewLineBeforeCursor + 1, cursorPosition);
    }
}
