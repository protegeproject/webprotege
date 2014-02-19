package edu.stanford.bmir.protege.web.server.util;

import edu.stanford.bmir.protege.web.server.util.ZipFileExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class ZipFileExtractor_TestCase {

    private byte[] bytes;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File zipFile;


    @Before
    public void setUp() throws IOException {
        bytes = new byte[100];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = 1;
        }
        zipFile = folder.newFile("zipFile");
    }

    @Test
    public void shouldExtractZipFile() throws IOException {
        OutputStream out = new FileOutputStream(zipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipEntry entryA = new ZipEntry("/FileA");
        entryA.setSize(bytes.length);
        zipOutputStream.putNextEntry(entryA);
        zipOutputStream.write(bytes);
        ZipEntry entryB = new ZipEntry("/Directory/FileB");
        entryB.setSize(bytes.length);
        zipOutputStream.putNextEntry(entryB);
        zipOutputStream.write(bytes);
        zipOutputStream.close();

        ZipFileExtractor extractor = new ZipFileExtractor();
        File outputDirectory = folder.newFolder("out");
        extractor.extractFileToDirectory(zipFile, outputDirectory);
        File extractedFileA = new File(outputDirectory, "FileA");
        assertTrue(extractedFileA.exists());
        assertEquals(extractedFileA.length(), 100);
        File extractedFileB = new File(outputDirectory, "/Directory/FileB");
        assertTrue(extractedFileB.exists());
        assertEquals(extractedFileB.length(), 100);
        File extractedDirectory = new File(outputDirectory, "Directory");
        assertTrue(extractedDirectory.exists());
        assertTrue(extractedDirectory.isDirectory());
    }
}
