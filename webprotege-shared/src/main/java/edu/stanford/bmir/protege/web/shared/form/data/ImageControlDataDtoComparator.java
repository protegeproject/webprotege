package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.Comparators;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

public class ImageControlDataDtoComparator implements Comparator<ImageControlDataDto> {

    private static final Comparator<Optional<IRI>> optionalComparator = Comparators.emptiesLast(
            IRI::compareTo
    );

    @Inject
    public ImageControlDataDtoComparator() {
    }

    @Override
    public int compare(ImageControlDataDto o1, ImageControlDataDto o2) {
        return optionalComparator.compare(o1.getIri(), o2.getIri());
    }
}
