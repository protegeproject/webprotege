package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.bmir.protegex.chao.change.api.Change;
import edu.stanford.bmir.protegex.chao.change.api.ChangeFactory;
import edu.stanford.bmir.protegex.chao.change.api.Composite_Change;
import edu.stanford.bmir.protegex.chao.change.api.impl.DefaultChange;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.impl.DefaultOntology_Component;
import edu.stanford.smi.protege.model.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * Stores a cache of frame to change count references.
 * 
 * @author seanf
 */
public class ChangeCache {
	public static final String DEFAULT_AUTHOR_LABEL = "N/A";
	private static Map<KnowledgeBase, ChangeCache> instances = new HashMap<KnowledgeBase, ChangeCache>();
	
	private List<String> authorList;
	
	// map storing the number of changes associated with a given frame
	private Map<Frame, Integer> frame2ChangeCountMap = new HashMap<Frame, Integer>();
	
	// map of the total number of changes to all children below a given frame
    private Map<Frame, Integer> frame2ChildrenChangeCountMap = new HashMap<Frame, Integer>();
    
    private Map<Frame, List<Change>> frame2ChangesMap = new HashMap<Frame, List<Change>>();
    
    private List<Composite_Change> allChanges;
    
    private Slot authorSlot;
    private Slot partOfChangeSlot;
    private Slot currentNameSlot;
    
    private KnowledgeBase kb;
    
    private ChangeCache(KnowledgeBase kb) {
    	this.kb = kb;
    	
    	this.authorList = new ArrayList<String>();
    	
    	fillChangeChache();
    }
    
    public synchronized static ChangeCache getInstance(KnowledgeBase kb) {
    	ChangeCache changeCache;
    		changeCache = instances.get(kb);
    	
    	if(changeCache == null) {
    		changeCache = new ChangeCache(kb);
    		instances.put(kb, changeCache);
    	}
    	
    	return changeCache;
    }
    
    private void fillChangeChache() {
    	fillChangeChache(null);
    }
    
    private void fillChangeChache(List<String> authorFilter) {
    	KnowledgeBase changeKb = ChAOKbManager.getChAOKb(kb);
    	ChangeFactory factory = new ChangeFactory(changeKb);
    	
    	partOfChangeSlot = changeKb.getSlot("partOfCompositeChange");
    	currentNameSlot = changeKb.getSlot("currentName");
    	authorSlot = changeKb.getSlot("author");
    	
    	allChanges = (List<Composite_Change>)factory.getAllComposite_ChangeObjects();
    	
    	long time = System.currentTimeMillis();
    	
    	//Collection<Change> allChanges = tic.getTopLevelChanges();
    	Set<String> uniqueAuthors = new HashSet<String>();
    	
    	for(Change change : allChanges) {
    		DefaultChange defaultChange = (DefaultChange)change;
    		Instance instance = defaultChange.getWrappedProtegeInstance();
			
			// ignore the change if it is part of a larger composite change
			if(instance.getDirectOwnSlotValue(partOfChangeSlot) != null) continue;
			
			// get the author name from the wrapped instance of the change object
			Object o =  instance.getOwnSlotValue(authorSlot);
			if(o != null) {
				String author = o.toString();
				if(author == null || author.length() == 0) author = DEFAULT_AUTHOR_LABEL;
				
				uniqueAuthors.add(author);
				
				// check filter if it exists
				if((authorFilter != null && authorFilter.contains(author)) || authorFilter == null) {
					processConceptToChangeTerm(kb, defaultChange);
				}
			}
    	}
    	
    	// update parent frames with change values
    	propogateChanges();
    	
    	System.out.println("Done: " + (System.currentTimeMillis() - time));
    	
    	if(authorFilter == null) {
    		authorList.clear();
    		authorList.addAll(uniqueAuthors);
    	}
    }
    
    public void applyFilter(List<String> authors) {
    	frame2ChangeCountMap.clear();
    	frame2ChildrenChangeCountMap.clear();
    	
    	fillChangeChache(authors);
    }
    
    public List<String> getAuthorList() {
    	if(newChangesExist()) loadAuthorList();
    	
    	return authorList;
    }
    
