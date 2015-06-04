package edu.stanford.bmir.protege.web.server.project;

import com.thoughtworks.xstream.XStream;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.server.UIConfigurationManagerException;
import edu.stanford.bmir.protege.web.server.inject.DefaultUiConfigurationDirectory;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectSpecificUiConfigurationDataDirectory;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
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

    private static final String CONFIGURATION_FILE_NAME = "ui-configuration.xml";

    private final DefaultUIConfigurationFileManager defaultUIConfigurationFileManager;

    private final ProjectId projectId;

    private final ProjectDetailsManager projectDetailsManager;

    private final File projectSpecificUiConfigurationDirectory;

    @Inject
    public UIConfigurationManager(ProjectId projectId,
                                  ProjectDetailsManager projectDetailsManager,
                                  DefaultUIConfigurationFileManager defaultUIConfigurationFileManager,
                                  @ProjectSpecificUiConfigurationDataDirectory File projectSpecificUiConfigurationDirectory) {
        this.projectId = projectId;
        this.projectDetailsManager = projectDetailsManager;
        this.defaultUIConfigurationFileManager = defaultUIConfigurationFileManager;
        this.projectSpecificUiConfigurationDirectory = projectSpecificUiConfigurationDirectory;
    }


    public ProjectLayoutConfiguration getProjectLayoutConfiguration(UserId userId) throws UIConfigurationManagerException {
        try {
            InputStream inputStream = getUIConfigurationInputStream(userId);
            ProjectLayoutConfiguration configuration = convertXMLToConfiguration(inputStream);
            inputStream.close();
            return configuration;
        }
        catch (IOException e) {
            throw new UIConfigurationManagerException(e.getMessage());
        }
    }


    public void saveProjectLayoutConfiguration(UserId userId, ProjectLayoutConfiguration config) throws UIConfigurationManagerException {
        try {
            OutputStream outputStream = getUIConfigurationOutputStream(userId);
            convertConfigDetailsToXML(config, outputStream);
            outputStream.close();
        }
        catch (IOException e) {
            throw new UIConfigurationManagerException(e.getMessage());
        }
    }


    /**
     * Gets an input stream which can be used to read the UI configuration for the specified projectId and userId.
     * @param userId The {@link UserId} of the current user.  Not {@code null}.  May correspond to the guest user
     * (see {@link edu.stanford.bmir.protege.web.shared.user.UserId#getGuest()}.
     * @return An {@link InputStream} which can be used to read the UI configuration for the specified user and
     *         specified project.  Not {@code null}.
     * @throws NullPointerException if {@code projectId} or {@code userId} is {@code null}.
     */
    private InputStream getUIConfigurationInputStream(UserId userId) throws IOException {
        checkNotNull(userId);
        return new BufferedInputStream(new FileInputStream(getUIConfigurationFile(userId)));
    }


    private File getUIConfigurationFile(UserId userId) {
        final File projectUserFile = getProjectAndUserSpecificConfigurationFile(userId);
        if (projectUserFile.exists()) {
            return projectUserFile;
        }

        final File projectFile = getProjectSpecificUiConfigurationFile();
        if (projectFile.exists()) {
            return projectFile;
        }

        OWLAPIProjectType projectType = projectDetailsManager.getType(projectId);
        return defaultUIConfigurationFileManager.getDefaultConfigurationFile(projectType);
    }

    private OutputStream getUIConfigurationOutputStream(UserId userId) throws IOException {
        checkNotNull(userId);
        final File configurationFile = getProjectAndUserSpecificConfigurationFile(userId);
        configurationFile.getParentFile().mkdirs();
        return new BufferedOutputStream(new FileOutputStream(configurationFile));
    }

    private File getProjectAndUserSpecificConfigurationFile(UserId userId) {
        File projectUserConfigurationDirectory = new File(projectSpecificUiConfigurationDirectory, getEscapedUserIdName(userId));
        return new File(projectUserConfigurationDirectory, CONFIGURATION_FILE_NAME);
    }

    private static String getEscapedUserIdName(UserId userId) {
        return userId.getUserName();
    }

    private File getProjectSpecificUiConfigurationFile() {
        return new File(projectSpecificUiConfigurationDirectory, CONFIGURATION_FILE_NAME);
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

    public ProjectLayoutConfiguration convertXMLToConfiguration(InputStream in) {
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
