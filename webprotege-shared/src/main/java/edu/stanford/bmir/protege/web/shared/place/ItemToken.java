package edu.stanford.bmir.protege.web.shared.place;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class ItemToken {

    private String typeName;

    private String itemContent;

    public ItemToken(String typeName, String itemContent) {
        this.typeName = typeName;
        this.itemContent = itemContent;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getItemContent() {
        return itemContent;
    }
}
