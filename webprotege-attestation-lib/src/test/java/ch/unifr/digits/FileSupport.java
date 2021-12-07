package ch.unifr.digits;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSupport {
    public static OWLOntology loadOntologyFromResources(String filename) throws OWLOntologyCreationException {
        try (InputStream inputStream = FileSupport.class.getClassLoader().getResourceAsStream(filename)) {
            OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
            OWLOntology owlOntology = ontologyManager.loadOntologyFromOntologyDocument(inputStream);
            return owlOntology;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMeasurementsSeries(String fname, String id, Stream<String> series, int numRuns) throws Exception {
        File file = new File(fname);
        String head = null;
        if (!file.exists()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("id").append(",");
            for (int i = 1; i <= numRuns; i++) {
                buffer.append("run_").append(i).append(",");
            }
            head = buffer.toString();
        }
        FileWriter fileWriter = new FileWriter(file, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        if (head != null) writer.println(head);

        String line = id+",";
        line += series.collect(Collectors.joining(","));
        writer.println(line);
        writer.close();
    }
}
