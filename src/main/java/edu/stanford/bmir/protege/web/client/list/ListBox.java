package edu.stanford.bmir.protege.web.client.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static cern.clhep.Units.s;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Aug 2017
 */
public class ListBox<K, E> extends Composite implements HasSelectionHandlers<Optional<E>> {

    interface ListBoxUiBinder extends UiBinder<HTMLPanel, ListBox> {

    }

    private static ListBoxUiBinder ourUiBinder = GWT.create(ListBoxUiBinder.class);

    @UiField
    FlowPanel contentHolder;

    private int selectedRow = 0;

    private List<E> elements = new ArrayList<>();

    private ListBoxCellRenderer<E> renderer = element -> new Label(element.toString());

    public ListBox() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setRenderer(@Nonnull ListBoxCellRenderer<E> renderer) {
        this.renderer = checkNotNull(renderer);
    }

    public void setListData(@Nonnull List<E> elements) {
        contentHolder.clear();
        int row = 0;
        this.elements.clear();
        for(E element : elements) {
            this.elements.add(element);
            // TODO: Render
            IsWidget rendering = renderer.render(element);
            Widget rendererWidget = rendering.asWidget();
            if(row == selectedRow) {
                rendererWidget.addStyleName(BUNDLE.style().selection());
            }
            else {
                rendererWidget.addStyleName(BUNDLE.style().noSelection());
            }
            contentHolder.add(rendering);
            row++;
        }
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Optional<E>> handler) {
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

    public Optional<E> getSelection() {
        if(selectedRow == -1) {
            return Optional.empty();
        }
        else {
            return Optional.of(elements.get(selectedRow));
        }
    }

    public void setSelection(K key) {
        int row = 0;
        for(E element : elements) {
            if(element.equals(key)) {
                setSelectedRow(row);
                break;
            }
            row++;
        }
    }

    public void clearSelection() {
        setSelectedRow(-1);
    }

    public void setSelectedRow(int selectedRow) {
        if(this.selectedRow == selectedRow) {
            return;
        }
        int oldSel = this.selectedRow;
        this.selectedRow = selectedRow;
        if (oldSel != -1) {
            setRowStyles(oldSel, false);
        }
        if (selectedRow != -1) {
            setRowStyles(this.selectedRow, true);
        }
        SelectionEvent.fire(this, getSelection());
    }

    @UiHandler("focusPanel")
    public void handleClick(MouseUpEvent event) {
        handleEventTarget(event.getClientY(), this::setSelectedRow);
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
        if(selectedRow > 0) {
            setSelectedRow(selectedRow - 1);
            contentHolder.getWidget(selectedRow).getElement().scrollIntoView();
        }
    }

    private void incrementSelection() {
        if(selectedRow < elements.size() - 1) {
            setSelectedRow(selectedRow + 1);
            contentHolder.getWidget(selectedRow).getElement().scrollIntoView();
        }
    }

    private void setRowStyles(int row, boolean selected) {
        if (selected) {
            contentHolder.getWidget(row).removeStyleName(BUNDLE.style().noSelection());
            contentHolder.getWidget(row).addStyleName(BUNDLE.style().selection());
        }
        else {
            contentHolder.getWidget(row).removeStyleName(BUNDLE.style().selection());
            contentHolder.getWidget(row).addStyleName(BUNDLE.style().noSelection());
        }
    }
}