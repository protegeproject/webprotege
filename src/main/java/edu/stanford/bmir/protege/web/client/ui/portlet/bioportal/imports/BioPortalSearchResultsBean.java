package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author marcosmr
 * 
 */
public class BioPortalSearchResultsBean implements IsSerializable {

	private int pageNum;
	private int nextpageNum;
	private int totalPages;
	private int numResults;
	private List<BioPortalResultBean> results;

	/**
	 * Empty constructor for serialization purposes only.
	 */
	public BioPortalSearchResultsBean() {
	}

	public BioPortalSearchResultsBean(int pageNum, int nextpageNum,
			int totalPages, int numResults, List<BioPortalResultBean> results) {
		this.pageNum = pageNum;
		this.nextpageNum = nextpageNum;
		this.totalPages = totalPages;
		this.numResults = numResults;
		this.results = results;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getNextpageNum() {
		return nextpageNum;
	}

	public void setNextpageNum(int nextpageNum) {
		this.nextpageNum = nextpageNum;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public List<BioPortalResultBean> getResults() {
		return results;
	}

	public void setResults(List<BioPortalResultBean> results) {
		this.results = results;
	}

}
