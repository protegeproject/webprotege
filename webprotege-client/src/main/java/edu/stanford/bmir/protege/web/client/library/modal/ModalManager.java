package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import elemental.client.Browser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2018
 */
@ApplicationSingleton
public class ModalManager {

    @Nonnull
    private final Stack<ModalPresenterData> modalStack = new Stack<>();

    @Nonnull
    private final Provider<ModalPresenter> modalPresenterProvider;

    private boolean handlersAttached = false;

    private Optional<elemental.dom.Element> focusedElement = Optional.empty();

    @Inject
    public ModalManager(@Nonnull Provider<ModalPresenter> modalPresenterProvider) {
        this.modalPresenterProvider = checkNotNull(modalPresenterProvider);
        attachHandlers();
    }

    private static boolean isAcceptAccelerator(@Nonnull NativeEvent event) {
        return event.getKeyCode() == KeyCodes.KEY_ENTER && event.getCtrlKey();
    }

    private static boolean isEscapeAccelerator(@Nonnull NativeEvent event) {
        return event.getKeyCode() == KeyCodes.KEY_ESCAPE;
    }

    @Nonnull
    private Optional<Element> getFirstFocusableElement() {
        if(modalStack.isEmpty()) {
            return Optional.empty();
        }
        ModalPresenterData presenter = modalStack.peek();
        return getFirstFocusableElementFrom(presenter.getElement());
    }

