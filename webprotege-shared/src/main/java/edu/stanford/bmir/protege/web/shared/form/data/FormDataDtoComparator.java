package edu.stanford.bmir.protege.web.shared.form.data;

import javax.inject.Inject;
import java.util.Comparator;

public class FormDataDtoComparator implements Comparator<FormDataDto> {

    @Inject
    public FormDataDtoComparator() {
    }

    @Override
    public int compare(FormDataDto o1, FormDataDto o2) {
        return 0;
    }
}
