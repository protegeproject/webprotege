package edu.stanford.bmir.protege.web.client.place;

import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public class CollectionElementIdItem extends Item<CollectionElementId> {

    private static final Type<CollectionElementId> TYPE = new Type<>("Element");

    public CollectionElementIdItem(CollectionElementId item) {
        super(item);
    }

    @Override
    public Type<CollectionElementId> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String getItemRendering() {
        return getItem().getId();
    }
}