    @Nonnull
    private static Optional<Element> getFirstFocusableElementFrom(@Nonnull Element element) {
        if (element.getTabIndex() == 0) {
            return Optional.of(element);
        }
        else {
            for (int i = 0; i < element.getChildCount(); i++) {
                Node child = element.getChild(i);
                if (Element.is(child)) {
                    Optional<Element> e = getFirstFocusableElementFrom(Element.as(child));
                    if (e.isPresent()) {
                        return e;
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Nonnull
    private Optional<Element> getLastFocusableElement() {
        if(modalStack.isEmpty()) {
            return Optional.empty();
        }
        ModalPresenterData presenter = modalStack.peek();
        return getLastFocusableElementFrom(presenter.getElement(), Optional.empty());
    }

    @Nonnull
    private static Optional<Element> getLastFocusableElementFrom(Element element, Optional<Element> currentFocusable) {
        if (element.getTabIndex() == 0) {
            currentFocusable = Optional.of(element);
        }
        for (int i = 0; i < element.getChildCount(); i++) {
            Node child = element.getChild(i);
            if (Element.is(child)) {
                currentFocusable = getLastFocusableElementFrom(Element.as(child), currentFocusable);
            }
        }
        return currentFocusable;
    }

    private void attachHandlers() {
        if(handlersAttached) {
            return;
        }
        handlersAttached = true;
        Event.addNativePreviewHandler(this::handleNativePreviewEvent);
        RootPanel widgets = RootPanel.get();
        widgets.sinkEvents(Event.ONKEYUP);
        BodyElement element = Document.get().getBody();
        elemental.dom.Element e = (elemental.dom.Element) element;
        e.addEventListener(BrowserEvents.FOCUSIN, this::handleFocus);
    }

    private void handleNativePreviewEvent(@Nonnull NativePreviewEvent event) {
        if (modalStack.isEmpty()) {
            return;
        }
        NativeEvent nativeEvent = event.getNativeEvent();
        EventTarget eventTarget = nativeEvent.getEventTarget();
        if (!Element.is(eventTarget)) {
            return;
        }
        Element element = Element.as(eventTarget);
        switch (event.getTypeInt()) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEUP:
                if (!isElementInCurrentModal(element)) {
                    event.cancel();
                }
                else if (!isInFocusableElement(element)) {
                    event.cancel();
                }
                break;
            case Event.ONKEYDOWN:
                if (nativeEvent.getKeyCode() == KeyCodes.KEY_TAB) {
                    if(nativeEvent.getShiftKey()) {
                        // Tabbing backwards
                        getFirstFocusableElement().ifPresent(firstFocusable -> {
                            if(element.equals(firstFocusable)) {
                                event.cancel();
                                getLastFocusableElement().ifPresent(Element::focus);
                            }
                        });
                    }
                    else {
                        // Tabbing forward
                        getLastFocusableElement().ifPresent(lastFocusable -> {
                            if (lastFocusable.equals(element)) {
                                event.cancel();
                                getFirstFocusableElement().ifPresent(Element::focus);
                            }
                        });
                    }
                }
                else if(isAcceptAccelerator(nativeEvent)) {
                    event.cancel();
                }
                break;
            case Event.ONKEYUP:
                if(isEscapeAccelerator(nativeEvent)) {
                    event.cancel();
                    handleEscapeAccelerator();
                }
                else if(isAcceptAccelerator(nativeEvent)) {
                    event.cancel();
                    handleAcceptAccelerator();
                }
                break;
        }
    }

    private boolean isInFocusableElement(@Nonnull Element element) {
        Element parent = element;
        while (parent != null) {
            if(parent.getTabIndex() == 0) {
                return true;
            }
            parent = parent.getParentElement();
        }
        return false;
    }

    private void handleFocus(@Nonnull elemental.events.Event event) {
        elemental.events.EventTarget sourceElement = event.getSrcElement();
        Element element = (Element) sourceElement;
        if(isElementInCurrentModal(element)) {
            modalStack.peek().setLastFocusedElement(element);
        }
    }

    private boolean isInCurrentModal(@Nonnull NativeEvent event) {
        EventTarget eventTarget = event.getEventTarget();
        return Element.is(eventTarget) && isElementInCurrentModal(Element.as(eventTarget));
    }

    private boolean isElementInCurrentModal(@Nonnull Element element) {
        if (modalStack.isEmpty()) {
            return false;
        }
        Element viewElement = modalStack.peek().getElement();
        Element parent = element;
        while (parent != null) {
            if (parent.equals(viewElement)) {
                return true;
            }
            parent = parent.getParentElement();
        }
        return false;
    }

    private void handleAcceptAccelerator() {
        if (modalStack.isEmpty()) {
            return;
        }
        ModalPresenterData presenter = modalStack.peek();
        presenter.handleAccept();
    }

    private void handleEscapeAccelerator() {
        if (modalStack.isEmpty()) {
            return;
        }
        hideCurrentModal();
    }

    @Nonnull
    public ModalPresenter createPresenter() {
        return modalPresenterProvider.get();
    }

    public void showModal(@Nonnull ModalPresenter presenter) {
        boolean alreadyShowing = isShowing(presenter);
        if (alreadyShowing) {
            throw new RuntimeException("Already showing modal for presenter");
        }
        if(modalStack.isEmpty()) {
            focusedElement = Optional.ofNullable(Browser.getDocument().getActiveElement());
        }
        modalStack.push(new ModalPresenterData(presenter));
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(presenter.getView());
        presenter.setModalCloser(this::hideCurrentModal);
    }

    private boolean isShowing(@Nonnull ModalPresenter presenter) {
        return modalStack.stream().anyMatch(pd -> pd.presenter.equals(presenter));
    }

    private void hideCurrentModal() {
        if(modalStack.isEmpty()) {
            return;
        }
        ModalPresenterData currentModal = modalStack.pop();
        currentModal.presenter.hide();
        if(!modalStack.isEmpty()) {
            modalStack.peek().focusLastElement();
        }
        else {
            focusedElement.ifPresent(elemental.dom.Element::focus);
        }
    }

    private static class ModalPresenterData {

        @Nonnull
        private final ModalPresenter presenter;

        @Nullable
        private Element lastFocusedElement;

        public ModalPresenterData(@Nonnull ModalPresenter presenter) {
            this.presenter = checkNotNull(presenter);
        }

        @Nonnull
        public IsWidget getView() {
            return presenter.getView();
        }

        @Nonnull
        public Element getElement() {
            return presenter.getView().asWidget().getElement();
        }

        @Nonnull
        public Optional<Element> getLastFocusedElement() {
            return Optional.ofNullable(lastFocusedElement);
        }

        public void setLastFocusedElement(@Nonnull Element element) {
            lastFocusedElement = checkNotNull(element);
        }

        public void handleAccept() {
            presenter.accept();
        }

        public void handleEscape() {
            presenter.escape();
        }

        public void focusLastElement() {
            if(lastFocusedElement != null) {
                lastFocusedElement.focus();
            }
        }
    }
}
