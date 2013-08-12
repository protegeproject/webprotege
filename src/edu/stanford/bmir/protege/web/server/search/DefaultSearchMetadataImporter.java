package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.search.importer.*;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class DefaultSearchMetadataImporter implements SearchMetadataImporter {

    public SearchMetadataDB getSearchMetadata(final OWLAPIProject project, Set<SearchType> types) {
        SearchMetadataImportContext context = new SearchMetadataImportContext(project);
        SearchMetadataDB db = new SearchMetadataDB();

        getEntityBasedSearchMetadata(types, context, db);
        getAxiomBasedSearchMetadata(types, context, db);
        getOntologyBasedSearchMetadata(types, context, db);

        return db;
    }

    private void getEntityBasedSearchMetadata(Set<SearchType> types, SearchMetadataImportContext context, SearchMetadataDB db) {

        List<EntityBasedSearchMDImporter> importers = getEntityBasedSearchMetadataImporters(types);

        Set<OWLEntity> processed = new HashSet<OWLEntity>();
        for (OWLOntology ontology : context.getOntologies()) {
            for (OWLEntity entity : ontology.getSignature()) {
                if (processed.add(entity)) {
                    getSearchMetadataForEntity(entity, context, db, importers);
                }
            }
        }
    }

    private void getSearchMetadataForEntity(OWLEntity entity, SearchMetadataImportContext context, SearchMetadataDB db, List<EntityBasedSearchMDImporter> importers) {
        String entityRendering = context.getRendering(entity);
        for (EntityBasedSearchMDImporter importer : importers) {
            importer.generateSearchMetadataFor(entity, entityRendering, context, db);
        }
    }


    private void getAxiomBasedSearchMetadata(Set<SearchType> types, SearchMetadataImportContext context, SearchMetadataDB db) {
        for (AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
            getSearchMetadataForAxiomsOfType(axiomType, types, context, db);
        }
    }

    private void getSearchMetadataForAxiomsOfType(AxiomType<?> axiomType, Set<SearchType> types, SearchMetadataImportContext context, SearchMetadataDB db) {
        for (AxiomBasedSearchMetadataImporter importer : getAxiomBasedSearchMetadataImporters(types, axiomType)) {
            for (OWLOntology ontology : context.getOntologies()) {
                for (OWLAxiom ax : ontology.getAxioms(axiomType)) {
                    OWLObject subject = new AxiomSubjectProvider().getSubject(ax);
                    if (subject instanceof OWLEntity) {
                        OWLEntity entSubject = (OWLEntity) subject;
                        String rendering = context.getRendering(entSubject);
                        importer.generateSearchMetadataFor(ax, entSubject, rendering, context, db);
                    }
                }
            }
        }
    }


    private void getOntologyBasedSearchMetadata(Set<SearchType> types, SearchMetadataImportContext context, SearchMetadataDB db) {
        List<OntologyBasedSearchMDImporter> ontologyBasedSearchMDImporters = getOntologyBasedSearchMetadataImporters(types);
        for (OWLOntology ontology : context.getOntologies()) {
            for (OntologyBasedSearchMDImporter importer : ontologyBasedSearchMDImporters) {
                importer.generateSearchMetadata(ontology, context, db);
            }
        }
    }

//
//    /**
//     * A convenience method which gets the styled string rendering for an {@link OWLObject}
//     * @param editorKit The editor kit which should be used to compute rendering for entities embedded within the
//     * object.
//     * @param object The object to be rendered
//     * @return The {@link StyledString} rendering of the object.
//     */
//    private StyledString getStyledStringRendering(final OWLEditorKit editorKit, OWLObject object) {
//        OWLEditorKitShortFormProvider sfp = new OWLEditorKitShortFormProvider(editorKit);
//        OWLEditorKitOntologyShortFormProvider ontologySfp = new OWLEditorKitOntologyShortFormProvider(editorKit);
//        OWLObjectRenderingContext renderingContext = new OWLObjectRenderingContext(sfp, ontologySfp);
//        OWLObjectStyledStringRenderer renderer = new OWLObjectStyledStringRenderer(renderingContext);
//        return renderer.getRendering(object);
//    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<EntityBasedSearchMDImporter> getEntityBasedSearchMetadataImporters(Set<SearchType> types) {
        List<EntityBasedSearchMDImporter> entityBasedSearchMDImporters = new ArrayList<EntityBasedSearchMDImporter>();
        entityBasedSearchMDImporters.add(new DisplayNameSearchMetadataImporter());
        entityBasedSearchMDImporters.add(new EntityIRISearchMetadataImporter());
        entityBasedSearchMDImporters.add(new EntityAnnotationValueSearchMetadataImporter());

        List<EntityBasedSearchMDImporter> result = new ArrayList<EntityBasedSearchMDImporter>();
        for (EntityBasedSearchMDImporter importer : entityBasedSearchMDImporters) {
            if (importer.isImporterFor(types)) {
                result.add(importer);
            }
        }
        return result;
    }

    private List<AxiomBasedSearchMetadataImporter> getAxiomBasedSearchMetadataImporters(Set<SearchType> types, AxiomType<?> axiomType) {
        List<AxiomBasedSearchMetadataImporter> axiomBasedSearchMetadataImporters = new ArrayList<AxiomBasedSearchMetadataImporter>();
        axiomBasedSearchMetadataImporters.add(new AxiomAnnotationSearchMetadataImporter());
        axiomBasedSearchMetadataImporters.add(new LogicalAxiomRenderingSearchMetadataImporter());

        List<AxiomBasedSearchMetadataImporter> result = new ArrayList<AxiomBasedSearchMetadataImporter>();
        for (AxiomBasedSearchMetadataImporter importer : axiomBasedSearchMetadataImporters) {
            if (importer.isImporterFor(axiomType, types)) {
                result.add(importer);
            }
        }
        return result;
    }

    private List<OntologyBasedSearchMDImporter> getOntologyBasedSearchMetadataImporters(Set<SearchType> types) {
        List<OntologyBasedSearchMDImporter> ontologyBasedSearchMDImporters = new ArrayList<OntologyBasedSearchMDImporter>();
        ontologyBasedSearchMDImporters.add(new OntologyAnnotationSearchMetadataImporter());

        List<OntologyBasedSearchMDImporter> result = new ArrayList<OntologyBasedSearchMDImporter>();
        for (OntologyBasedSearchMDImporter importer : ontologyBasedSearchMDImporters) {
            if (importer.isImporterFor(types)) {
                result.add(importer);
            }
        }
        return result;
    }
}
