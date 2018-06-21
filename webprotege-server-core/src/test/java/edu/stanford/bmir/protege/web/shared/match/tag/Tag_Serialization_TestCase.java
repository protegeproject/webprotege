package edu.stanford.bmir.protege.web.shared.match.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 */
public class Tag_Serialization_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider(new OWLDataFactoryImpl()).get();
    }

    @Test
    public void shouldSerializeTag() throws Exception {
        Tag tag = Tag.get(TagId.createTagId(),
                          ProjectId.get("12345678-1234-1234-1234-123456789abc"),
                          "The label",
                          "The description",
                          Color.getRGB(10, 20, 30),
                          Color.getRGB(200, 210, 220),
                          ImmutableList.of(EntityIsNotDeprecatedCriteria.get()));
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, tag);
        Tag readTag = objectMapper.readValue(new StringReader(sw.toString()), Tag.class);
        MatcherAssert.assertThat(readTag, is(tag));
    }
}
