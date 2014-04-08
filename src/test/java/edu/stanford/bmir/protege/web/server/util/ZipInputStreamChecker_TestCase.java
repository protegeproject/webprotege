package edu.stanford.bmir.protege.web.server.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedInputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ZipInputStreamChecker_TestCase {

    @Mock
    private BufferedInputStream bufferedInputStream;

    @Test
    public void shouldTestZipInputStream() throws IOException {
        when(bufferedInputStream.read())
                .thenReturn((int) 'P')
                .thenReturn((int) 'K');
        ZipInputStreamChecker checker = new ZipInputStreamChecker();
        assertTrue(checker.isZipInputStream(bufferedInputStream));
    }

    @Test
    public void shouldTestNonZipInputStream() throws IOException {
        when(bufferedInputStream.read())
                .thenReturn((int) 'X')
                .thenReturn((int) 'Y');
        ZipInputStreamChecker checker = new ZipInputStreamChecker();
        assertFalse(checker.isZipInputStream(bufferedInputStream));
    }
}
