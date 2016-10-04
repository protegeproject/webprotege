package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.notes.api.NoteStoreException;
import edu.stanford.bmir.protege.web.shared.notes.Note;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.notes.NoteType;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public class ChAOBasedNotesStore { // implements MutableNoteStore {


    private static final OWLObjectProperty annotatesProperty;

    private static final OWLDataProperty createdAtProperty;

    private static final OWLDataProperty authoredByProperty;

    private static final OWLDataProperty subjectProperty;

    private static final OWLDataProperty bodyProperty;


    private static final Set<OWLEntity> notesOntologyVocabulary;

    public static final NoteType DEFAULT_NOTE_TYPE = NoteType.COMMENT;


    static {
        OWLDataFactory df = new OWLDataFactoryImpl();
        annotatesProperty = df.getOWLObjectProperty(ChAOVocabulary.ANNOTATES.getIRI());
        createdAtProperty = df.getOWLDataProperty(ChAOVocabulary.CREATED_AT.getIRI());
        authoredByProperty = df.getOWLDataProperty(ChAOVocabulary.AUTHOR.getIRI());
        subjectProperty = df.getOWLDataProperty(ChAOVocabulary.SUBJECT.getIRI());
        bodyProperty = df.getOWLDataProperty(ChAOVocabulary.BODY.getIRI());

        Set<OWLEntity> vocabulary = new HashSet<OWLEntity>(10);
        vocabulary.add(annotatesProperty);
        vocabulary.add(createdAtProperty);
        vocabulary.add(authoredByProperty);
        vocabulary.add(annotatesProperty);
        vocabulary.add(subjectProperty);
        vocabulary.add(bodyProperty);
        notesOntologyVocabulary = Collections.unmodifiableSet(vocabulary);
    }

    public void removeNote(NoteId note) throws NoteStoreException {
    }

    private OWLDataFactory df;

    private OWLOntology domainOntology;

    private OWLOntology notesOntology;

    private MalformedNoteHandler malformedNoteHandler = new QuietMalformedNoteHandler();


    public ChAOBasedNotesStore(OWLOntology domainOntology, OWLOntology notesOntology) {
        this.domainOntology = domainOntology;
        this.notesOntology = notesOntology;
        OWLOntologyManager notesOntologyManager = notesOntology.getOWLOntologyManager();
        df = notesOntologyManager.getOWLDataFactory();
//        noteStoreIndex = new NoteStoreIndex(this);
    }

//    public void addNote(Note note) throws NoteStoreException {
//        try {
//            List<OWLOntologyChange> changes = getAddNoteChanges(note);
//            notesOntology.getOWLOntologyManager().applyChanges(changes);
//            noteStoreIndex.addNote(note);
//        }
//        catch (OWLOntologyChangeException e) {
//            throw new NoteStoreException(e, note);
//        }
//    }
//
//    public void removeNote(Note note) throws NoteStoreException {
//        try {
//            List<OWLOntologyChange> changes = getRemoveNoteChanges(note.getNoteId());
//            notesOntology.getOWLOntologyManager().applyChanges(changes);
//            noteStoreIndex.removeNote(note);
//        }
//        catch (OWLOntologyChangeException e) {
//            throw new NoteStoreException(e, note);
//        }
//    }
//
//    public Iterator<Note> iterator() {
//        return parseNotesFromOntology().iterator();
//    }
//
//    public int getNoteCount() {
//        return noteStoreIndex.getNoteCount();
//    }
//
//    public boolean containsNote(NoteId noteId) {
//        return noteStoreIndex.containsNote(noteId);
//    }
//
//    public Note getNote(NoteId noteId) {
//        return noteStoreIndex.getNote(noteId);
//    }
//
//
//    public boolean hasReplies(InReplyToId inReplyToId) {
//        return noteStoreIndex.hasReplies(inReplyToId);
//    }
//
//    public List<Note> getReplies(InReplyToId inReplyToId) {
//        return noteStoreIndex.getReplies(inReplyToId);
//    }
//
//    public Set<IRI> getIRIsWithReplies() {
//        return noteStoreIndex.getIRIsWithReplies();
//    }
//
//    public int getIRIsWithRepliesCount() {
//        return noteStoreIndex.getRepliesToIRIsCount();
//    }
//
//    public InReplyToId getInReplyToId(NoteId noteId) {
//        Note note = noteStoreIndex.getNote(noteId);
//        if(note == null) {
//            return null;
//        }
//        return note.getInReplyToId();
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Vocabulary management
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean isNotesVocabulary(OWLEntity entity) {
        return notesOntologyVocabulary.contains(entity);
    }

    public boolean isNotesVocabulary(OWLObjectPropertyExpression propertyExpression) {
        return isNotesVocabulary((OWLEntity) propertyExpression.getNamedProperty());
    }

    public boolean isNotesVocabulary(OWLDataPropertyExpression propertyExpression) {
        return isNotesVocabulary((OWLEntity) propertyExpression.asOWLDataProperty());
    }


    private OWLObjectProperty getAnnotatesProperty() {
        return annotatesProperty;
    }

    private OWLDataProperty getCreatedAtProperty() {
        return createdAtProperty;
    }

    private OWLDataProperty getAuthorProperty() {
        return authoredByProperty;
    }

    private OWLDataProperty getSubjectProperty() {
        return subjectProperty;
    }

    private OWLDataProperty getBodyProperty() {
        return bodyProperty;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Change generation
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public List<OWLOntologyChange> getReplaceNoteChanges(NoteId existingNoteId, Note replaceWith) {
        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();
        changeList.addAll(getRemoveNoteChanges(existingNoteId));
        changeList.addAll(getAddNoteChanges(replaceWith));
        return changeList;
    }

    public List<OWLOntologyChange> getAddNoteChanges(Note note) {
        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();
        for(OWLAxiom axiom : renderNoteIntoAxioms(note)) {
            changeList.add(new AddAxiom(notesOntology, axiom));
        }
        return changeList;
    }

    public List<OWLOntologyChange> getRemoveNoteChanges(NoteId noteId) {
        OWLNamedIndividual noteIndividual = getNoteNamedIndividual(noteId);
        if(!notesOntology.containsIndividualInSignature(noteIndividual.getIRI())) {
            return Collections.emptyList();
        }
        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();
        for(OWLObjectPropertyAssertionAxiom ax : notesOntology.getObjectPropertyAssertionAxioms(noteIndividual)) {
            if(isNotesVocabulary(ax.getProperty())) {
                changeList.add(new RemoveAxiom(notesOntology, ax));
            }
        }
        for(OWLDataPropertyAssertionAxiom ax : notesOntology.getDataPropertyAssertionAxioms(noteIndividual)) {
            if(isNotesVocabulary(ax.getProperty())) {
                changeList.add(new RemoveAxiom(notesOntology, ax));
            }
        }
        for(OWLClassAssertionAxiom ax : notesOntology.getClassAssertionAxioms(noteIndividual)) {
            changeList.add(new RemoveAxiom(notesOntology, ax));
        }
        return changeList;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Parsing of notes from ontology
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Set<Note> parseNotesFromOntology() {
        Set<Note> parsedNotes = new HashSet<Note>();
        for(OWLNamedIndividual individual : notesOntology.getIndividualsInSignature()) {
            if(isNoteIndividual(individual)) {
                NoteId noteId = NoteId.createNoteIdFromLexicalForm(individual.getIRI().toString());
                try {
                    Note parsedNote = parseNoteFromOntology(noteId);
                    parsedNotes.add(parsedNote);
                }
                catch (MalformedNoteException e) {
                    malformedNoteHandler.handleMalformedNote(noteId, notesOntology, e);
                }
            }
        }
        return parsedNotes;
    }

    public Note parseNoteFromOntology(NoteId noteId) throws MalformedNoteException {
        NoteId inReplyToId = parseInReplyToIdFromOntology(noteId);
        long createdAt = parseCreatedAtFromOntology(noteId);
        UserId authoredBy = parseAuthoredByFromOntology(noteId);
        NoteType noteType = parseNoteTypeFromOntology(noteId);
        Optional<String> subject = parseSubjectFromOntology(noteId);
        String body = parseBodyFromOntology(noteId);
        return Note.createNote(noteId, Optional.fromNullable(inReplyToId), createdAt, authoredBy, noteType, subject, body);
    }

    private NoteType parseNoteTypeFromOntology(NoteId noteId) throws MalformedNoteException {
//        OWLNamedIndividual noteIndividual = getNoteNamedIndividual(noteId);
//        Set<OWLClass> namedTypes = getNamedTypes(noteIndividual);
//        if(namedTypes.isEmpty()) {
//            return DEFAULT_NOTE_TYPE;
//        }
//        OWLClass noteTypeClass;
//        if(namedTypes.size() > 1) {
//            // Now what? Select the first NAMED one in lexicographic order
//            List<OWLClass> sortedClassExpressions = new ArrayList<OWLClass>(namedTypes);
//            Collections.sort(sortedClassExpressions);
//            noteTypeClass = sortedClassExpressions.get(0);
//        }
//        else {
//            noteTypeClass = namedTypes.iterator().next();
//        }
        return NoteType.COMMENT;
    }

    private NoteId parseInReplyToIdFromOntology(NoteId noteId) {
        OWLNamedIndividual inReplyToNamedIndividual = getSingleValuedObjectPropertyValue(noteId, getAnnotatesProperty());
        if(isNoteIndividual(inReplyToNamedIndividual)) {
            return NoteId.createNoteIdFromLexicalForm(inReplyToNamedIndividual.getIRI().toString());
        }
        else {
            return NoteId.createNoteIdFromLexicalForm(inReplyToNamedIndividual.getIRI().toString());
        }
    }

    private long parseCreatedAtFromOntology(NoteId noteId) throws MalformedNoteException {
        return getSingleValuedDataPropertyLongValue(noteId, getCreatedAtProperty());
    }

    private UserId parseAuthoredByFromOntology(NoteId noteId) throws MalformedNoteException {
        String authorId = getSingleValuedDataPropertyStringValue(noteId, getAuthorProperty());
        return UserId.getUserId(authorId);
    }

    private Optional<String> parseSubjectFromOntology(NoteId noteId) {
        return Optional.fromNullable(getOptionalSingleValuedDataPropertyStringValue(noteId, getSubjectProperty(), null));
    }

    private String parseBodyFromOntology(NoteId noteId) {
        return getOptionalSingleValuedDataPropertyStringValue(noteId, getBodyProperty(), "");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////  Rendering of Notes into Axioms
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Set<OWLAxiom> renderNoteIntoAxioms(Note note) {

        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();


        OWLNamedIndividual noteIndividual = getNoteNamedIndividual(note);
        OWLNamedIndividual inReplyToIndividual = getInReplyToNamedIndividual(note);
        axioms.add(df.getOWLObjectPropertyAssertionAxiom(annotatesProperty, noteIndividual, inReplyToIndividual));


        axioms.add(df.getOWLDataPropertyAssertionAxiom(createdAtProperty, noteIndividual, note.getTimestamp()));
        axioms.add(df.getOWLDataPropertyAssertionAxiom(authoredByProperty, noteIndividual, note.getAuthor().getUserName()));
        axioms.add(df.getOWLDataPropertyAssertionAxiom(subjectProperty, noteIndividual, note.getSubject()));
        axioms.add(df.getOWLDataPropertyAssertionAxiom(bodyProperty, noteIndividual, note.getBody()));

        IRI typeIRI = IRI.create(org.protege.notesapi.notes.NoteType.Comment.name());
        OWLClass typeClass = df.getOWLClass(typeIRI);
        axioms.add(df.getOWLClassAssertionAxiom(typeClass, noteIndividual));


        return axioms;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private boolean isNoteIndividual(OWLNamedIndividual inReplyToNamedIndividual) {
        return !domainOntology.containsEntityInSignature(inReplyToNamedIndividual.getIRI(), true);
    }

    private OWLNamedIndividual getInReplyToNamedIndividual(NoteId inReplyToId) {
        IRI inReplyToIndividualIRI = IRI.create(inReplyToId.getLexicalForm());
        return df.getOWLNamedIndividual(inReplyToIndividualIRI);
    }

    private OWLNamedIndividual getInReplyToNamedIndividual(Note note) {
        return getInReplyToNamedIndividual(note.getNoteId());
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////  Helper methods
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the {@link OWLNamedIndividual} that acts as the main node for a note.
     * @param note The note.  Not null.
     * @return The corresponding individual for the note.  Not null. Be aware that this individual may, or may not,
     * be in the signature of the notes ontology.
     */
    private OWLNamedIndividual getNoteNamedIndividual(Note note) {
        return getNoteNamedIndividual(note.getNoteId());
    }

    /**
     * Gets the {@link OWLNamedIndividual} that acts as the main node for a note.
     * @param noteId The id of the note.  Not null.
     * @return The corresponding individual for the note.  Not null.  Be aware that this individual may, or may not,
     * be in the signature of the notes ontology.
     */
    private OWLNamedIndividual getNoteNamedIndividual(NoteId noteId) {
        IRI noteIndividualIRI = IRI.create(noteId.getLexicalForm());
        return df.getOWLNamedIndividual(noteIndividualIRI);
    }



    private OWLNamedIndividual getSingleValuedObjectPropertyValue(NoteId noteId, OWLObjectProperty property) {
        OWLNamedIndividual noteIndividual = getNoteNamedIndividual(noteId);
        Collection<OWLIndividual> values = EntitySearcher.getObjectPropertyValues(noteIndividual, property, notesOntology);
        if(values.isEmpty()) {
            throw new FoundZeroButExpectedOneObjectPropertyValue(noteId, property);
        }
        if(values.size() > 1) {
            throw new FoundMultipleButExpectedOneObjectPropertyValue(noteId, property);
        }
        OWLIndividual value = values.iterator().next();
        if(value.isAnonymous()) {
            throw new FoundAnonymousButExpectedNamedIndividualException(noteId, property, value.asOWLAnonymousIndividual());
        }
        return value.asOWLNamedIndividual();
    }


    private OWLLiteral getSingleValuedDataPropertyValue(NoteId noteId, OWLDataProperty property) {
        OWLLiteral value = getOptionalSingleValuedDataPropertyValue(noteId, property, null);
        if(value == null) {
            throw new FoundZeroButExpectedOneDataPropertyValueException(noteId, property);
        }
        return value;
    }

    private OWLLiteral getOptionalSingleValuedDataPropertyValue(NoteId noteId, OWLDataProperty property, OWLLiteral defaultValue) {
        OWLNamedIndividual noteIndividual = getNoteNamedIndividual(noteId);
        Collection<OWLLiteral> values = EntitySearcher.getDataPropertyValues(noteIndividual, property, notesOntology);
        if(values.isEmpty()) {
            return defaultValue;
        }
        if(values.size() > 1) {
            throw new FoundMultipleButExpectedOneDataPropertyValueException(noteId, property);
        }
        return values.iterator().next();
    }

    private String getSingleValuedDataPropertyStringValue(NoteId noteId, OWLDataProperty dataProperty) {
        String value = getOptionalSingleValuedDataPropertyStringValue(noteId, dataProperty, null);
        if(value == null) {
            throw new FoundZeroButExpectedOneDataPropertyValueException(noteId, dataProperty);
        }
        return value;
    }

    private String getOptionalSingleValuedDataPropertyStringValue(NoteId noteId, OWLDataProperty dataProperty, String defaultValue) {
        OWLLiteral literal = getOptionalSingleValuedDataPropertyValue(noteId, dataProperty, null);
        if(literal == null) {
            return defaultValue;
        }
        return literal.getLiteral();
    }

    private Long getOptionalSingleValuedDataPropertyLongValue(NoteId noteId, OWLDataProperty dataProperty, Long defaultValue) {
        OWLLiteral literal = getOptionalSingleValuedDataPropertyValue(noteId, dataProperty, null);
        if(literal == null) {
            return defaultValue;
        }
        String trimmedLexicalValue = literal.getLiteral().trim();
        return Long.parseLong(trimmedLexicalValue);
    }

    private Long getSingleValuedDataPropertyLongValue(NoteId noteId, OWLDataProperty dataProperty) {
        Long value = getOptionalSingleValuedDataPropertyLongValue(noteId, dataProperty, null);
        if(value == null) {
            throw new FoundZeroButExpectedOneDataPropertyValueException(noteId, dataProperty);
        }
        return value;
    }

    private Set<OWLClass> getNamedTypes(OWLNamedIndividual noteIndividual) {
        Collection<OWLClassExpression> types = EntitySearcher.getTypes(noteIndividual, notesOntology);
        Set<OWLClass> namedTypes = new HashSet<>(types.size());
        for(OWLClassExpression classExpression : types) {
            if(!classExpression.isAnonymous()) {
                namedTypes.add(classExpression.asOWLClass());
            }
        }
        return namedTypes;
    }

//
//    public static void main(String[] args) throws Exception {
//        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//        OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/Users/matthewhorridge/Desktop/2013.04.26.OPL_annotation.owl"));
//        ChAOBasedNotesStore ns = new ChAOBasedNotesStore(ont, ont);
//        Set<Note> notes = ns.parseNotesFromOntology();
//        for(Note note : notes) {
//            System.out.println(note);
//        }
//    }
}
