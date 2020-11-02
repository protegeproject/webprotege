package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.IncrementingPatternDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class EntityCrudKitSettings_SerializationTestCase {

    private EntityCrudKitSettings<UuidSuffixSettings> settings;

    @Before
    public void setUp() {
        settings = EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(),
                                             UuidSuffixSettings.get(),
                                             GeneratedAnnotationsSettings.get(ImmutableList.of(
                                                     GeneratedAnnotationDescriptor.get(DataFactory.get().getRDFSLabel(),
                                                                                       IncrementingPatternDescriptor.get(
                                                                                               100_000,
                                                                                               "%d"),
                                                                                       SubClassOfCriteria.get(DataFactory.getOWLThing(),
                                                                                                              HierarchyFilterType.DIRECT)))));
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(settings, EntityCrudKitSettings.class);
    }
}
