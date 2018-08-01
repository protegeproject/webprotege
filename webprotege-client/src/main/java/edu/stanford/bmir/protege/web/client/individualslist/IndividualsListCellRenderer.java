package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class IndividualsListCellRenderer implements ListBoxCellRenderer<OWLNamedIndividualData> {

    @Nonnull
    private DisplayDictionaryLanguage language = DisplayDictionaryLanguage.empty();

    @Override
    public IsWidget render(OWLNamedIndividualData element) {
        return new Label(element.getShortForms().entrySet().stream()
                                .filter(e -> e.getKey().equals(language))
                                .findFirst()
                                .map(Map.Entry::getValue)
                                .orElse(element.getBrowserText()));
    }

    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage displayLanguage) {
        this.language = checkNotNull(displayLanguage);
    }
}
