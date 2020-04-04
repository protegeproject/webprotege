package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-04
 */
public class FreshEntityIri_TestCase {


    public static final String UUID = "12345678-1234-1234-1234-123456789abc";

    public static final String LANG_TAG = "lang-tag";

    public static final String SUPPLIED_NAME = "Supplied Name";

    public static final EntityType<OWLObjectProperty> ENTITY_TYPE = EntityType.OBJECT_PROPERTY;

    private final ImmutableSet<IRI> parentIris = ImmutableSet.of(IRI.create("http://example.org/ParentIriA"),
                                                                 IRI.create("http://example.orf/ParentIriB"));

    private FreshEntityIri freshEntityIri;

    @Before
    public void setUp() {
        freshEntityIri = FreshEntityIri.get(ENTITY_TYPE,
                                            SUPPLIED_NAME,
                                            LANG_TAG,
                                            UUID,
                                            parentIris);
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenEntityType() {
        assertThat(freshEntityIri.getEntityType(), is(equalTo(ENTITY_TYPE)));
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenSuppliedName() {
        assertThat(freshEntityIri.getSuppliedName(), is(equalTo(SUPPLIED_NAME)));
    }

    @Test
    public void shouldGetFreshEntityIriWitGivenLangTag() {
        assertThat(freshEntityIri.getLangTag(), is(equalTo(LANG_TAG)));
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenUuid() {
        assertThat(freshEntityIri.getUuid(), is(equalTo(UUID)));
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenParentIris() {
        assertThat(freshEntityIri.getParentIris(), is(equalTo(parentIris)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_If_EntityType_IsNull() {
        FreshEntityIri.get(null, SUPPLIED_NAME, LANG_TAG, UUID, parentIris);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_If_SuppliedName_IsNull() {
        FreshEntityIri.get(ENTITY_TYPE, null, LANG_TAG, UUID, parentIris);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_If_LangTag_IsNull() {
        FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, null, UUID, parentIris);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_If_Uuid_IsNull() {
        FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, null, parentIris);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_If_ParentIris_IsNull() {
        FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, UUID, null);
    }

    @Test
    public void shouldAcceptEmptySuppliedName() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, "", LANG_TAG, UUID, parentIris);
        assertThat(freshEntityIri.getSuppliedName(), is(""));
    }

    @Test
    public void shouldAcceptEmptyLangTag() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, "", UUID, parentIris);
        assertThat(freshEntityIri.getLangTag(), is(""));
    }

    @Test
    public void shouldAcceptEmptyUuid() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, "", parentIris);
        assertThat(freshEntityIri.getUuid(), is(""));
    }

    @Test
    public void shouldAcceptEmptyParentIris() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, UUID, ImmutableSet.of());
        assertThat(freshEntityIri.getParentIris(), is(ImmutableSet.of()));
    }

    @Test
    public void shouldRoundTrip() {
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptySuppliedName() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, "", LANG_TAG, UUID, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyLangTag() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, "", UUID, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyUuid() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, "", parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyParentIris() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, UUID, ImmutableSet.of());
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmpty() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, "", "", "", ImmutableSet.of());
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripSuppliedNameWithHash() {
        String suppliedName = "The#Supplied#Name";
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, suppliedName, LANG_TAG, UUID, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripParentIriWithAmpersand() {
        IRI parentIri = IRI.create("http://example.org/A&B");
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, UUID, ImmutableSet.of(parentIri));
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripParentIriWithHash() {
        IRI parentIri = IRI.create("http://example.org#A");
        FreshEntityIri freshEntityIri = FreshEntityIri.get(ENTITY_TYPE, SUPPLIED_NAME, LANG_TAG, UUID, ImmutableSet.of(parentIri));
        assertRoundTrips(freshEntityIri);
    }

    public void assertRoundTrips(@Nonnull FreshEntityIri freshEntityIri) {
        String iriString = freshEntityIri.getIri().toString();
        FreshEntityIri parsedFreshEntityIri = FreshEntityIri.parse(iriString);
        assertThat(parsedFreshEntityIri, is(equalTo(freshEntityIri)));
    }

}