    public List<Change> getChanges(Frame frame) {
    	List<Change> changes = new ArrayList<Change>();
    	if(frame2ChangesMap == null) return changes;
    	
    	changes = frame2ChangesMap.get(frame);
    	if(changes == null) return new ArrayList<Change>();
    	
    	return changes;
    }
    
    public int getChangeCount(Frame frame) {
    	if(frame2ChangeCountMap == null) return 0;
    	
    	Integer result = frame2ChangeCountMap.get(frame);
		if(result == null) return 0;
		
		return result;
    }
    
    public int getChildrenChangeCount(Frame frame) {
    	if(frame2ChildrenChangeCountMap == null) return 0;
    	
		Integer result = frame2ChildrenChangeCountMap.get(frame);
		if(result == null) return 0;
		
		return result;
    }
    
    private void loadAuthorList() {
    	Set<String> uniqueAuthors = new HashSet<String>();
    	
    	for(Change change : allChanges) {
    		DefaultChange defaultChange = (DefaultChange)change;
    		Instance instance = defaultChange.getWrappedProtegeInstance();
			
			// ignore the change if it is part of a larger composite change
			if(instance.getDirectOwnSlotValue(partOfChangeSlot) != null) continue;
			
			// get the author name from the wrapped instance of the change object
			Object o =  instance.getOwnSlotValue(authorSlot);
			if(o != null) {
				String author = o.toString();
				if(author == null || author.length() == 0) author = DEFAULT_AUTHOR_LABEL;
				
				uniqueAuthors.add(author);
			}
    	}
    	
    	authorList.clear();
		authorList.addAll(uniqueAuthors);
    }
    
    private boolean newChangesExist() {
    	KnowledgeBase changeKb = ChAOKbManager.getChAOKb(kb);
        if (changeKb == null){
            return false;
        }
    	ChangeFactory factory = new ChangeFactory(changeKb);
    	List<Composite_Change> changes = (List<Composite_Change>)factory.getAllComposite_ChangeObjects();
    	
    	if(changes.size() != allChanges.size()) {
    		allChanges = changes;
    		
    		return true;
    	}
    	return false;
    }
    
    /**
	 * Recurses up the hierarchy from cls adding the changeCount to the super classes.
	 * 
	 * @param cls
	 * @param changeCount
	 */
	private void propogateChanges(Cls cls, int changeCount, Map<Frame, Integer> conceptToChangeCountMap) {
		Collection<List<Cls>> pathsToRoot = ModelUtilities.getPathsToRoot(cls);
        
        Set<Cls> allParents = new HashSet<Cls>();
        for (List<Cls> path : pathsToRoot) {
            allParents.addAll(path);
        }
        allParents.remove(cls);
        
        for(Cls parent : allParents) {
			Integer count = conceptToChangeCountMap.get(parent);
			if(count == null) {
				count = 0;
			}
			
			count += changeCount;
			conceptToChangeCountMap.put(parent, count);
        }
	}
	
	private void propogateChanges() {
		for(Entry<Frame, Integer> entry : frame2ChangeCountMap.entrySet()) {
			propogateChanges((Cls)entry.getKey(), entry.getValue(), frame2ChildrenChangeCountMap);
		}
	}
    
    private void processConceptToChangeTerm(KnowledgeBase sourceKb, DefaultChange change) {
    	// get the current name of the apply to object
		DefaultOntology_Component ontologyComponent = (DefaultOntology_Component)change.getApplyTo();
		Object o = ontologyComponent.getWrappedProtegeInstance().getDirectOwnSlotValue(currentNameSlot);
		if(o != null) {
			String applyToName = o.toString();
			if(applyToName != null) {
				Frame sourceFrame = sourceKb.getFrame(applyToName);
				
				List<Change> changes = frame2ChangesMap.get(sourceFrame);
				Integer changeCount = frame2ChangeCountMap.get(sourceFrame);
				if(changeCount == null) {
					changeCount = 0;
					changes = new LinkedList<Change>();
					frame2ChangesMap.put(sourceFrame, changes);
				}
				changeCount++;
				changes.add(change);
				frame2ChangeCountMap.put(sourceFrame, changeCount);
			}
		}
    }
}
