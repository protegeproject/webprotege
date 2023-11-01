package edu.stanford.bmir.protege.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    /**
     * Read contents of text file resource from <code>src/test/resources</code>.
     * Entire content read into memory, so best for small files.
     * 
     * @param resourceName
     * @return text content and assuming UTF-8 encoding
     * @throws IOException
     */
    public static String readResourceTestFile(String resourceName) throws IOException {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	URI resource;
	try {
	    resource = classLoader.getResource(resourceName).toURI();
	} catch (URISyntaxException e) {
	    String msg = String.format("Can't read '%s'", resourceName);
	    throw new IOException(msg, e);
	}
	byte[] rawContent = Files.readAllBytes(Paths.get(resource));

	return new String(rawContent, StandardCharsets.UTF_8);
    }
}
