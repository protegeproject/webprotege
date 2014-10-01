package edu.stanford.bmir.protege.web.server.bioportal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bioontology.ontologies.api.Client;
import org.bioontology.ontologies.api.http.HTTPOptions;
import org.bioontology.ontologies.api.models.NCBOClass;
import org.bioontology.ontologies.api.models.NCBOOntology;
import org.bioontology.ontologies.api.pages.NCBOPage;
import org.bioontology.ontologies.api.util.SearchOptions;

import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.BioPortalResultBean;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.BioPortalSearchResultsBean;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.exceptions.DetailsNotFoundException;

/**
 * @author marcosmr
 * 
 */
public class BioPortalServicesUtil {

	private static Client bpServicesClient;
	private static Map<String, NCBOOntology> bpOntologies;
	private static long ontLoadedTime;
	private static final long ontRefreshPeriod = 24 * 60 * 60 * 1000; // 24h to milliseconds

	static {
		bpServicesClient = new Client(new HTTPOptions(
				BioPortalConstants.DEFAULT_API_KEY));
	}

	// This method keeps a cache of ontologies that is refreshed periodically
	private static Map<String, NCBOOntology> getOntologies() {
		long time = new Date().getTime();
		if ((bpOntologies == null) || (time - ontLoadedTime > ontRefreshPeriod)) {
			// Get all ontologies from BioPortal and store them for future use
			bpOntologies = new HashMap<String, NCBOOntology>();
			for (NCBOOntology ont : bpServicesClient.getAll(NCBOOntology.class)) {
				if (ont.getId() != null)
					bpOntologies.put(ont.getId(), ont);
			}
			ontLoadedTime = new Date().getTime();
		}
		return bpOntologies;
	}

	public static BioPortalSearchResultsBean search(String text,
			int pageNum, int pageSize) throws InterruptedException {
		List<BioPortalResultBean> results = new ArrayList<BioPortalResultBean>();		

		SearchOptions searchOpts = new SearchOptions(pageNum, pageSize, false,
				null, false, true, false, null, null, null);

		// NCBO BioPortal REST Services client
		NCBOPage<NCBOClass> page = bpServicesClient.search(text, searchOpts);
		
		for (NCBOClass c : page.getCollection()) {			
			String id = c.getId();
			String shortId = getLastFragmentOfUri(id);
			String idSelf = c.getLinks().get("self").toString();
			String preferredName = c.getPrefLabel();
			String uriBp = c.getLinks().get("ui").toString();
			NCBOOntology ont = getOntologies().get(
					c.getLinks().get("ontology").toString());
			String ontologyId = ont.getId();
			String ontologyAcronym = ont.getAcronym();
			String ontologyName = ont.getName();
			String ontologyUriBp = ont.getLinks().get("ui").toString();
			String matchingField = c.getMatchType();
			
			BioPortalResultBean result = new BioPortalResultBean(
					id, shortId, idSelf, preferredName, uriBp, ontologyId,
					ontologyAcronym, ontologyName, ontologyUriBp, matchingField);

			results.add(result);
		}
		BioPortalSearchResultsBean searchResults = 
				new BioPortalSearchResultsBean(page.getPageNum(), page.getNextPageNum(), page.getTotalPages(), getNumberOfResults(text), results);
		
		return searchResults;
	}
	
	public static int getNumberOfResults(String text) {
		// The total number of results is the number of pages that contain one result
		SearchOptions searchOpts = new SearchOptions(1, 1, false, null, false,
				true, false, null, null, null);
		NCBOPage<NCBOClass> page = bpServicesClient.search(text, searchOpts);
		return page.getTotalPages();
	}

	public static LinkedHashMap<String, List<String>> getDetailsMapById(
			String classId) throws DetailsNotFoundException {
		LinkedHashMap<String, List<String>> detailsMap = new LinkedHashMap<String, List<String>>();
		LinkedHashMap<String, List<String>> detailsMapTmp = new LinkedHashMap<String, List<String>>();
		// TODO: This option should be managed by the ontologies_api_java_client library
		String includeSuffix = "?include=all";
		NCBOClass c = bpServicesClient.getByID(classId + includeSuffix,
				NCBOClass.class);		
		if (c != null) {
			if (c.getId() != null) {
				detailsMap.put("ID",
						Arrays.asList(getLastFragmentOfUri(c.getId())));
			}
			if (c.getPrefLabel() != null)
				detailsMap.put("Preferred name",
						Arrays.asList(c.getPrefLabel()));
			if ((c.getSynonym() != null) && (c.getSynonym().size() > 0))
				detailsMap.put("Synonyms", c.getSynonym());
			if ((c.getDefinition() != null) && (c.getDefinition().size() > 0))
				detailsMap.put("Definitions", c.getDefinition());

			// All the other properties will be inserted in alphabetical order
			if (c.getId() != null) {
				detailsMapTmp.put("Full ID", Arrays.asList(c.getId()));
			}
			if ((c.getCui() != null) && (c.getCui().size() > 0))
				detailsMapTmp.put("Cuis", c.getCui());
			if ((c.getSemanticType() != null)
					&& (c.getSemanticType().size() > 0))
				detailsMapTmp.put("Semantic types", c.getSemanticType());
			if ((c.getProperties() != null) && (c.getProperties().size() > 0)) {
				for (Map.Entry<String, List<String>> entry : c.getProperties()
						.entrySet()) {
					detailsMapTmp.put(getLastFragmentOfUri(entry.getKey()),
							entry.getValue());
				}
			}
			// Sort alphabetically
			List<Map.Entry<String, List<String>>> entries = new ArrayList<Map.Entry<String, List<String>>>(
					detailsMapTmp.entrySet());
			Collections.sort(entries,
					new Comparator<Map.Entry<String, List<String>>>() {
						public int compare(Map.Entry<String, List<String>> a,
								Map.Entry<String, List<String>> b) {
							return a.getKey().compareToIgnoreCase(b.getKey());
						}
					});
			detailsMapTmp = new LinkedHashMap<String, List<String>>();
			for (Map.Entry<String, List<String>> entry : entries) {
				detailsMapTmp.put(entry.getKey(), entry.getValue());
			}
			detailsMap.putAll(detailsMapTmp);

			// BioPortal page
			if (c.getLinks().get("self") != null)
				detailsMap.put("BioPortalPage",
						Arrays.asList(c.getLinks().get("ui").toString()));
		}
		else {
			throw new DetailsNotFoundException("Details not found in BioPortal");
		}
		return detailsMap;
	}

	public static String getHtmlLink(String text) {
		String output = text;
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Matcher matcher = Pattern.compile(regex).matcher(text);
		if (matcher.find()) {
			String linkText = text;			
			output = "<a href='" + text + "' target='_blank'>" + linkText
					+ "</a>";
		}
		return output;
	}

	public static String getLastFragmentOfUri(String uri) {
		String shortId = null;
		String[] fragments = uri.split("/");
		shortId = fragments[fragments.length - 1];
		if (shortId.contains("#")) {
			fragments = shortId.split("#");
			shortId = fragments[fragments.length - 1];
		}
		return shortId;
	}
	
}
