package edu.stanford.bmir.protege.web.server.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class ZipFileExtractor {

    public ZipFileExtractor() {
    }

    public void extractFileToDirectory(File zip, File outputDirectory) throws IOException {
        ZipFile zipFile = new ZipFile(zip);
        Enumeration<? extends ZipEntry> zipEntryEnumeration = zipFile.entries();
        while(zipEntryEnumeration.hasMoreElements()) {
            extractZipEntry(outputDirectory, zipFile, zipEntryEnumeration);
        }
    }

    private void extractZipEntry(File outputDirectory,
                                 ZipFile zipFile,
                                 Enumeration<? extends ZipEntry> zipEntryEnumeration) throws IOException {
        ZipEntry entry = zipEntryEnumeration.nextElement();
        String entryName = entry.getName();
        File entryFile = new File(outputDirectory, entryName);
        File entryParentFile = entryFile.getParentFile();
        entryParentFile.mkdirs();
        BufferedOutputStream entryOutputStream = new BufferedOutputStream(new FileOutputStream(entryFile));
        InputStream entryInputStream = zipFile.getInputStream(entry);
        IOUtils.copy(entryInputStream, entryOutputStream);
        entryInputStream.close();
        entryOutputStream.close();
    }
}
