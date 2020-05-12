package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public interface FormTabView extends IsWidget {

    void setClickHandler(@Nonnull ClickHandler clickHandler);

    void setLabel(@Nonnull LanguageMap label);

    void setSelected(boolean selected);
}
