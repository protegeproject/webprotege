package edu.stanford.bmir.protege.web.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thoughtworks.xstream.XStream;

import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationService;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.smi.protege.util.Log;

public class ProjectConfigurationServiceImpl extends RemoteServiceServlet implements ProjectConfigurationService {
    //private static final Logger log = Log.getLogger(ProjectConfigurationServiceImpl.class);

    private static final long serialVersionUID = -2875415014621934377L;

    private static final String PROJECT_CONFIG_DIR = "projectConfigurations";

    private static String configurationFilePrefix = null;

    public static final String PROJECT_CONFIGURATION_FILE_NAME_PREFIX = "configuration";

    public static final String PROJECT_CONFIGURATION_FILE_EXTENSION = ".xml";

    public static final String DEFAULT_OBO_CONFIGURATION_FILE_NAME = "default-obo-configuration";

    public static final String DEFAULT_OWL_CONFIGURATION_FILE_NAME = "default-owl-configuration";


    private static File getDefaultConfigurationsDirectory() {
        return new File(PROJECT_CONFIG_DIR);
    }

    private static File getProjectConfigurationsDirectory(ProjectId projectId) {
        OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
        return documentStore.getConfigurationsDirectory();
    }
    
    private static File getUserProjectConfigurationFile(ProjectId projectId, UserId userId) {
        return new File(getProjectConfigurationsDirectory(projectId), PROJECT_CONFIGURATION_FILE_NAME_PREFIX + "_" + projectId.getId() + "_" + userId.getUserName() + PROJECT_CONFIGURATION_FILE_EXTENSION);
    }
    
    private static File getCustomProjectConfigurationFile(ProjectId projectId) {
        return new File(getProjectConfigurationsDirectory(projectId), PROJECT_CONFIGURATION_FILE_NAME_PREFIX + "_" + projectId.getId() + PROJECT_CONFIGURATION_FILE_EXTENSION);
    }
    
    private static File getDefaultProjectConfigurationFile(ProjectId projectId) {
        return new File(getDefaultConfigurationsDirectory(), getDefaultProjectConfigurationName(projectId) + PROJECT_CONFIGURATION_FILE_EXTENSION);
    }
    

//    private static String getProjectConfigPrefix() {
//        if (configurationFilePrefix == null) {
//            configurationFilePrefix = getProjectConfigurationDirectory() + File.separator + PROJECT_CONFIGURATION_FILE_NAME_PREFIX;
//        }
//        return configurationFilePrefix;
//    }



    public static File getConfigurationFile(String projectName, String userName) {
        ProjectId projectId = ProjectId.get(projectName);
        UserId userId = UserId.getUserId(userName);

        File configFile = getUserProjectConfigurationFile(projectId, userId);
        if (!configFile.exists()) {
            configFile = getCustomProjectConfigurationFile(projectId);
        }
        if (!configFile.exists()) {
            configFile = getDefaultProjectConfigurationFile(projectId);
        }
        if (Log.getLogger().isLoggable(Level.FINE)) {
            Log.getLogger().fine("Path to project configuration file: " + configFile);
        }
        return configFile;
    }

    private static String getDefaultProjectConfigurationName(ProjectId projectId) {
        OWLAPIProjectMetadataManager metadataManager = OWLAPIProjectMetadataManager.getManager();
        OWLAPIProjectType projectType = metadataManager.getType(projectId);
        return getDefaultProjectConfigurationFileName(projectType);
    }
    
    private static String getDefaultProjectConfigurationFileName(OWLAPIProjectType projectType) {
        // TODO: Make configurable via a properties file or something similar
        if(projectType.equals(OWLAPIProjectType.getOBOProjectType())) {
            return DEFAULT_OBO_CONFIGURATION_FILE_NAME;
        }
        else {
            return DEFAULT_OWL_CONFIGURATION_FILE_NAME;
        }
    }


//    public static File getProjectAndUserConfigurationFile(String projectName, String userName) {
//        return new File(getProjectConfigPrefix() + "_" + projectName + "_" + userName + ".xml");
//    }


    public ProjectLayoutConfiguration getProjectLayoutConfiguration(String projectName, String userName) {
        ProjectLayoutConfiguration config = null;

        File f = getConfigurationFile(projectName, userName);

        if (!f.exists()) {
            Log.getLogger().severe("Installation misconfigured: Default project configuration file missing: " + f);
            throw new IllegalStateException("Misconfiguration");
        }
        try {
            FileReader fileReader = new FileReader(f);
            config = convertXMLToConfiguration(fileReader);
            fileReader.close();
        }
        catch (java.io.FileNotFoundException e) {
            config = new ProjectLayoutConfiguration();
        }
        catch (java.io.IOException e) {
            Log.getLogger().log(Level.WARNING, "Failed to read from config file at server. ", e);
        }

        config.setOntologyName(projectName);

        return config;
    }


    public void saveProjectLayoutConfiguration(String projectName, String userName, ProjectLayoutConfiguration config) {
        String xml = convertConfigDetailsToXML(config);
        File f = getUserProjectConfigurationFile(ProjectId.get(projectName), UserId.getUserId(userName));
        f.getParentFile().mkdirs();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(xml);
            bw.close();
        }
        catch (IOException e) {
            Log.getLogger().log(Level.WARNING, "Failed to write to config file. ", e);
        }

    }

    public List<ProjectType> getProjectTypes() {
        return Arrays.asList(new ProjectType(OWLAPIProjectType.getDefaultProjectType().getProjectTypeName()), new ProjectType(OWLAPIProjectType.getOBOProjectType().getProjectTypeName()));
    }


    public String convertConfigDetailsToXML(ProjectLayoutConfiguration config) {
        XStream xstream = new XStream();
        xstream.alias("project", ProjectLayoutConfiguration.class);
        xstream.alias("tab", TabConfiguration.class);
        xstream.alias("portlet", PortletConfiguration.class);
        xstream.alias("column", TabColumnConfiguration.class);
        xstream.alias("project", ProjectLayoutConfiguration.class);
        return xstream.toXML(config);
    }

    public ProjectLayoutConfiguration convertXMLToConfiguration(Reader reader) {
        XStream xstream = new XStream();
        xstream.alias("project", ProjectLayoutConfiguration.class);
        xstream.alias("tab", TabConfiguration.class);
        xstream.alias("portlet", PortletConfiguration.class);
        xstream.alias("column", TabColumnConfiguration.class);
        xstream.alias("map", LinkedHashMap.class);
        ProjectLayoutConfiguration config = (ProjectLayoutConfiguration) xstream.fromXML(reader);
        return config;
    }

}
