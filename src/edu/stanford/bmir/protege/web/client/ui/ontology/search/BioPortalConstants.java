package edu.stanford.bmir.protege.web.client.ui.ontology.search;

import java.util.HashMap;
import java.util.Map;


public class BioPortalConstants {

    public static final String RECORD_TYPE_PREFERRED_NAME = "apreferredname";
    public static final String RECORD_TYPE_CONCEPT_ID = "bconceptid";
    public static final String RECORD_TYPE_SYNONYM = "csynonym";
    public static final String RECORD_TYPE_PROPERTY = "dproperty";

    private static Map<String, String> recordType2PrintName = new HashMap<String, String>();

    static {
        recordType2PrintName.put(RECORD_TYPE_PROPERTY, "In property");
        recordType2PrintName.put(RECORD_TYPE_SYNONYM, "In synonym");
        recordType2PrintName.put(RECORD_TYPE_PREFERRED_NAME, "In preferred term");
        recordType2PrintName.put(RECORD_TYPE_CONCEPT_ID, "In ID");
    }


    public static String WP_BP_USERNAME = "webprotege";
    public static String BP_USER_ID = "bp_user_id";

    //FIXME: these properties should be handled on the server, not client, send null, if nothing

    public static String DEFAULT_BIOPORTAL_URL =  "http://bioportal.bioontology.org/";   //production
    //public static String DEFAULT_BIOPORTAL_URL = "http://stage.bioontology.org/";    //stage

    public static final String BP_ONTOLOGY_STR = "ontologies";
    public static final String BP_VISUALIZE_STR = "visualize";
    static String DEFAULT_BIOPORTAL_VISUALIZE_URL = DEFAULT_BIOPORTAL_URL + BP_VISUALIZE_STR + "/";
    static String DEFAULT_BIOPORTAL_ONTOLOGY_URL = DEFAULT_BIOPORTAL_URL + BP_ONTOLOGY_STR + "/";

    public static String DEFAULT_BIOPORTAL_REST_BASE_URL = "http://rest.bioontology.org/bioportal/";   //production
    //public static String DEFAULT_BIOPORTAL_REST_BASE_URL = "http://stagerest.bioontology.org/bioportal/";    //stage

    public static String DEFAULT_BIOPORTAL_REST_CALL_SUFFIX = "apikey=5a15a173-dac1-4ea4-b2cf-61ac033bb026";    //production - account: webprotege
    //public static String DEFAULT_BIOPORTAL_REST_CALL_SUFFIX = "apikey=5a15a173-dac1-4ea4-b2cf-61ac033bb026 ";    //stage - account: webprotege
    static String DEFAULT_BIOPORTAL_SEARCH_ONE_PAGE_OPTION = "pagesize=10&pagenum=1";

    public static final boolean DEFAULT_IMPORT_FROM_ORIGINAL_ONTOLOGIES = true;

    static String CONFIG_PROPERTY_REFERENCE_CLASS = "reference_class";
    public static String CONFIG_PROPERTY_REFERENCE_PROPERTY = "reference_property";

    static String CONFIG_PROPERTY_URL_PROPERTY = "url_property";

    static String CONFIG_PROPERTY_ONTOLOGY_NAME_PROPERTY = "ontology_name_property";
    static String CONFIG_PROPERTY_ONTOLOGY_NAME_ALT_PROPERTY = "ontology_name_alt_property";
    public static String CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY = "ontology_id_property";

    public static String CONFIG_PROPERTY_CONCEPT_ID_PROPERTY = "concept_id_property";
    static String CONFIG_PROPERTY_CONCEPT_ID_ALT_PROPERTY = "concept_id_property_alt";

    static String CONFIG_PROPERTY_PREFERRED_LABEL_PROPERTY = "preferred_label_property";

    public static String CONFIG_PROPERTY_BIOPORTAL_BASE_URL = "bioportal_base_url";
    public static String CONFIG_PROPERTY_BIOPORTAL_REST_BASE_URL = "bioportal_rest_base_url";
//    static String CONFIG_PROPERTY_BIOPORTAL_SEARCH_URL = "bioportal_search_url";
    public static String CONFIG_PROPERTY_BIOPORTAL_REST_CALL_SUFFIX = "bioportal_rest_call_suffix";

    static String CONFIG_PROPERTY_SEARCH_ONTOLOGY_IDS = "search_ontology_ids";
    static String CONFIG_PROPERTY_SEARCH_OPTIONS = "search_options";
    static String CONFIG_PROPERTY_SEARCH_ONE_PAGE_OPTION = "search_one_page_option";
    static String CONFIG_PROPERTY_DEFAULT_SEARCH_STRING = "default_search_string";

    static String CONFIG_PROPERTY_IMPORT_FROM_ORIGINAL_ONTOLOGY = "import_from_original_ontology";

    static final String SHOW_ALL_BUTTON_TEXT = "Show all search results";
    static final String DNF_BUTTON_TEXT = "Did not find it. Leave a comment";
    static final String DNF_CONCEPT_ID = "Did-Not-Find";
    static final String DNF_CONCEPT_ID_SHORT = "DNF";
    static final String DNF_CONCEPT_LABEL = "(Did Not Find Concept)";
    static final String XML_ELEMENT_NUM_PAGES = "numPages";
    static final String XML_ELEMENT_NUM_RESULTS_TOTAL = "numResultsTotal";


    public static String getRecordTypePrintText(String recType) {
        String text = recordType2PrintName.get(recType);
        return text == null ? "" : text;
    }

}
