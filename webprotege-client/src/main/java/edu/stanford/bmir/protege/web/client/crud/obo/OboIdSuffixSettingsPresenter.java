package edu.stanford.bmir.protege.web.client.crud.obo;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OboIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class OboIdSuffixSettingsPresenter {

    public static final int DEFAULT_TOTAL_DIGITS = 7;

    @Nonnull
    private final OboIdSuffixSettingsView view;

    @Inject
    public OboIdSuffixSettingsPresenter(@Nonnull OboIdSuffixSettingsView view) {
        this.view = view;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.clearView();
        view.setTotalDigits(Integer.toString(DEFAULT_TOTAL_DIGITS));
    }

    public void setSettings(@Nonnull OboIdSuffixSettings settings) {
        checkNotNull(settings);
        int totalDigits = settings.getTotalDigits();
        view.setTotalDigits(Integer.toString(totalDigits));
        List<UserIdRange> userIdRanges = settings.getUserIdRanges();
        view.setUserIdRanges(userIdRanges);
    }

    @Nonnull
    public OboIdSuffixSettings getSettings() {
        int totalDigits = getTotalDigits();
        List<UserIdRange> userIdRanges = view.getUserIdRanges();
        return OboIdSuffixSettings.get(totalDigits, ImmutableList.copyOf(userIdRanges));
    }

    private int getTotalDigits() {
        try {
            String totalDigitsString = view.getTotalDigits();
            return Integer.parseInt(totalDigitsString);
        } catch(NumberFormatException e) {
            return DEFAULT_TOTAL_DIGITS;
        }
    }
}
