package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.Comparators;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlDataDto;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

public class SingleChoiceControlDataDtoComparator implements Comparator<SingleChoiceControlDataDto> {

    private static Comparator<Optional<PrimitiveFormControlDataDto>> optionalComparator = Comparators.emptiesLast(
            PrimitiveFormControlDataDto::compareTo
    );

    @Inject
    public SingleChoiceControlDataDtoComparator() {
    }

    @Override
    public int compare(SingleChoiceControlDataDto o1, SingleChoiceControlDataDto o2) {
        return optionalComparator.compare(o1.getChoice(), o2.getChoice());
    }
}
