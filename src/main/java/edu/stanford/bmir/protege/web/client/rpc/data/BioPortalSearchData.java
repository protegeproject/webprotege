package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * @author Csongor Nyulas
 * @author Marcos Martinez
 */

public class BioPortalSearchData implements Serializable {	
	
	private int pageToLoad;
	private int pageSize;

	public int getPageToLoad() {
		return pageToLoad;
	}

	public void setPageToLoad(int pageToLoad) {
		this.pageToLoad = pageToLoad;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

//    private String bpRestBaseUrl;
//	private String bpRestCallSuffix;
//	private String searchOntologyIds;
//	private String searchOptions;
//	private String searchPageOption;
//	
//	
//	@Override
//	public String toString() {
//		return "BioPortalSearchData [" +
//		        "bpRestBaseUrl=" + bpRestBaseUrl + 
//				"bpRestCallSuffix=" + bpRestCallSuffix +
//				", searchOntologyIds=" + searchOntologyIds + 
//				", searchOptions=" + searchOptions + 
//				", searchPageOption=" + searchPageOption + 
//				"]";
//	}
//
//	public String getBpRestBaseUrl() {
//        return bpRestBaseUrl;
//    }
//    public void setBpRestBaseUrl(String bpRestBaseUrl) {
//        this.bpRestBaseUrl = bpRestBaseUrl;
//    }
//    public String getBpRestCallSuffix() {
//		return bpRestCallSuffix;
//	}
//	public void setBpRestCallSuffix(String bpRestCallSuffix) {
//		this.bpRestCallSuffix = bpRestCallSuffix;
//	}
//	public String getSearchOntologyIds() {
//		return searchOntologyIds;
//	}
//	public void setSearchOntologyIds(String searchOntologyIds) {
//		this.searchOntologyIds = searchOntologyIds;
//	}
//	public String getSearchOptions() {
//	    return searchOptions;
//	}
//	public void setSearchOptions(String searchOptions) {
//	    this.searchOptions = searchOptions;
//	}
//	public String getSearchPageOption() {
//		return searchPageOption;
//	}
//	public void setSearchPageOption(String searchPageOption) {
//		this.searchPageOption = searchPageOption;
//	}

}
