package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeShortFromProvider_TestCase {

    public static final String LANG = "LANG";

    public static final String LITERAL = "LITERAL";

    public static final String ENTITY_IRI_FRAGMENT = "FRAGMENT";

    @Mock
    private HasAnnotationAssertionAxioms annotationAssertionAxiomsProvider;

    @Mock
    private HasLang languageProvider;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertion;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI entityIRI;

    @Mock
    private OWLLiteral annotationValue;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    private WebProtegeShortFormProvider shortFormProvider;

    @Before
    public void setUp() throws Exception {
        when(entity.getIRI()).thenReturn(entityIRI);
        when(entityIRI.getFragment()).thenReturn(ENTITY_IRI_FRAGMENT);

        when(annotationAssertion.getValue()).thenReturn(annotationValue);
        when(annotationValue.getLiteral()).thenReturn(LITERAL);
        when(annotationValue.getLang()).thenReturn(LANG);

        when(annotationAssertion.getProperty()).thenReturn(annotationProperty);

        when(annotationProperty.getIRI()).thenReturn(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        when(annotationAssertionAxiomsProvider.getAnnotationAssertionAxioms(entityIRI)).thenReturn(
                Sets.newHashSet(annotationAssertion));
        shortFormProvider = new WebProtegeShortFormProvider(
                DefaultLabellingIRIs.asImmutableList(),
                annotationAssertionAxiomsProvider,
                languageProvider);
    }

    @Test
    public void shouldReturnLiteralValueWithLang() {
        when(languageProvider.getLang()).thenReturn(LANG);
        String shortForm = shortFormProvider.getShortForm(entity);
        assertThat(shortForm, equalTo(LITERAL));
    }

    @Test
    public void shouldReturnLiteralValueForUnknownLang() {
        when(languageProvider.getLang()).thenReturn("OTHER");
        String shortForm = shortFormProvider.getShortForm(entity);
        assertThat(shortForm, equalTo(LITERAL));
    }

    @Test
    public void shouldReturnLiteralValueOfRDFSLabelAnnotation() {
        when(annotationProperty.getIRI()).thenReturn(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        assertThat(shortFormProvider.getShortForm(entity), is(LITERAL));
    }

    @Test
    public void shouldReturnLiteralValueOfSKOSPrefLabelAnnotation() {
        when(annotationProperty.getIRI()).thenReturn(SKOSVocabulary.PREFLABEL.getIRI());
        assertThat(shortFormProvider.getShortForm(entity), is(LITERAL));
    }


    @Test
    public void shouldReturnEntityIRIFragmentIfAnnotationAssertionPropertyIsNotKnown() {
        when(annotationProperty.getIRI()).thenReturn(IRI.create("http://other.com/other"));
        assertThat(shortFormProvider.getShortForm(entity), is(ENTITY_IRI_FRAGMENT));
    }

    @Test
    public void shouldReturnBuiltInRenderingForOWLThing() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_TARGET_INDIVIDUAL);
    }

    @Test
    public void shouldReturnBuiltInRenderingForOWLNothing() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_NOTHING);
    }

    @Test
    public void shouldReturnBuiltInRenderingForOWLTopObjectProperty() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_TOP_OBJECT_PROPERTY);
    }
    
    @Test
    public void shouldReturnBuiltInRenderingForOWLBottomObjectProperty() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_BOTTOM_OBJECT_PROPERTY);
    }


    @Test
    public void shouldReturnBuiltInRenderingForOWLTopDataProperty() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_TOP_DATA_PROPERTY);
    }
    
    @Test
    public void shouldReturnBuiltInRenderingForOWLBottomDataProperty() {
        testBuiltInShortForm(OWLRDFVocabulary.OWL_BOTTOM_DATA_PROPERTY);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSAltLabel() {
        testBuiltInShortForm(SKOSVocabulary.ALTLABEL);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSPrefLabel() {
        testBuiltInShortForm(SKOSVocabulary.PREFLABEL);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSHiddenLabel() {
        testBuiltInShortForm(SKOSVocabulary.HIDDENLABEL);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSChangeNote() {
        testBuiltInShortForm(SKOSVocabulary.CHANGENOTE);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSDefinition() {
        testBuiltInShortForm(SKOSVocabulary.DEFINITION);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSExactMatch() {
        testBuiltInShortForm(SKOSVocabulary.EXACTMATCH);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSBroadMatch() {
        testBuiltInShortForm(SKOSVocabulary.BROADMATCH);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSNarrowMatch() {
        testBuiltInShortForm(SKOSVocabulary.NARROWMATCH);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSEditorialNote() {
        testBuiltInShortForm(SKOSVocabulary.EDITORIALNOTE);
    }


    @Test
    public void shouldReturnBuiltInRenderingForSKOSHistoryNote() {
        testBuiltInShortForm(SKOSVocabulary.HISTORYNOTE);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSNote() {
        testBuiltInShortForm(SKOSVocabulary.NOTE);
    }

    @Test
    public void shouldReturnBuiltInRenderingForSKOSRelatedMatch() {
        testBuiltInShortForm(SKOSVocabulary.RELATEDMATCH);
    }

    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Contributor() {
        testBuiltInShortForm(DublinCoreVocabulary.CONTRIBUTOR);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Coverage() {
        testBuiltInShortForm(DublinCoreVocabulary.COVERAGE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Creator() {
        testBuiltInShortForm(DublinCoreVocabulary.CREATOR);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Date() {
        testBuiltInShortForm(DublinCoreVocabulary.DATE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Description() {
        testBuiltInShortForm(DublinCoreVocabulary.DESCRIPTION);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Format() {
        testBuiltInShortForm(DublinCoreVocabulary.FORMAT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Identifier() {
        testBuiltInShortForm(DublinCoreVocabulary.IDENTIFIER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Language() {
        testBuiltInShortForm(DublinCoreVocabulary.LANGUAGE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Publisher() {
        testBuiltInShortForm(DublinCoreVocabulary.PUBLISHER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Relation() {
        testBuiltInShortForm(DublinCoreVocabulary.RELATION);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Rights() {
        testBuiltInShortForm(DublinCoreVocabulary.RIGHTS);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Source() {
        testBuiltInShortForm(DublinCoreVocabulary.SOURCE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Subject() {
        testBuiltInShortForm(DublinCoreVocabulary.SUBJECT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Title() {
        testBuiltInShortForm(DublinCoreVocabulary.TITLE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Type() {
        testBuiltInShortForm(DublinCoreVocabulary.TYPE);
    }

    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Any_type() {
        testBuiltInShortForm(XSDVocabulary.ANY_TYPE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Any_simple_type() {
        testBuiltInShortForm(XSDVocabulary.ANY_SIMPLE_TYPE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_String() {
        testBuiltInShortForm(XSDVocabulary.STRING);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Integer() {
        testBuiltInShortForm(XSDVocabulary.INTEGER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Long() {
        testBuiltInShortForm(XSDVocabulary.LONG);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Int() {
        testBuiltInShortForm(XSDVocabulary.INT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Short() {
        testBuiltInShortForm(XSDVocabulary.SHORT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Byte() {
        testBuiltInShortForm(XSDVocabulary.BYTE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Decimal() {
        testBuiltInShortForm(XSDVocabulary.DECIMAL);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Float() {
        testBuiltInShortForm(XSDVocabulary.FLOAT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Boolean() {
        testBuiltInShortForm(XSDVocabulary.BOOLEAN);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Double() {
        testBuiltInShortForm(XSDVocabulary.DOUBLE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Non_positive_integer() {
        testBuiltInShortForm(XSDVocabulary.NON_POSITIVE_INTEGER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Negative_integer() {
        testBuiltInShortForm(XSDVocabulary.NEGATIVE_INTEGER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Non_negative_integer() {
        testBuiltInShortForm(XSDVocabulary.NON_NEGATIVE_INTEGER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Unsigned_long() {
        testBuiltInShortForm(XSDVocabulary.UNSIGNED_LONG);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Unsigned_int() {
        testBuiltInShortForm(XSDVocabulary.UNSIGNED_INT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Positive_integer() {
        testBuiltInShortForm(XSDVocabulary.POSITIVE_INTEGER);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Base_64_binary() {
        testBuiltInShortForm(XSDVocabulary.BASE_64_BINARY);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Normalized_string() {
        testBuiltInShortForm(XSDVocabulary.NORMALIZED_STRING);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Hex_binary() {
        testBuiltInShortForm(XSDVocabulary.HEX_BINARY);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Any_uri() {
        testBuiltInShortForm(XSDVocabulary.ANY_URI);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Q_name() {
        testBuiltInShortForm(XSDVocabulary.Q_NAME);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Notation() {
        testBuiltInShortForm(XSDVocabulary.NOTATION);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Token() {
        testBuiltInShortForm(XSDVocabulary.TOKEN);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Language() {
        testBuiltInShortForm(XSDVocabulary.LANGUAGE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Name() {
        testBuiltInShortForm(XSDVocabulary.NAME);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Ncname() {
        testBuiltInShortForm(XSDVocabulary.NCNAME);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Nmtoken() {
        testBuiltInShortForm(XSDVocabulary.NMTOKEN);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Id() {
        testBuiltInShortForm(XSDVocabulary.ID);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Idref() {
        testBuiltInShortForm(XSDVocabulary.IDREF);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Idrefs() {
        testBuiltInShortForm(XSDVocabulary.IDREFS);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Entity() {
        testBuiltInShortForm(XSDVocabulary.ENTITY);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Entities() {
        testBuiltInShortForm(XSDVocabulary.ENTITIES);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Duration() {
        testBuiltInShortForm(XSDVocabulary.DURATION);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Date_time() {
        testBuiltInShortForm(XSDVocabulary.DATE_TIME);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Date_time_stamp() {
        testBuiltInShortForm(XSDVocabulary.DATE_TIME_STAMP);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Time() {
        testBuiltInShortForm(XSDVocabulary.TIME);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Date() {
        testBuiltInShortForm(XSDVocabulary.DATE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_G_year_month() {
        testBuiltInShortForm(XSDVocabulary.G_YEAR_MONTH);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_G_year() {
        testBuiltInShortForm(XSDVocabulary.G_YEAR);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_G_month_day() {
        testBuiltInShortForm(XSDVocabulary.G_MONTH_DAY);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_G_day() {
        testBuiltInShortForm(XSDVocabulary.G_DAY);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_G_month() {
        testBuiltInShortForm(XSDVocabulary.G_MONTH);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Unsigned_short() {
        testBuiltInShortForm(XSDVocabulary.UNSIGNED_SHORT);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Unsigned_byte() {
        testBuiltInShortForm(XSDVocabulary.UNSIGNED_BYTE);
    }


    @Test
    public void shouldReturnBuiltInRenderingFor_OWL_Prefix() {
        testPrefixRendering(Namespaces.OWL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_RDFS_Prefix() {
        testPrefixRendering(Namespaces.RDFS);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_RDF_Prefix() {
        testPrefixRendering(Namespaces.RDF);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_XSD_Prefix() {
        testPrefixRendering(Namespaces.XSD);
    }

    @Test
    public void shouldReturnBuiltInRenderingFor_SWRL_Prefix() {
        testPrefixRendering(Namespaces.SWRL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SWRLB_Prefix() {
        testPrefixRendering(Namespaces.SWRLB);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SKOS_Prefix() {
        testPrefixRendering(Namespaces.SKOS);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_GRDDL_Prefix() {
        testPrefixRendering(Namespaces.GRDDL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_MA_Prefix() {
        testPrefixRendering(Namespaces.MA);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_PROV_Prefix() {
        testPrefixRendering(Namespaces.PROV);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_RDFA_Prefix() {
        testPrefixRendering(Namespaces.RDFA);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_RIF_Prefix() {
        testPrefixRendering(Namespaces.RIF);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_R2RML_Prefix() {
        testPrefixRendering(Namespaces.R2RML);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SD_Prefix() {
        testPrefixRendering(Namespaces.SD);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SKOSXL_Prefix() {
        testPrefixRendering(Namespaces.SKOSXL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_POWDER_Prefix() {
        testPrefixRendering(Namespaces.POWDER);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_VOID_Prefix() {
        testPrefixRendering(Namespaces.VOID);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_POWDERS_Prefix() {
        testPrefixRendering(Namespaces.POWDERS);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_XHV_Prefix() {
        testPrefixRendering(Namespaces.XHV);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_ORG_Prefix() {
        testPrefixRendering(Namespaces.ORG);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_GLDP_Prefix() {
        testPrefixRendering(Namespaces.GLDP);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_CNT_Prefix() {
        testPrefixRendering(Namespaces.CNT);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_DCAT_Prefix() {
        testPrefixRendering(Namespaces.DCAT);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_EARL_Prefix() {
        testPrefixRendering(Namespaces.EARL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_HT_Prefix() {
        testPrefixRendering(Namespaces.HT);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_PTR_Prefix() {
        testPrefixRendering(Namespaces.PTR);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_CC_Prefix() {
        testPrefixRendering(Namespaces.CC);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_CTAG_Prefix() {
        testPrefixRendering(Namespaces.CTAG);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_DCTERMS_Prefix() {
        testPrefixRendering(Namespaces.DCTERMS);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_DC_Prefix() {
        testPrefixRendering(Namespaces.DC);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_FOAF_Prefix() {
        testPrefixRendering(Namespaces.FOAF);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_GR_Prefix() {
        testPrefixRendering(Namespaces.GR);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_ICAL_Prefix() {
        testPrefixRendering(Namespaces.ICAL);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_OG_Prefix() {
        testPrefixRendering(Namespaces.OG);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_REV_Prefix() {
        testPrefixRendering(Namespaces.REV);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SIOC_Prefix() {
        testPrefixRendering(Namespaces.SIOC);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_VCARD_Prefix() {
        testPrefixRendering(Namespaces.VCARD);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SCHEMA_Prefix() {
        testPrefixRendering(Namespaces.SCHEMA);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_GEO_Prefix() {
        testPrefixRendering(Namespaces.GEO);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_SC_Prefix() {
        testPrefixRendering(Namespaces.SC);
    }

    @Test
    public void shouldReturnBuiltInRenderingFor_DBPEDIA_Prefix() {
        testPrefixRendering(Namespaces.DBPEDIA);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_DBP_Prefix() {
        testPrefixRendering(Namespaces.DBP);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_DBO_Prefix() {
        testPrefixRendering(Namespaces.DBO);
    }



    @Test
    public void shouldReturnBuiltInRenderingFor_YAGO_Prefix() {
        testPrefixRendering(Namespaces.YAGO);
    }



    private void testPrefixRendering(Namespaces prefix) {
        OWLEntity entity = mock(OWLEntity.class);
        when(entity.isBuiltIn()).thenReturn(true);
        when(entity.getIRI()).thenReturn(IRI.create(prefix.getPrefixIRI() + "frag"));
        assertThat(shortFormProvider.getShortForm(entity), equalTo(prefix.getPrefixName() + ":frag"));
    }


    private <V extends HasIRI & HasPrefixedName> void testBuiltInShortForm(V vocabulary) {
        OWLEntity entity = mock(OWLEntity.class);
        when(entity.isBuiltIn()).thenReturn(true);
        when(entity.getIRI()).thenReturn(vocabulary.getIRI());
        assertThat(shortFormProvider.getShortForm(entity), equalTo(vocabulary.getPrefixedName()));
    }

}
