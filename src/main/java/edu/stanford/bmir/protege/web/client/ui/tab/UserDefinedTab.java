package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class UserDefinedTab extends AbstractTab {

	@Inject
	public UserDefinedTab(@Assisted TabId tabId, SelectionModel selectionModel) {
		super(tabId, selectionModel);
	}
}
