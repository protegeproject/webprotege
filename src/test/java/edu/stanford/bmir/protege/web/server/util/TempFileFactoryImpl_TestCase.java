package edu.stanford.bmir.protege.web.server.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class TempFileFactoryImpl_TestCase {

    @Test
    public void shouldCreateTempDirectory() throws IOException {
        TempFileFactoryImpl fileFactory = new TempFileFactoryImpl();
        File dir = fileFactory.createTempDirectory();
        assertThat(dir.exists(), is(true));
        assertThat(dir.isDirectory(), is(true));
        dir.delete();
    }
}
