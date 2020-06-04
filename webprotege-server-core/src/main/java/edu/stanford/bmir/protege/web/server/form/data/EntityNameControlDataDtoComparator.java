package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.Comparators;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.EntityNameControlDataDto;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

public class EntityNameControlDataDtoComparator implements Comparator<EntityNameControlDataDto> {

    private static final Comparator<Optional<OWLEntityData>> optionalEntityDataComparator = Comparators.emptiesLast(
            OWLEntityData::compareToIgnoreCase
    );

    @Inject
    public EntityNameControlDataDtoComparator() {
    }

    @Override
    public int compare(EntityNameControlDataDto o1, EntityNameControlDataDto o2) {
        return optionalEntityDataComparator.compare(o1.getEntity(), o2.getEntity());
    }
}
