package edu.stanford.bmir.protege.web.server;

import com.thoughtworks.xstream.XStream;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.*;
import java.util.LinkedHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class UIConfigurationManager {

    private static final String DEFAULT_OBO_CONFIGURATION_FILE_NAME = "default-obo-configuration.xml";

    private static final String DEFAULT_OWL_CONFIGURATION_FILE_NAME = "default-owl-configuration.xml";

    private static final String CONFIGURATION_FILE_NAME = "ui-configuration.xml";


    private static final File DEFAULT_OWL_CONFIGURATION_FILE = new File(WebProtegeFileStore.getInstance().getDefaultUIConfigurationDataDirectory(), DEFAULT_OWL_CONFIGURATION_FILE_NAME);

    private static final File DEFAULT_OBO_CONFIGURATION_FILE = new File(WebProtegeFileStore.getInstance().getDefaultUIConfigurationDataDirectory(), DEFAULT_OBO_CONFIGURATION_FILE_NAME);


    private static final UIConfigurationManager instance = new UIConfigurationManager();

    /**
     * Gets the one and only instance of the {@link UIConfigurationManager}.
     * @return The singleton instance of the {@link UIConfigurationManager}.  Not {@code null}.
     */
    public static UIConfigurationManager get() {
        return instance;
    }


    public ProjectLayoutConfiguration getProjectLayoutConfiguration(ProjectId projectId, UserId userId) throws UIConfigurationManagerException {
        try {
            InputStream inputStream = getUIConfigurationInputStream(projectId, userId);
            ProjectLayoutConfiguration configuration = convertXMLToConfiguration(inputStream, projectId);
            inputStream.close();
            return configuration;
        }
        catch (IOException e) {
            throw new UIConfigurationManagerException(e.getMessage());
        }
    }


    public void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config) throws UIConfigurationManagerException {
        try {
            OutputStream outputStream = getUIConfigurationOutputStream(projectId, userId);
            convertConfigDetailsToXML(config, outputStream);
            outputStream.close();
        }
        catch (IOException e) {
            throw new UIConfigurationManagerException(e.getMessage());
        }
    }


    /**
     * Gets an input stream which can be used to read the UI configuration for the specified projectId and userId.
     * @param projectId The {@link ProjectId} of the project whose UI configuration is to be read.  Not {@code null}.
     * @param userId The {@link UserId} of the current user.  Not {@code null}.  May correspond to the guest user
     * (see {@link edu.stanford.bmir.protege.web.shared.user.UserId#getGuest()}.
     * @return An {@link InputStream} which can be used to read the UI configuration for the specified user and
     *         specified project.  Not {@code null}.
     * @throws NullPointerException if {@code projectId} or {@code userId} is {@code null}.
     */
    private InputStream getUIConfigurationInputStream(ProjectId projectId, UserId userId) throws IOException {
        checkNotNull(projectId);
        checkNotNull(userId);
        return new BufferedInputStream(new FileInputStream(getUIConfigurationFile(projectId, userId)));
    }


    private File getUIConfigurationFile(ProjectId projectId, UserId userId) {
        final File projectUserFile = getConfigurationFile(projectId, userId);
        if (projectUserFile.exists()) {
            return projectUserFile;
        }

        final File projectFile = getConfigurationFile(projectId);
        if (projectFile.exists()) {
            return projectFile;
        }

        OWLAPIProjectType projectType = OWLAPIProjectMetadataManager.getManager().getType(projectId);
        if (projectType.equals(OWLAPIProjectType.getOBOProjectType())) {
            return DEFAULT_OBO_CONFIGURATION_FILE;
        }
        else {
            return DEFAULT_OWL_CONFIGURATION_FILE;
        }

    }

    private OutputStream getUIConfigurationOutputStream(ProjectId projectId, UserId userId) throws IOException {
        checkNotNull(projectId);
        checkNotNull(userId);
        final File configurationFile = getConfigurationFile(projectId, userId);
        configurationFile.getParentFile().mkdirs();
        return new BufferedOutputStream(new FileOutputStream(configurationFile));
    }


    private static File getProjectConfigurationDirectory(ProjectId projectId) {
        final OWLAPIProjectDocumentStore projectDocumentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
        return projectDocumentStore.getConfigurationsDirectory();
    }

    private static File getConfigurationFile(ProjectId projectId, UserId userId) {
        File projectConfigurationDirectory = getProjectConfigurationDirectory(projectId);
        File projectUserConfigurationDirectory = new File(projectConfigurationDirectory, getEscapedUserIdName(userId));
        return new File(projectUserConfigurationDirectory, CONFIGURATION_FILE_NAME);
    }

    private static String getEscapedUserIdName(UserId userId) {
        return userId.getUserName();
    }

    private static File getConfigurationFile(ProjectId projectId) {
        return new File(getProjectConfigurationDirectory(projectId), CONFIGURATION_FILE_NAME);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void convertConfigDetailsToXML(ProjectLayoutConfiguration config, OutputStream outputStream) {
        XStream xstream = new XStream();
        xstream.alias("project", ProjectLayoutConfiguration.class);
        xstream.alias("tab", TabConfiguration.class);
        xstream.alias("portlet", PortletConfiguration.class);
        xstream.alias("column", TabColumnConfiguration.class);
        xstream.alias("project", ProjectLayoutConfiguration.class);
        xstream.toXML(config, outputStream);
    }

    public ProjectLayoutConfiguration convertXMLToConfiguration(InputStream in, ProjectId projectId) {
        XStream xstream = new XStream();
        xstream.alias("project", ProjectLayoutConfiguration.class);
        xstream.alias("tab", TabConfiguration.class);
        xstream.alias("portlet", PortletConfiguration.class);
        xstream.alias("column", TabColumnConfiguration.class);
        xstream.alias("map", LinkedHashMap.class);
        ProjectLayoutConfiguration configuration = (ProjectLayoutConfiguration) xstream.fromXML(in);
        configuration.setProjectId(projectId);
        return configuration;
    }


}
