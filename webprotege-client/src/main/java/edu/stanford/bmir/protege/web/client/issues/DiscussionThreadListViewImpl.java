package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class DiscussionThreadListViewImpl extends Composite implements DiscussionThreadListView {

    private boolean enabled = false;

    interface DiscussionThreadListViewImplUiBinder extends UiBinder<HTMLPanel, DiscussionThreadListViewImpl> {

    }

    private static DiscussionThreadListViewImplUiBinder ourUiBinder = GWT.create(DiscussionThreadListViewImplUiBinder.class);

    @UiField
    FlowPanel threadContainer;

    private final List<DiscussionThreadView> threadViews = new ArrayList<>();

    @Inject
    public DiscussionThreadListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        threadContainer.clear();
        threadViews.clear();
    }

    @Override
    public void addDiscussionThreadView(DiscussionThreadView view) {
        threadContainer.add(view);
        threadViews.add(view);
    }

    @Override
    public void hideDiscussionThreadView(DiscussionThreadView threadView) {
        if(threadViews.remove(threadView)) {
            Animation animation = new Animation() {
                @Override
                protected void onUpdate(double progress) {
                    double opacity = 1 - (progress);
                    final Element element = threadView.asWidget().getElement();
                    element.getStyle().setOpacity(opacity);
                    final double height = element.getClientHeight();
                    if (progress == 1) {
                        Animation slideAnimation = new Animation() {
                            @Override
                            protected void onUpdate(double progress) {
                                double slideHeight = height * (1 - progress);
                                element.getStyle().setHeight((int) slideHeight, Style.Unit.PX);
                                if(progress == 1) {
                                    threadContainer.remove(threadView);
                                }
                            }
                        };
                        slideAnimation.run(500);
                    }
                }
            };
            animation.run(400);
        }
    }

    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param b <code>true</code> to enable the widget, <code>false</code>
     *                to disable it
     */
    @Override
    public void setEnabled(boolean b) {
        enabled = true;
        threadViews.forEach(v -> v.setEnabled(b));
    }
}