package edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.rpc.ChAOStatsServiceManager;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public class ChangeTreeFilterWindow extends Window {
	// list displaying authors that should have their changes filtered
	private ListBox filteredAuthorList;
	
	// list displaying authors who's changes should be shown
	private ListBox displayedAuthorList;
	
	private Button applyButton;
	
	private String projectName;
	
	public ChangeTreeFilterWindow(String projectName) {
		this.projectName = projectName;
		
		init();
	}
	
	public List<String> getDisplayedAuthors() {
		List<String> authors = new ArrayList<String>(displayedAuthorList.getItemCount());
		for(int i = 0; i < displayedAuthorList.getItemCount(); i++) {
			authors.add(displayedAuthorList.getItemText(i));
		}
		
		return authors;
	}
	
	public void addFilterListener(ButtonListener listener) {
		applyButton.addListener(listener);
	}
	
	@Override
	public void show() {
		super.show();
		
		ChAOStatsServiceManager.getInstance().getChangeAuthors(projectName, new AsyncCallback<Collection<String>>() {
			public void onSuccess(Collection<String> authors) {
				displayedAuthorList.clear();
				for(String author : authors) {
					int i = 0;
					for(; i < filteredAuthorList.getItemCount(); i++) {
						if(filteredAuthorList.getItemText(i).equals(author)) break;
					}
					
					// did not find the author, then it should not be filtered
					if(i == filteredAuthorList.getItemCount()) {
						displayedAuthorList.addItem(author);
					}
				}
			}
			
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void init() {
		setTitle("Filter change tree");  
        setWidth(600);  
        setHeight(400);  
        setLayout(new FitLayout());  
        setPaddings(5);  
        setButtonAlign(Position.CENTER);  
        setCloseAction(Window.HIDE);  
        setPlain(true);  

        applyButton = new Button("Apply");
        addButton(applyButton);
        
        Button cancelButton = new Button("Close", new ButtonListenerAdapter() {
        	public void onClick(Button button, EventObject e) {
        		hide();
			}
        });
        addButton(cancelButton);
        
        add(getAuthorFilterPanel());
	}
	
	private Panel getAuthorFilterPanel() {
		Panel panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.setLayout(new FitLayout());

		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());

		displayedAuthorList = new ListBox(true);
		filteredAuthorList = new ListBox(true);
		
		ChAOStatsServiceManager.getInstance().getChangeAuthors(projectName, new AsyncCallback<Collection<String>>() {
			public void onSuccess(Collection<String> authors) {
				for(String author : authors) {
					displayedAuthorList.addItem(author);
				}
			}
			
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		Panel displayedAuthorPanel = new Panel();
		displayedAuthorPanel.setTitle("Displayed authors:");
		displayedAuthorPanel.setLayout(new FitLayout());
		displayedAuthorPanel.setWidth(200);
		displayedAuthorPanel.add(displayedAuthorList);

		Panel filteredAuthorPanel = new Panel();
		filteredAuthorPanel.setLayout(new FitLayout());
		filteredAuthorPanel.setTitle("Filtered authors:");
		filteredAuthorPanel.setWidth(200);
		filteredAuthorPanel.add(filteredAuthorList);

		BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
		eastData.setMinSize(175);
		eastData.setMaxSize(400);
		eastData.setMargins(new Margins(0, 0, 1, 0));

		BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
		westData.setMinSize(175);
		westData.setMaxSize(400);
		westData.setMargins(new Margins(0, 0, 1, 0));

		Button moveRight = new Button(">>", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				swapAuthors(displayedAuthorList, filteredAuthorList);
			}
		});
		Button moveLeft = new Button("<<", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				swapAuthors(filteredAuthorList, displayedAuthorList);
			}
		});
		
		Panel buttonContainerPanel = new Panel();
		buttonContainerPanel.setLayout(new FitLayout());
		
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		Panel moveRightPanel = new Panel();
		moveRightPanel.add(moveRight);
		
		Panel moveLeftPanel = new Panel();
		moveLeftPanel.add(moveLeft);
		
		buttonPanel.add(moveRightPanel);
		buttonPanel.add(moveLeftPanel);
		
		buttonPanel.setCellHeight(moveRightPanel, "40");
		
		buttonContainerPanel.add(buttonPanel);

		borderPanel.add(buttonContainerPanel, new BorderLayoutData(RegionPosition.CENTER));
		borderPanel.add(displayedAuthorPanel, westData);
		borderPanel.add(filteredAuthorPanel, eastData);
		
		Panel centerPanel = new Panel();  
		centerPanel.setHtml("<p>center panel</p>");  
		centerPanel.setBodyStyle("background-color:#C3D9FF");  
		
		//borderPanel.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER)); 

		panel.add(borderPanel);

		return panel;
	}
	
	private void swapAuthors(ListBox source, ListBox target) {
		for(int i = 0; i < source.getItemCount(); i++) {
			if(source.isItemSelected(i)) {
				target.addItem(source.getItemText(i));
				source.removeItem(i);
				i--;
			}
		}
	}
}
