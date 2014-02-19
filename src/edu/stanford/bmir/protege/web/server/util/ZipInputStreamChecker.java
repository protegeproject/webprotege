package edu.stanford.bmir.protege.web.server.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class ZipInputStreamChecker {

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_0 = 'P';

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_1 = 'K';

    public boolean isZipFile(File file) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            return isZipInputStream(bufferedInputStream);
        } finally {
            bufferedInputStream.close();
        }
    }

    public boolean isZipInputStream(BufferedInputStream in) throws IOException {
        in.mark(2);
        char ch0 = (char) in.read();
        char ch1 = (char) in.read();
        in.reset();
        return ch0 == ZIP_FILE_MAGIC_NUMBER_BYTE_0 && ch1 == ZIP_FILE_MAGIC_NUMBER_BYTE_1;
    }
}
