package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.client.place.*;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.Namespaces;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class ProjectViewPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectViewPlace> {

    private static final String PROJECTS = "projects/";

    private static final String EDIT = "/edit/";

    private static final String SELECTION = "?selection=";

    private static RegExp regExp = RegExp.compile(PROJECTS + "(.{36})" + EDIT + "([^\\?]*)(\\" + SELECTION + "(.*))?" );

    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public Class<ProjectViewPlace> getPlaceClass() {
        return ProjectViewPlace.class;
    }

    public ProjectViewPlace getPlace(String token) {
        GWT.log("[ProjectViewPlaceTokenizer] Parsing: " + token);
        token = URL.decode(token);
        GWT.log("[ProjectViewPlaceTokenizer] Decoded: " + token);

        MatchResult result = regExp.exec(token);
        GWT.log("[ProjectViewPlaceTokenizer] MatchResult: " + result);
        String projectId = result.getGroup(1);
        String perspectiveId = result.getGroup(2);
        String selectionString = result.getGroup(4);

        GWT.log("[ProjectViewPlaceTokenizer] Parsed: ProjectId: " + projectId);
        GWT.log("[ProjectViewPlaceTokenizer] Parsed: PerspectiveId: " + perspectiveId);
        GWT.log("[ProjectViewPlaceTokenizer] Parsed: Selection: " + selectionString);
        ProjectViewPlace.Builder builder = new ProjectViewPlace.Builder(ProjectId.get(projectId), new PerspectiveId(perspectiveId));

        if(selectionString != null) {
            ItemTokenizer tokenizer = new ItemTokenizer();
            List<ItemToken> tokenList = tokenizer.parseTokens(selectionString);
            for(ItemToken t : tokenList) {
                OWLDataFactoryImpl dataFactory = new OWLDataFactoryImpl();
                ItemTokenParser parser = new ItemTokenParser();
                DefaultPrefixManager prefixManager = new DefaultPrefixManager();
                prefixManager.setPrefix("owl:", Namespaces.OWL.getPrefixIRI());
                List<Item<?>> entity = parser.parse(t, new DefaultItemTypeMapper(dataFactory, prefixManager));
                for(Item<?> item : entity) {
                    builder.withSelectedItem(item);
                }
            }
        }
        return builder.build();
    }

    public String getToken(ProjectViewPlace place) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROJECTS);
        sb.append(place.getProjectId().getId());
        sb.append(EDIT);
        sb.append(place.getPerspectiveId().getId());

        List<ItemToken> itemTokens = Lists.newArrayList();
        for(Item<?> item : place.getItemSelection()) {
            String typeName = item.getAssociatedType().getName();
            itemTokens.add(new ItemToken(typeName, item.getItem().toString()));
        }
        if (!itemTokens.isEmpty()) {
            String rendering = new ItemTokenizer().renderTokens(itemTokens);
            sb.append(SELECTION);
            sb.append(rendering);
        }

        return sb.toString();
    }
}
