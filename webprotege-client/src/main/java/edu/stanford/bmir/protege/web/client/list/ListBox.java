package edu.stanford.bmir.protege.web.client.list;

import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Aug 2017
 */
public class ListBox<K, E> extends Composite implements HasSelectionHandlers<List<E>>, HasClickHandlers, HasDoubleClickHandlers, HasRequestFocus {

    interface ListBoxUiBinder extends UiBinder<HTMLPanel, ListBox> {

    }

    private static ListBoxUiBinder ourUiBinder = GWT.create(ListBoxUiBinder.class);

    @UiField
    FlowPanel contentHolder;

    @UiField
    FocusPanel focusPanel;

    private SelectionInterval selectionInterval = SelectionInterval.emptySelection();

    private List<E> elements = new ArrayList<>();

    private Function<E, K> keyExtractor = (element) -> null;

    private ListBoxCellRenderer<E> renderer = element -> {
        if(element instanceof HasBrowserText) {
            return new Label(((HasBrowserText) element).getBrowserText());
        }
        else {
            return new Label(element.toString());
        }
    };

    public ListBox() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return focusPanel.addClickHandler(handler);
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return focusPanel.addDoubleClickHandler(handler);
    }

    public void setRenderer(@Nonnull ListBoxCellRenderer<E> renderer) {
        this.renderer = checkNotNull(renderer);
        setListData(elements);
    }

    public void setListData(@Nonnull List<E> elements) {
        contentHolder.clear();
        int row = 0;
        StringBuilder sb = new StringBuilder();
        for(E element : elements) {
            IsWidget rendering = renderElement(row, element);
            contentHolder.add(rendering);
            row++;
        }
        this.elements = new ArrayList<>(elements);
    }

    private IsWidget renderElement(int row, E element) {
        IsWidget rendering = renderer.render(element);
        Widget rendererWidget = rendering.asWidget();
        if(selectionInterval.contains(row)) {
            rendererWidget.addStyleName(BUNDLE.style().selection());
        }
        else {
            rendererWidget.addStyleName(BUNDLE.style().noSelection());
        }
        return rendering;
    }

    public List<E> getElements() {
        return new ArrayList<>(elements);
    }


    public void updateElement(E element) {

        int elementCount = elements.size();
        for(int index = 0; index < elementCount; index++) {
            E e = elements.get(index);
            if(keyExtractor.apply(e).equals(keyExtractor.apply(element))) {
                contentHolder.remove(index);
                contentHolder.insert(renderElement(index, element), index);
                elements.set(index, element);
            }
        }
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<List<E>> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    private void handleEventTarget(int clientY, Consumer<Integer> consumer) {
        for(int i = 0; i < contentHolder.getWidgetCount(); i++) {
            Widget w = contentHolder.getWidget(i);
            int itemY = w.getAbsoluteTop();
            if(itemY < clientY && clientY < itemY + w.getOffsetHeight()) {
                consumer.accept(i);
                return;
            }
        }
    }

    public Optional<E> getFirstSelectedElement() {
        return selectionInterval.getFirstSelectedElement(elements);
    }

    public List<E> getSelection() {
        if(selectionInterval.isEmpty()) {
            return Collections.emptyList();
        }
        List<E> sel = new ArrayList<>();
        int from, to;
        if(selectionInterval.getStartIndex() <= selectionInterval.getEndIndex()) {
            from = selectionInterval.getStartIndex();
            to = selectionInterval.getEndIndex();
        }
        else {
            from = selectionInterval.getEndIndex();
            to = selectionInterval.getStartIndex();
        }
        for(int i = from; i < to + 1; i++) {
            if (i < elements.size()) {
                E element = elements.get(i);
                sel.add(element);
            }
        }
        return sel;
    }

    public void setKeyExtractor(@Nonnull Function<E, K> keyExtractor) {
        this.keyExtractor = checkNotNull(keyExtractor);
    }

    public void setSelection(K key) {
        int row = 0;
        for(E element : elements) {
            K k = keyExtractor.apply(element);
            if(k != null && k.equals(key)) {
                setSelectionInterval(SelectionInterval.singleRow(row));
                break;
            }
            row++;
        }
    }

    public void clearSelection() {
        setSelectionInterval(SelectionInterval.emptySelection());
    }

    public void setSelectionInterval(SelectionInterval interval) {
        GWT.log("[ListBox] setSelectionInterval " + interval);
        SelectionInterval oldInterval = selectionInterval;
        selectionInterval = interval;
        if(oldInterval.equals(selectionInterval)) {
            return;
        }
        oldInterval.forEach(i -> setRowStyles(i, false));
        selectionInterval.forEach(i -> setRowStyles(i, true));
        SelectionEvent.fire(this, getSelection());
    }

    @UiHandler("focusPanel")
    public void handleClick(MouseUpEvent event) {
        GWT.log("[ListBox] handleClick");
        event.preventDefault();
        event.stopPropagation();
        handleEventTarget(event.getClientY(), i -> {
            GWT.log("[ListBox] handleClick for row " + i);
            SelectionInterval nextInterval;
            if(event.isShiftKeyDown()) {
                nextInterval = selectionInterval.extend(i);
            }
            else {
                nextInterval = SelectionInterval.singleRow(i);
            }
            GWT.log("[ListBox] Next selection interval: " + nextInterval);
            setSelectionInterval(nextInterval);
        });
    }

    @UiHandler("focusPanel")
    public void handleDoubleClick(DoubleClickEvent event) {

    }


    @UiHandler("focusPanel")
    public void focusPanelKeyUp(KeyDownEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_UP) {
            event.preventDefault();
            event.stopPropagation();
            decrementSelection();
        }
        else if(event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            event.preventDefault();
            event.stopPropagation();
            incrementSelection();
        }
    }

    private void decrementSelection() {
        SelectionInterval oldInterval = selectionInterval;
        SelectionInterval nextInterval = selectionInterval.decrement();
        if(oldInterval.equals(nextInterval)) {
            return;
        }
        setSelectionInterval(nextInterval);
        contentHolder.getWidget(selectionInterval.getStartIndex()).getElement().scrollIntoView();
    }

    private void incrementSelection() {
        SelectionInterval oldInterval = selectionInterval;
        SelectionInterval nextInterval = selectionInterval.increment(elements.size() - 1);
        if(oldInterval.equals(nextInterval)) {
            return;
        }
        setSelectionInterval(nextInterval);
        contentHolder.getWidget(selectionInterval.getEndIndex()).getElement().scrollIntoView();
    }

    private void setRowStyles(int row, boolean selected) {
        if(-1 < row && row < contentHolder.getWidgetCount()) {
            if (selected) {
                contentHolder.getWidget(row).removeStyleName(BUNDLE.style().noSelection());
                contentHolder.getWidget(row).addStyleName(BUNDLE.style().selection());
                contentHolder.getWidget(row).getElement().scrollIntoView();
            }
            else {
                contentHolder.getWidget(row).removeStyleName(BUNDLE.style().selection());
                contentHolder.getWidget(row).addStyleName(BUNDLE.style().noSelection());
            }
        }
    }

    @Override
    public void requestFocus() {
        Scheduler.get().scheduleDeferred(() -> {
            focusPanel.setFocus(true);
        });
    }

    public static class SelectionInterval {

        private static final SelectionInterval EMPTY = new SelectionInterval(-1, -1);

        private static final SelectionInterval FIRST = new SelectionInterval(0, 0);

        private final int startIndex;

        private final int endIndex;

        public SelectionInterval(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public boolean isEmpty() {
            return startIndex == -1;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public boolean contains(int row) {
            return row >= 0 && startIndex <= row && row <= endIndex;
        }

        public boolean isMultiSelect() {
            return getSelectionCount() > 1;
        }

        public int getSelectionCount() {
            return 1 + Math.abs(endIndex - startIndex);
        }

        public static SelectionInterval emptySelection() {
            return EMPTY;
        }

        public static SelectionInterval singleRow(int i) {
            return new SelectionInterval(i, i);
        }

        public SelectionInterval increment(int maxIndex) {
            int nextIndex;
            if(endIndex > startIndex) {
                nextIndex = endIndex + 1;
            }
            else {
                nextIndex = startIndex + 1;
            }
            if(nextIndex < maxIndex) {
                return new SelectionInterval(nextIndex, nextIndex);
            }
            else {
                return new SelectionInterval(maxIndex, maxIndex);
            }

        }

        public SelectionInterval decrement() {
            int nextIndex;
            if(startIndex < endIndex) {
                nextIndex = startIndex - 1;
            }
            else {
                nextIndex = endIndex - 1;
            }
            if(nextIndex > -1) {
                return new SelectionInterval(nextIndex, nextIndex);
            }
            else {
                return FIRST;
            }
        }

        public <E> Optional<E> getFirstSelectedElement(List<E> elements) {
            if(0 <= startIndex && startIndex < elements.size()) {
                return Optional.of(elements.get(startIndex));
            }
            else {
                return Optional.empty();
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(startIndex, endIndex);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SelectionInterval)) {
                return false;
            }
            SelectionInterval other = (SelectionInterval) obj;
            return this.startIndex == other.startIndex
                    && this.endIndex == other.endIndex;
        }

        public SelectionInterval extend(int index) {
            if(index < 0) {
                return this;
            }
            return new SelectionInterval(startIndex, index);
        }

        public void forEach(Consumer<Integer> consumer) {
            int from, to;
            GWT.log("[ListBox][SelectionInterval] forEach " + this);
            if(startIndex <= endIndex) {
                from = startIndex;
                to = endIndex;
            }
            else {
                from = endIndex;
                to = startIndex;
            }
            for(int i = from; i > -1 && i < to + 1; i++) {
                GWT.log("[ListBox][SelectionInterval] forEach " + i);
                consumer.accept(i);
            }
        }


        @Override
        public String toString() {
            return toStringHelper("SelectionInterval")
                    .add("start", startIndex)
                    .add("end", endIndex)
                    .toString();
        }
    }
}
