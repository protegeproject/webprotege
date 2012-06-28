package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.apache.commons.collections.FastTreeMap;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/06/2012
 */
public class AbstractWebProtegeHierarchyView extends FlowPanel {

    private Tree tree;
    
    private TreeHierarchyServiceProvider hierarchyService;

    private PopupPanel dragPopupPanel = new PopupPanel(true);

    private Timer dragPopupPanelTimer = new Timer() {
        @Override
        public void run() {
            dragPopupPanel.hide();
        }
    };

    public AbstractWebProtegeHierarchyView(TreeHierarchyServiceProvider hierarchyService) {
        this.hierarchyService = hierarchyService;
        tree = new Tree();
        add(tree);
        tree.setAnimationEnabled(true);
        initializeRoots();
        dragPopupPanel.add(new Label("Drop here"));
        dragPopupPanel.addStyleName("gwt-DialogBox");

    }
    
    private void initializeRoots() {
        for(int i = 0; i < 10; i++) {
            TreeItem item = new LazyTreeItem(new Label("A " + i));
            tree.addItem(item);
            if(i == 0) {
                tree.setSelectedItem(item);
            }

//            for(int j = 0; j < 10; j++) {
//                TextBox widget = new TextBox();
//                widget.setText("B " + j);
//                item.addItem(widget);
//            }
        }
    }



    private class LazyTreeItem extends TreeItem {

        private boolean inited = false;
        
        private int mouseX;
        
        private int mouseY;
        
        public LazyTreeItem(final Label widget) {
            super(widget);
            widget.addMouseDownHandler(new MouseDownHandler() {
                public void onMouseDown(MouseDownEvent event) {
                    mouseX = event.getX();
                    mouseY = event.getY();
                }
            });
            setState(false);
            addItem("DUMMY");
            widget.getElement().setDraggable(Element.DRAGGABLE_TRUE);
            widget.addDragStartHandler(new DragStartHandler() {
                public void onDragStart(DragStartEvent event) {
                    System.out.println("Drag start");
                    event.setData("text", "My dragged thing");
                    event.getDataTransfer().setDragImage(widget.getElement(), mouseX, mouseY);
                    dragPopupPanel.show();
                    setLoc(event.getRelativeElement());
                }
            });


            widget.addDragLeaveHandler(new DragLeaveHandler() {
                public void onDragLeave(DragLeaveEvent event) {
                    dragPopupPanelTimer.schedule(300);
                }
            });

            widget.addDragEnterHandler(new DragEnterHandler() {
                public void onDragEnter(DragEnterEvent event) {
                    dragPopupPanelTimer.cancel();
                    dragPopupPanel.show();
                    setLoc(event.getRelativeElement());
                }
            });

            widget.addDragOverHandler(new DragOverHandler() {
                public void onDragOver(DragOverEvent event) {
                    widget.addStyleName("web-protege-error-background");
                    Element element = event.getRelativeElement();
                    setLoc(element);
                }

            });

            widget.addDragLeaveHandler(new DragLeaveHandler() {
                public void onDragLeave(DragLeaveEvent event) {
                    widget.removeStyleName("web-protege-error-background");
                }
            });

            widget.addDropHandler(new DropHandler() {
                public void onDrop(DropEvent event) {
                    System.out.println("Drop");
                    widget.removeStyleName("web-protege-error-background");
                    event.preventDefault();
                    dragPopupPanel.hide();

                }
            });

        }


        private void setLoc(Element element) {
            dragPopupPanel.setPopupPosition(element.getAbsoluteRight() + 4, element.getAbsoluteTop() + 4);
        }

        @Override
        public void setState(boolean open) {
            fill(open);
            super.setState(open);
        }

        @Override
        public void setState(boolean open, boolean fireEvents) {
            fill(open);
            super.setState(open, fireEvents);
        }

        private void fill(boolean open) {
            if(open) {
                if(!inited) {
                    inited = true;
                    removeItems();
                    for(int i = 0; i < 5; i++) {
                        addItem(new LazyTreeItem(new Label("Item " + i)));
                    }
                }
            }
        }

        @Override
        public TreeItem getChild(int index) {
            return super.getChild(index);
        }

        @Override
        public TreeItem addItem(SafeHtml itemHtml) {
            return super.addItem(itemHtml);
        }
    }
}
