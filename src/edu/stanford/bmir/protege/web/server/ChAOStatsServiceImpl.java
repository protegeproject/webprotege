package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.ChAOStatsService;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.bmir.protegex.chao.change.api.Change;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Transaction;

public class ChAOStatsServiceImpl extends RemoteServiceServlet implements ChAOStatsService {
	private static final long serialVersionUID = -5245955561070741941L;

	private Project getProject(String projectName) {
        return ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
    }

	private KnowledgeBase getChAOKb(String projectName) {
        Project prj = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (prj == null) {
            return null;
        }
        return ChAOKbManager.getChAOKb(prj.getKnowledgeBase());
    }

	public void applyChangeFilter(String projectName, List<String> authors) {
		Project project = getProject(projectName);
        if(project != null) {
        	KnowledgeBase kb = project.getKnowledgeBase();

        	ChangeCache.getInstance(kb).applyFilter(authors);
        }
	}

	public Collection<String> getChangeAuthors(String projectName) {
    	List<String> authorList = new ArrayList<String>();

    	Project project = getProject(projectName);
        if(project == null) {
        	return authorList;
        }
        KnowledgeBase kb = project.getKnowledgeBase();

        authorList = ChangeCache.getInstance(kb).getAuthorList();

        return authorList;
	}

	public PaginationData<ChangeData> getCompositeChanges(String projectName,
			String entityName, List<String> authors, int start, int limit, String sort, String dir) {
        ArrayList<ChangeData> changeData = (ArrayList<ChangeData>) getCompositeChanges(projectName, entityName, authors);
        return PaginationServerUtil.pagedRecords(changeData, start, limit, sort, dir);
      }

	public Integer getNumChanges(String projectName, String entityName) {
		Integer result = 0;
    	KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return result;
        }
        Frame frame = getProject(projectName).getKnowledgeBase().getFrame(entityName);
        if (frame == null) {
            return result; // TODO: rather throw exception?
        }

        Project project = getProject(projectName);
        if(project == null) {
        	return result;
        }
        KnowledgeBase kb = project.getKnowledgeBase();

        // get result from the cache
        result = ChangeCache.getInstance(kb).getChangeCount(frame);

        return result;
	}

	public Integer getNumChildrenChanges(String projectName, String entityName) {
		Integer result = 0;
    	KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return result;
        }
        Frame frame = getProject(projectName).getKnowledgeBase().getFrame(entityName);
        if (frame == null) {
            return result; // TODO: rather throw exception?
        }

        Project project = getProject(projectName);
        if(project == null) {
        	return result;
        }
        KnowledgeBase kb = project.getKnowledgeBase();

        // get result from the cache
        result = ChangeCache.getInstance(kb).getChildrenChangeCount(frame);

        return result;
	}

	private Collection<ChangeData> getCompositeChanges(String projectName, String entityName, List<String> authors) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return changeData;
        }
        Frame frame = getProject(projectName).getKnowledgeBase().getFrame(entityName);
        if (frame == null) {
            return changeData; // TODO: rather throw exception?
        }

        List<Change> displayedChanges = new LinkedList<Change>();
        Collection<Change> changes = ChangeCache.getInstance(getProject(projectName).getKnowledgeBase()).getChanges(frame);
        if(changes != null) {
        	for(Change change : changes) {
        		String author = change.getAuthor();
        		if(author == null || author.length() == 0) {
        			author = ChangeCache.DEFAULT_AUTHOR_LABEL;
        		}
        		if(authors == null || authors.contains(author)) {
        			displayedChanges.add(change);
        		}
        	}
        }

        return getChangeData(displayedChanges);
    }

	private Collection<ChangeData> getChangeData(Collection<Change> changes) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        if (changes != null) {
            for (Change change : changes) {
                ChangeData data = new ChangeData();
                data.setAuthor(change.getAuthor());
                data.setDescription(getChangeDescription(change.getContext()));
                data.setTimestamp(change.getTimestamp().getDateParsed());
                changeData.add(data);
            }
        }
        return changeData;
    }

	private String getChangeDescription(String text) {
        if (text == null) {
            return "No details";
        }
        int index = text.indexOf(Transaction.APPLY_TO_TRAILER_STRING);
        if (index > 0) {
            return text.substring(0, index);
        }
        return text;
    }
}
