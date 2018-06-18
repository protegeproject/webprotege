package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public interface DateCriteria extends LexicalValueCriteria {

    String YEAR = "year";

    String MONTH = "month";

    String DAY = "day";

    @JsonProperty(YEAR)
    int getYear();

    @JsonProperty(MONTH)
    int getMonth();

    @JsonProperty(DAY)
    int getDay();

    static void checkArgs(int year, int month, int day) {
        checkArgument(month >= 1 && month <= 12);
        checkArgument(day >= 1 && day <= 31);

    }
}
