package ch.unifr.digits;

import ch.unifr.digits.webprotege.attestation.server.OntologyAttestationService;
import org.apache.jasper.tagplugins.jstl.core.Import;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.time.Duration;
import java.util.*;

import static ch.unifr.digits.FileSupport.saveMeasurementsSeries;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OntologyLoadTest {

    private static final int NUM_RUNS = 20;
    private static final String RESULTS_DIR = "results/";
    private static final Map<String, String> testOntologyFiles = new HashMap<>();
    private static final Map<String, OWLOntology> loadedOntologies = new HashMap<>();
    private static final Measurements measurements = new Measurements();
    private static final List<Runner> serviceRunners = new ArrayList<>();

    static {
        testOntologyFiles.put("biomodels", "ontologies/biomodels-21.owl");
        testOntologyFiles.put("dron", "ontologies/dron-full.owl");
        testOntologyFiles.put("gexo", "ontologies/gexo.rdf");
        testOntologyFiles.put("iobc", "ontologies/IOBC_1_4_0.ttl");
        testOntologyFiles.put("rh-mesh", "ontologies/mesh.owl");
        testOntologyFiles.put("ncit", "ontologies/ncit.owl");
        testOntologyFiles.put("nifstd", "ontologies/nif.ttl");
        testOntologyFiles.put("reto", "ontologies/reto.rdf");
        testOntologyFiles.put("rexo", "ontologies/rexo.rdf");
        testOntologyFiles.put("upheno", "ontologies/upheno.owl");

        serviceRunners.add(new Runner.HashRunner(new OntologyAttestationService(), measurements));
//        serviceRunners.add(new Runner.OntologyAttest(new OntologyAttestationService(), measurements, false));
    }

//    @BeforeAll
//    public static void beforeAll() throws Exception {
//        for (Map.Entry<String, String> entry : testOntologyFiles.entrySet()) {
//            OWLOntology owlOntology = FileSupport.loadOntologyFromResources(entry.getValue());
//            loadedOntologies.put(entry.getKey(), owlOntology);
//            int entities = owlOntology.getSignature(Imports.INCLUDED).size();
//            int classes = owlOntology.getClassesInSignature(Imports.INCLUDED).size();
//            System.out.println(entry.getKey() + "[numEntities="+ entities +"][numClasses=" + classes + "]");
//        }
//    }

    @Test
    public void batch() throws Exception {

        for (Map.Entry<String, String> entry : testOntologyFiles.entrySet()) {
            OWLOntology owlOntology = loadOntology(entry.getKey(), entry.getValue());
            for (Runner runner : serviceRunners) {
                for (int i = 0; i < NUM_RUNS; i++) {
                    runner.execute(entry.getKey(), owlOntology);
                }
                saveMeasurementsSeries(RESULTS_DIR+runner.name()+".csv", entry.getKey(),
                        measurements.getSeries(constructSeriesName(entry.getKey(), runner.name(), "hash"))
                                .stream().map(Duration::toNanos).map(String::valueOf), NUM_RUNS);
//                saveMeasurementsSeries(RESULTS_DIR+runner.name()+"-attest.csv", entry.getKey(),
//                        measurements.getSeries(constructSeriesName(entry.getKey(), runner.name(), "attest"))
//                                .stream().map(Duration::toNanos).map(String::valueOf), NUM_RUNS);
//                saveMeasurementsSeries(RESULTS_DIR+runner.name()+"-gas.csv", entry.getKey(),
//                        measurements.getManualSeries(constructSeriesName(entry.getKey(), runner.name(), "gas"))
//                                .stream().map(String::valueOf), NUM_RUNS);
            }
            System.gc();
        }

    }

    private static final String constructSeriesName(String id, String serviceName, String tag) {
        return id + "-" + serviceName + "-" + tag;
    }

    private static OWLOntology loadOntology(String key, String path) throws Exception {
        OWLOntology owlOntology = FileSupport.loadOntologyFromResources(path);
        int signature = owlOntology.getSignature(Imports.INCLUDED).size();
        int classes = owlOntology.getClassesInSignature(Imports.INCLUDED).size();
        int axioms = owlOntology.getAxioms(Imports.INCLUDED).size();
        int annotations = owlOntology.getAnnotations().size();
        int all = signature+annotations+axioms;
        System.out.println(key + "[numAxioms="+ axioms +"][numAnnotations="+ annotations +"][numSignature="+ signature +"][numClasses=" + classes + "][numEntities=" + all + "]");
        return owlOntology;
    }

    private interface Runner {
        void execute(String id, OWLOntology ontology) throws Exception;
        String name();

        class OntologyAttest implements Runner {

            private final OntologyAttestationService service;
            private final Measurements measurements;
            private final boolean skipBlc;

            OntologyAttest(OntologyAttestationService service, Measurements measurements, boolean skipBlc) {
                this.service = service;
                this.measurements = measurements;
                this.skipBlc = skipBlc;
            }

            @Override
            public void execute(String id, OWLOntology ontology) throws Exception {
                String iri = ontology.getOntologyID().getOntologyIRI().get().toString();
                String versionIri = ontology.getOntologyID().getVersionIRI().or(IRI.create("")).toString();
                int ticket = measurements.begin();
                String ontologyHash = service.ontologyHash(ontology);
                measurements.finish(constructSeriesName(id, name(), "hash"), ticket);

                if (skipBlc) return;
                System.out.println("attest " + name() + " " + iri + " " + versionIri + " " + ontologyHash);

                ticket = measurements.begin();
                TransactionReceipt receipt = service.attest(iri, versionIri, "John doe", String.valueOf(ontologyHash), null);
                measurements.finish(constructSeriesName(id, name(), "attest"), ticket);
                // gas cost is depends on set of call parameters and state of contract, first call is more expensive
                measurements.manualMeasurement("attest-gas", receipt.getGasUsed().longValue());
                System.out.println(receipt.getGasUsed());
            }

            @Override
            public String name() {
                return service.getClass().getSimpleName().toLowerCase();
            }
        }

        class HashRunner implements Runner {

            private final OntologyAttestationService service;
            private final Measurements measurements;

            HashRunner(OntologyAttestationService service, Measurements measurements) {
                this.service = service;
                this.measurements = measurements;
            }

            @Override
            public void execute(String id, OWLOntology ontology) throws Exception {
//                String iri = ontology.getOntologyID().getOntologyIRI().get().toString();
//                String versionIri = ontology.getOntologyID().getVersionIRI().or(IRI.create("")).toString();
                int ticket = measurements.begin();
                String ontologyHash = service.ontologyHash(ontology);
                measurements.finish(constructSeriesName(id, name(), "hash"), ticket);
            }

            @Override
            public String name() {
                return "hashing";
            }
        }
    }
}

