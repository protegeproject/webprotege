package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;

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
