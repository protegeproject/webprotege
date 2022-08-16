package ch.unifr.digits.webprotege.attestation.server.compression.tree;

import org.semanticweb.owlapi.formats.NTriplesDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class OWLToRDFTranslator {

    private RDFTriple[] triples;

    public OWLToRDFTranslator(OWLOntology ontology) throws OWLOntologyStorageException {
        translate(ontology);
    }

    private void translate(OWLOntology ontology) throws OWLOntologyStorageException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ontology.saveOntology(new NTriplesDocumentFormat(), out);

        byte[] buf = out.toByteArray();
        char[] file = byteArrayToCharArray(buf);
        ArrayList<String> lines = removeComments(getLines(file));

        this.triples = linesToTriples(lines);
    }

    private ArrayList<String> getLines(char[] file) {
        ArrayList<String> lines = new ArrayList<>();
        String file_as_string = String.copyValueOf(file);
        StringTokenizer tokenizer = new StringTokenizer(file_as_string, "\n");
        while (tokenizer.hasMoreTokens()) {
            lines.add(tokenizer.nextToken());
        }
        return lines;
    }

    private ArrayList<String> removeComments(ArrayList<String> lines) {
        ArrayList<String> linesNoComments = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.charAt(0) != '#') {
                linesNoComments.add(line);
            }
        }

        return linesNoComments;
    }


    private char[] byteArrayToCharArray(byte[] buf) {
        char[] arr = new char[buf.length];
        for (int i = 0; i < buf.length; i++) {
            arr[i] = (char) buf[i];
        }
        return arr;
    }

    private RDFTriple[] linesToTriples(ArrayList<String> lines) {
        RDFTriple[] triples = new RDFTriple[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            String subject = tokenizer.nextToken();
            String predicate = tokenizer.nextToken();

            // Compute the "object" as the rest of the string (without including the '.' at the end)
            StringBuilder sb =  new StringBuilder();

            while (tokenizer.hasMoreTokens()) {
                sb.append(tokenizer.nextToken());
            }

            sb.deleteCharAt(sb.length()-1);
            String object = sb.toString();

            RDFTriple triple = new RDFTriple(subject, predicate, object);
            triples[i] = triple;
        }

        return triples;
    }

    public RDFTriple[] getTriples() {
        return triples;
    }

    public void setTriples(RDFTriple[] triples) {
        this.triples = triples;
    }
}
