package edu.stanford.bmir.protege.web.server.util;

import static edu.stanford.bmir.protege.web.server.util.JavaUtil.cast;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner.Silent;

import edu.stanford.bmir.protege.web.TestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@RunWith(Silent.class)
public class TypelessJSONSerialization_TestCase {
    private ProjectId projectId = ProjectId.get("c4e39f8f-d2b3-4212-8888-28fabd2aa5ac");

    @Test
    public void testJSONSerialization() throws Exception {
	// test basic round-trip deserialize/serialize

	String testSettings = TestUtils
		.readResourceTestFile("edu/stanford/bmir/protege/web/server/util/project_settings_notags.json");
	// out.println(testSettings);
	Map<String, Object> data = TypelessJSONSerialization.deserializeJSON(testSettings);

	String newSettings = TypelessJSONSerialization.serializeToJSON(data, true);

	assertEquals(StringUtils.deleteWhitespace(testSettings), StringUtils.deleteWhitespace(newSettings));
	// out.println(newSettings);
    }

    @Test
    public void testJSONSerializationNameValueReplacement() throws Exception {
	String testSettings = TestUtils
		.readResourceTestFile("edu/stanford/bmir/protege/web/server/util/project_settings_notags.json");

	String newSettings = TypelessJSONSerialization.resplaceAllStringValue(testSettings, "projectId",
		projectId.getId());
	Map<String, Object> data = TypelessJSONSerialization.deserializeJSON(newSettings);

	String id = projectId.getId();
	Map<String, Object> projectSettings = cast(data.get("projectSettings"));
	Map<String, Object> sharingSettings = cast(data.get("sharingSettings"));
	Map<String, Object> searchSettings = cast(data.get("searchSettings"));
	List<Map<String, Object>> searchFilters = cast(searchSettings.get("searchFilters"));

	assertEquals(id, projectSettings.get("projectId"));
	assertEquals(id, sharingSettings.get("projectId"));
	assertEquals(id, searchSettings.get("projectId"));
	assertEquals(id, searchFilters.get(0).get("projectId"));
    }
}
