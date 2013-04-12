package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thoughtworks.xstream.XStream;
import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationService;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.util.Log;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

public class ProjectConfigurationServiceImpl extends RemoteServiceServlet implements ProjectConfigurationService {

    public ProjectLayoutConfiguration getProjectLayoutConfiguration(ProjectId projectId, UserId userId) {
        UIConfigurationManager configurationManager = UIConfigurationManager.get();
        return configurationManager.getProjectLayoutConfiguration(projectId, userId);
    }


    public void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config) {
        UIConfigurationManager.get().saveProjectLayoutConfiguration(projectId, userId, config);
    }
}
