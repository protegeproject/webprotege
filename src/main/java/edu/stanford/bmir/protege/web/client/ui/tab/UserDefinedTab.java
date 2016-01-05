package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;

public class UserDefinedTab extends AbstractTab {

	@Inject
	public UserDefinedTab(@Assisted TabId tabId) {
		super(tabId);
	}
}
