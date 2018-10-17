package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import elemental.client.Browser;
import elemental.dom.Element;
import elemental.util.Indexable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Mar 2018
 */
public class ElementalUtil {

    /**
     * Determines if the specified widget is active.
     * @param widget The widget.
     * @return true of the widget or a descendant of the widget is active, otherwise false.
     */
    public static boolean isWidgetOrDescendantWidgetActive(@Nonnull IsWidget widget) {
        Element activeElement = Browser.getDocument().getActiveElement();
        Element widgetElement = widget.asWidget().getElement().cast();
        while(activeElement != null) {
            if(activeElement.equals(widgetElement)) {
                return true;
            }
            activeElement = activeElement.getParentElement();
        }
        return false;
    }

    public static  <T> Stream<T> streamOf(@Nonnull Indexable indexable) {
        return StreamSupport.stream(new IndexableIterable<T>(indexable).spliterator(), false);
    }


    private static class IndexableIterable<T> implements Iterable<T> {

        private final Indexable indexable;

        public IndexableIterable(@Nonnull Indexable indexable) {
            this.indexable = checkNotNull(indexable);
        }

        @Nonnull
        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {

                private int index;

                @Override
                public boolean hasNext() {
                    return index < indexable.length() - 1;
                }

                @SuppressWarnings("unchecked")
                @Override
                public T next() {
                    T t = (T) indexable.at(index);
                    index++;
                    return t;
                }
            };
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEach(Consumer<? super T> action) {
            for(int i = 0; i < indexable.length(); i++) {
                action.accept((T) indexable.at(i));
            }
        }

        @Override
        public Spliterator<T> spliterator() {
            return Spliterators.spliterator(iterator(), indexable.length(), Spliterator.SIZED | Spliterator.ORDERED);
        }
    }
}
