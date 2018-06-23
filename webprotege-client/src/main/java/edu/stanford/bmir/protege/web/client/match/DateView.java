package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public interface DateView extends IsWidget {

    /**
     * Returns the year e.g. 2018
     */
    int getYear();

    /**
     * Returns the month in the range of 1 to 12
     */
    int getMonth();

    /**
     * Returns the day in the range of 1 to 31
     */
    int getDay();

    void setYear(int year);

    void setMonth(int month);

    void setDay(int day);
}
