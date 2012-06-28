package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author z.khan
 */
public class OpenIdData implements Serializable {
	private String name;
	
	private List<String> openIdList;
	private List<String> openIdAccId;
	private List<String> openIdProvider;
	
	public OpenIdData() {
	}
	
	public OpenIdData(String name, List<String> openIdList,List<String> openIdAccId,List<String> openIdProvider) {
		this.name = name;
		this.openIdList = openIdList;
		this.openIdAccId = openIdAccId;
		this.openIdProvider = openIdProvider;
	}
	
    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

    public List<String> getOpenIdList() {
        return openIdList;
    }

    public void setOpenIdList(List<String> openIdList) {
        this.openIdList = openIdList;
    }
    
    public List<String> getOpenIdAccId() {
        return openIdAccId;
    }

    public void setOpenIdAccId(List<String> openIdAccId) {
        this.openIdAccId = openIdAccId;
    }

    public List<String> getOpenIdProvider() {
        return openIdProvider;
    }

    public void setOpenIdProvider(List<String> openIdProvider) {
        this.openIdProvider = openIdProvider;
    }
	
}
