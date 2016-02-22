package edu.stanford.bmir.protege.web.client.workspace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ApplicationViewImpl extends Composite implements ApplicationView, RequiresResize, ProvidesResize {

    interface ApplicationViewImplUiBinder extends UiBinder<HTMLPanel, ApplicationViewImpl> {

    }

    private static ApplicationViewImplUiBinder ourUiBinder = GWT.create(ApplicationViewImplUiBinder.class);

    @UiField
    protected SimpleLayoutPanel mainView;

    @Inject
    public ApplicationViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setWidget(IsWidget widget) {
        mainView.setWidget(widget);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                onResize();
            }
        });
    }

    @Override
    public void onResize() {
        LayoutUtil.setBounds(mainView, 0, 0, 0, 0);
        mainView.onResize();
    }


}