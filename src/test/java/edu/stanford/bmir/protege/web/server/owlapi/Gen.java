package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public class Gen {

    public static void main(String[] args) {
        for(Namespaces v : Namespaces.values()) {
            System.out.println("@Test\n" +
                    "    public void shouldReturnBuiltInRenderingFor_" + v.name() + "_Prefix() {\n" +
                    "        testPrefixRendering(Namespaces." + v.name() + ");\n" +
                    "    }\n\n\n");
        }
    }
}
