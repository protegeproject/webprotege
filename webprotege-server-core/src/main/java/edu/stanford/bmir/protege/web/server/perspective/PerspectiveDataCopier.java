package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Apr 16
 */
public class PerspectiveDataCopier {

    private static final String DEFAULT_PERSPECTIVE_DATA_RESOURCE_PATH = "/default-perspective-data";

    private static final Logger logger = LoggerFactory.getLogger(PerspectiveDataCopier.class);

    private final File defaultPerspectiveDataDirectory;



    @Inject
    public PerspectiveDataCopier(@Nonnull @DefaultPerspectiveDataDirectory File defaultPerspectiveDataDirectory) {
        this.defaultPerspectiveDataDirectory = defaultPerspectiveDataDirectory;
    }

    public void copyDefaultPerspectiveData() {
        try {
            defaultPerspectiveDataDirectory.mkdirs();
            copyPerspectiveFile("perspective.list.json");
            copyPerspectiveFile("Classes.json");
            copyPerspectiveFile("Properties.json");
            copyPerspectiveFile("Individuals.json");
            copyPerspectiveFile("Comments.json");
            copyPerspectiveFile("Changes by Entity.json");
            copyPerspectiveFile("History.json");
            copyPerspectiveFile("Query.json");
            copyPerspectiveFile("OWL Classes.json");
            copyPerspectiveFile("OWL Properties.json");

//            URL url = getClass().getResource(DEFAULT_PERSPECTIVE_DATA_RESOURCE_PATH);
//            File templateDefaultPerspectiveDataDirectory = new File(url.toURI());
//            defaultPerspectiveDataDirectory.getParentFile().mkdirs();
//            FileUtils.copyDirectory(templateDefaultPerspectiveDataDirectory, defaultPerspectiveDataDirectory, false);
//            logger.info("Copied default perspective data to {} (from {})",
//                        defaultPerspectiveDataDirectory,
//                        templateDefaultPerspectiveDataDirectory);
//            File[] perspectiveFiles = defaultPerspectiveDataDirectory.listFiles();
//            if (perspectiveFiles != null) {
//                for(File file : perspectiveFiles) {
//                    String fileName = file.getName();
//                    if(fileName.endsWith(".json") && !fileName.equals("perspective.list.json")) {
//                        String strippedFileName = fileName.substring(0, fileName.length() - 5);
//                        String hashedFileName = computeMD5(strippedFileName) + ".json";
//                        file.renameTo(new File(defaultPerspectiveDataDirectory, hashedFileName));
//                        logger.info("Installed \"{}\" as {}", fileName, hashedFileName);
//                    }
//                }
//            }
        }
        catch (IOException ioEx) {
            logger.info("There was a problem copying the default perspective data.  " +
                                "Details: {}", ioEx.getMessage());
        }
    }

    private void copyPerspectiveFile(@Nonnull String perspectiveFileName) throws IOException {
        String resourcePath = DEFAULT_PERSPECTIVE_DATA_RESOURCE_PATH + "/" + perspectiveFileName;
        try(BufferedInputStream is = new BufferedInputStream(getClass().getResourceAsStream(resourcePath))) {
            Path outFile;
            if(!perspectiveFileName.equals("perspective.list.json")) {
                String strippedFileName = perspectiveFileName.substring(0, perspectiveFileName.length() - 5);
                String hashedFileName = computeMD5(strippedFileName) + ".json";
                outFile = defaultPerspectiveDataDirectory.toPath().resolve(hashedFileName);
            }
            else {
                outFile = defaultPerspectiveDataDirectory.toPath().resolve(perspectiveFileName);
            }
            FileUtils.copyInputStreamToFile(is, outFile.toFile());
            logger.info("Installed \"{}\" to {}", perspectiveFileName, outFile.toAbsolutePath().toString());
        }

    }

    private static String computeMD5(String name) {
        Md5MessageDigestAlgorithm algorithm = new Md5MessageDigestAlgorithm();
        algorithm.updateWithBytesFromUtf8String(name);
        return algorithm.computeDigestAsBase16Encoding();
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/tmp/testing");
        FileSystem fs = FileSystems.newFileSystem(path, PerspectiveDataCopier.class.getClassLoader());

        fs.getRootDirectories().forEach(root -> {
            try {
                Files.list(root).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
