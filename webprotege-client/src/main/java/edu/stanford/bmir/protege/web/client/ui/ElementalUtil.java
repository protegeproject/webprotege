package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import elemental.client.Browser;
import elemental.dom.Element;
import elemental.dom.Node;
import elemental.dom.NodeList;
import elemental.events.EventTarget;
import elemental.html.HTMLCollection;
import elemental.util.Indexable;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Mar 2018
 */
public class ElementalUtil {

    /**
     * Determines if the specified widget is active.
     *
     * @param widget The widget.
     * @return true of the widget or a descendant of the widget is active, otherwise false.
     */
    public static boolean isWidgetOrDescendantWidgetActive(@Nonnull IsWidget widget) {
        Element activeElement = Browser.getDocument().getActiveElement();
        Element widgetElement = widget.asWidget().getElement().cast();
        while (activeElement != null) {
            if (activeElement.equals(widgetElement)) {
                return true;
            }
            activeElement = activeElement.getParentElement();
        }
        return false;
    }

    public static Stream<Element> elementsByTagName(@Nonnull Element element,
                                                    @Nonnull String tagName) {
        NodeList elementsByTagName = element.getElementsByTagName(tagName);
        return getElementStream(elementsByTagName);
    }

    private static Stream<Element> getElementStream(NodeList nodeList) {
        return ElementalUtil.streamOf(nodeList)
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node);
    }

    public static <T> Stream<T> streamOf(@Nonnull Indexable indexable) {
        Stream.Builder<T> builder = Stream.builder();
        for (int i = 0; i < indexable.length(); i++) {
            builder.add((T) indexable.at(i));
        }
        return builder.build();
    }

    public static Stream<Element> childSvgGroupElements(@Nonnull Element element) {
        return childElementsByTagName(element, "g");
    }

    public static Stream<Element> childElementsByTagName(@Nonnull Element element,
                                                         @Nonnull String tagName) {
        return getElementStream(element.getChildren())
                .filter(e -> e.getTagName().equals(tagName));
    }

    private static Stream<Element> getElementStream(HTMLCollection collection) {
        return ElementalUtil.streamOf(collection)
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node);
    }

    public static Element firstChildGroupElement(@Nonnull Element element) {
        return childElementsByTagName(element, "g")
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected svg group element"));
    }

    public static Element firstChildElementByTagName(Element element, String tagName) {
        return childElementsByTagName(element, tagName)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected " + tagName + " element"));
    }

    public static Optional<Element> nthChildGroupElement(Element element, int n) {
        return childElementsByTagName(element, "g").skip(n).findFirst();
    }


    public static Element nthChildGroupElementOrError(Element element, int n, String errorMsg) {
        return childElementsByTagName(element, "g")
                .skip(n)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(errorMsg));
    }

    public static void addClassNames(@Nonnull Element element, String... classNames) {
        String concat = Stream.of(classNames).collect(Collectors.joining(" "));
        addClassName(element, concat);
    }

    public static void addClassName(@Nonnull Element element, String className) {
        String current = element.getAttribute("class");
        if (current == null) {
            element.setAttribute("class", className);
        }
        else if (!current.contains(className)) {
            String replacement = current.trim() + " " + className;
            element.setAttribute("class", replacement);
        }
    }

    public static void removeClassName(@Nonnull Element element, String className) {
        String current = element.getAttribute("class");
        if (current == null) {
            return;
        }
        String replacement = current.replace(className, "").trim();
        element.setAttribute("class", replacement);
    }

    public static Optional<String> getAncestorAttribute(@Nonnull EventTarget eventTarget,
                                                        @Nonnull String attribute) {
        if (eventTarget instanceof Node) {
            return getAncestorAttribute((Node) eventTarget, attribute);
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<String> getAncestorAttribute(@Nonnull Node node,
                                                        String attribute) {
        Element parent;
        if (node instanceof Element) {
            parent = (Element) node;
        }
        else {
            parent = node.getParentElement();
        }
        while (parent != null) {
            String attributeValue = parent.getAttribute(attribute);
            if (attributeValue != null) {
                return Optional.of(attributeValue);
            }
            parent = parent.getParentElement();
        }
        return Optional.empty();
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
            for (int i = 0; i < indexable.length(); i++) {
                action.accept((T) indexable.at(i));
            }
        }

        @Override
        public Spliterator<T> spliterator() {
            return Spliterators.spliterator(iterator(), indexable.length(), Spliterator.SIZED | Spliterator.ORDERED);
        }
    }
}
