package edu.stanford.bmir.protege.web.server.notes.converter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/08/2012
 * <p>
 * The purpose of this code is to convert legacy webprotege (based on Protege 3) notes ontologies to the latest
 * notes format.  The converter expects an OWL file which is a dump of instances and assertions based on the
 * P3 CHAO notes ontology.
 * </p>
 * <p>
 * The basic schema of the ChAO ontology is that an IRI :A is annotated with a note as follows, with
 * p: binding to &lt;http://protege.stanford.edu/ontologies/ChAO/changes.owl#&gt;</br>
 * <p/>
 * First, we have a dummy individual that represents the entity (IRI) being annotated.  For classes these are
 * typed with p:Ontology_Class and a data property value pointing to a string corresponding to the IRI of the class.
 * <p/>
 * :i rdf:type p:Ontology_Class<br>
 * :i p:currentName "current name literal - IRI lexical value"<br>
 * <p/>
 * Then we have the "Note Individual" :n<br>
 * and we specify what the note replies to<br>
 * :n p:annotates :i<br>
 * <p/>
 * Specify the various field for the note<br>
 * :n p:createdAt "Created at literal format is mm/dd/yyyy hh:mm:ss z"<br>
 * :n p:author    "Author name literal"<br>
 * :n p:subject "Subject literal" </br>
 * :n p:body "Body literal" .  <br>
 * <p/>
 * </p>
 * Caveats:  For ChAO ontologies exported from WebProtege, the prefix p: may actually (in some cases) bind to
 * a different base.
 */
public class CHAO2NotesConverter {

//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    // Some constants that we need.  Unfortunately, the CHAO dump doesn't have a fixed notes vocabulary as the base
//    // changes on each dump.
//
//    public static final String ONTOLOGY_CLASS_IRI_FRAGMENT = "Ontology_Class";
//
//    public static final String CURRENT_NAME_FRAGMENT = "currentName";
//
//    public static final String ANNOTATES_NAME_FRAGMENT = "annotates";
//
//    public static final String AUTHOR_NAME_FRAGMENT = "author";
//
//    public static final String SUBJECT_NAME_FRAGMENT = "subject";
//
//    public static final String BODY_NAME_FRAGMENT = "body";
//
//    public static final String CREATED_NAME_FRAGMENT = "created";
//
//    public static final String DATE_NAME_FRAGMENT = "date";
//
//
//    // Actual CHAO notes vocabulary translated into entities.
//
//    private OWLClass ontologyClass;
//
//    private OWLDataProperty currentNameProperty;
//
//    private OWLObjectProperty annotatesProperty;
//
//    private OWLDataProperty authorProperty;
//
//    private OWLDataProperty bodyProperty;
//
//    private OWLDataProperty subjectProperty;
//
//    private OWLObjectProperty createdProperty;
//
//    private OWLDataProperty dateProperty;
//
//    private OWLDataFactory df;
//
//
//
//    private OWLOntology domainOntology;
//
//    private OWLOntology notesOntology;
//
//
//    private Map<OWLEntity, OWLNamedIndividual> entity2OntologyClsIndividualMap = new HashMap<OWLEntity, OWLNamedIndividual>();
//
//
//    //    private Map<OWLNamedIndividual, Set<OWLNamedIndividual>> notes2Replies = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
//    private Multimap<OWLNamedIndividual, OWLNamedIndividual> notes2Replies = HashMultimap.create();
//
//
//
//    public CHAO2NotesConverter(OWLOntology domainOntology, OWLOntology notesOntology, String base) {
//        this.domainOntology = domainOntology;
//        this.notesOntology = notesOntology;
//        df = notesOntology.getOWLOntologyManager().getOWLDataFactory();
//        ontologyClass = df.getOWLClass(IRI.create(base + "#" + ONTOLOGY_CLASS_IRI_FRAGMENT));
//        currentNameProperty = df.getOWLDataProperty(IRI.create(base + "#" + CURRENT_NAME_FRAGMENT));
//        annotatesProperty = df.getOWLObjectProperty(IRI.create(base + "#" + ANNOTATES_NAME_FRAGMENT));
//        authorProperty = df.getOWLDataProperty(IRI.create(base + "#" + AUTHOR_NAME_FRAGMENT));
//        subjectProperty = df.getOWLDataProperty(IRI.create(base + "#" + SUBJECT_NAME_FRAGMENT));
//        bodyProperty = df.getOWLDataProperty(IRI.create(base + "#" + BODY_NAME_FRAGMENT));
//        createdProperty = df.getOWLObjectProperty(IRI.create(base + "#" + CREATED_NAME_FRAGMENT));
//        dateProperty = df.getOWLDataProperty(IRI.create(base + "#" + DATE_NAME_FRAGMENT));
//
//
//        mapOntologyClasses();
//    }
//
//
//
//
//
//    private void mapOntologyClasses() {
//        for (OWLClassAssertionAxiom ax : notesOntology.getClassAssertionAxioms(ontologyClass)) {
//            final OWLIndividual individual = ax.getIndividual();
//            if (individual.isNamed()) {
//                OWLNamedIndividual ontologyClsIndividual = individual.asOWLNamedIndividual();
//                IRI ontologyClsIndividualIRI = ontologyClsIndividual.getIRI();
//                Set<OWLLiteral> values = ontologyClsIndividual.getDataPropertyValues(currentNameProperty, notesOntology);
//                for (OWLLiteral val : values) {
//                    String iriLiteral = val.getLiteral().trim();
//                    IRI iri = IRI.create(iriLiteral);
//                    if (domainOntology.containsEntityInSignature(iri)) {
//                        entity2OntologyClsIndividualMap.put(df.getOWLClass(iri), ontologyClsIndividual);
//                    }
//                }
//            }
//        }
//
//        for (OWLObjectPropertyAssertionAxiom ax : notesOntology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
//            if (ax.getProperty().equals(annotatesProperty)) {
//                // Each note points to the note that it "replies to".  We swap these around to be more efficient.
//                // Object becomes subject
//                OWLNamedIndividual subjectNoteIndividual = ax.getObject().asOWLNamedIndividual();
//                // Subject becomes object
//                OWLNamedIndividual objectNoteIndividual = ax.getSubject().asOWLNamedIndividual();
//                notes2Replies.put(subjectNoteIndividual, objectNoteIndividual);
//            }
//        }

//        for (OWLEntity entity : entity2OntologyClsIndividualMap.keySet()) {
//            OWLNamedIndividual individual = entity2OntologyClsIndividualMap.get(entity);
//            List<CHAONoteData> noteDatas = new ArrayList<CHAONoteData>();
//            for (OWLNamedIndividual note : notes2Replies.get(individual)) {
//                CHAONoteData noteData = dumpNote(note, 0);
//                noteDatas.add(noteData);
//            }
//
//        }
//    }

//    public void convertToNotes(OWLAPINotesManager notesManager) {
//        for (OWLEntity entity : entity2OntologyClsIndividualMap.keySet()) {
//            OWLNamedIndividual representativeInd = entity2OntologyClsIndividualMap.get(entity);
//            final Collection<OWLNamedIndividual> namedIndividuals = notes2Replies.get(representativeInd);
//            if (!namedIndividuals.isEmpty()) {
//                for (OWLNamedIndividual noteInd : namedIndividuals) {
//                    CHAONoteData data = dumpNote(noteInd, 0);
//                    convertToNotes(notesManager, data, entity);
//                }
//            }
//        }
//    }


//
//    private void convertToNotes(OWLAPINotesManager notesManager, CHAONoteData noteData, OWLEntity entity) {
//        NoteContent content = NoteContent.builder().setSubject(noteData.getSubject()).setBody(noteData.getBody()).setNoteType(NoteType.COMMENT).build();
//        final UserId author = UserId.getUserId(noteData.getAuthor());
//        Note note = notesManager.addNoteToEntity(entity, content, author, noteData.getTimestamp());
//        for(CHAONoteData reply : noteData.getReplies()) {
//            convertToNotes(notesManager, reply, note.getNoteId());
//        }
//
//    }
//
//    private void convertToNotes(OWLAPINotesManager notesManager, CHAONoteData noteData, NoteId replyTo) {
//        NoteContent content = NoteContent.builder().setSubject(noteData.getSubject()).setBody(noteData.getBody()).setNoteType(NoteType.COMMENT).build();
//        Note note = notesManager.addReplyToNote(replyTo, content, UserId.getUserId(noteData.getAuthor()), noteData.getTimestamp());
//        for(CHAONoteData reply : noteData.getReplies()) {
//            convertToNotes(notesManager, reply, note.getNoteId());
//        }
//
//    }

//    public Multimap<OWLEntity, Note> convertToNotes() {
//        Multimap<OWLEntity, Note> result = HashMultimap.create();
//        for (OWLEntity entity : entity2OntologyClsIndividualMap.keySet()) {
//            OWLNamedIndividual representativeInd = entity2OntologyClsIndividualMap.get(entity);
//            final Collection<OWLNamedIndividual> namedIndividuals = notes2Replies.get(representativeInd);
//            if (!namedIndividuals.isEmpty()) {
//                for (OWLNamedIndividual noteInd : namedIndividuals) {
//                    CHAONoteData data = dumpNote(noteInd, 0);
//                    result.put(entity, )
////                    for(CHAONoteData d : data.getReplies()) {
////                        Set<Note> notes = new HashSet<Note>();
////                        convertToNotes(notes, d, Optional.<NoteId>absent());
////                        result.putAll(entity, notes);
////                    }
//                }
//            }
//        }
//        return result;
//    }

//    private void convertToNotes(Set<Note> notes, CHAONoteData noteData, Optional<NoteId> replyTo) {
//        NoteId noteId = NoteId.createNoteIdFromLexicalForm("Note_" + UUID.randomUUID().toString());
//        NoteHeader header = new NoteHeader(noteId, replyTo, UserId.getUserId(noteData.getAuthor()), noteData.getTimestamp());
//        NoteContent content = NoteContent.builder().setSubject(noteData.getSubject()).setBody(noteData.getBody()).setNoteType(NoteType.COMMENT).build();
//        Note note = Note.createNote(header, content);
//        notes.add(note);
//        for(CHAONoteData reply : noteData.getReplies()) {
//            convertToNotes(notes, reply, Optional.of(noteId));
//        }
//
//    }
//
//    private void dumpNotes() {
//
//        for (OWLEntity entity : entity2OntologyClsIndividualMap.keySet()) {
//            OWLNamedIndividual representativeInd = entity2OntologyClsIndividualMap.get(entity);
//            final Collection<OWLNamedIndividual> namedIndividuals = notes2Replies.get(representativeInd);
//            if (!namedIndividuals.isEmpty()) {
//                System.out.println("--------------------------------------------------------------------------------------");
//                System.out.println(entity.getEntityType() + ": " + entity);
//                for (OWLNamedIndividual noteInd : namedIndividuals) {
//                    System.out.println(dumpNote(noteInd, 0));
//                }
//            }
//        }
//    }
//
//    private CHAONoteData dumpNote(OWLNamedIndividual note, int depth) {
//        String author = getFirstLiteralValue(note.getDataPropertyValues(authorProperty, notesOntology), "");
//        String subject = getFirstLiteralValue(note.getDataPropertyValues(subjectProperty, notesOntology), "");
//        String body = getFirstLiteralValue(note.getDataPropertyValues(bodyProperty, notesOntology), "");
//
//        long timestamp = 0;
//        for (OWLIndividual createdNode : note.getObjectPropertyValues(createdProperty, notesOntology)) {
//            for (OWLLiteral litDate : createdNode.getDataPropertyValues(dateProperty, notesOntology)) {
//                try {
//                    // P3 stored the value for the created property as a fixed format date: mm/dd/yyyy hh:mm:ss z
//                    // We parse this into a timestamp, which is easier to handle, more robust and can be presented
//                    // by the client
//                    SimpleDateFormat f = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss z");
//                    Date date = f.parse(litDate.getLiteral());
//                    timestamp = date.getTime();
//                    break;
//                }
//                catch (ParseException e) {
//                    System.out.println("Could not parse date: " + e.getMessage());
//                }
//
//            }
//        }
//        String typeName = "Comment";
//        for (OWLClassExpression ce : note.getTypes(notesOntology)) {
//            if (!ce.isAnonymous()) {
//                OWLClass noteType = ce.asOWLClass();
//                // Base depends on the ontology
//                typeName = noteType.getIRI().getFragment();
//                break;
//            }
//        }
//
//        Collection<OWLNamedIndividual> replyIds = notes2Replies.get(note);
//        List<CHAONoteData> replies = new ArrayList<CHAONoteData>();
//        if (replyIds != null) {
//            for (OWLNamedIndividual reply : replyIds) {
//                CHAONoteData replyNoteData = dumpNote(reply, depth + 1);
//                replies.add(replyNoteData);
//            }
//        }
//
//        return new CHAONoteData(author, cleanSubject(subject), cleanBody(body), typeName, timestamp, replies);
//    }
//
//    private static Optional<String> cleanSubject(String subject) {
//        final String trimmedSubject = subject.trim();
//        if(trimmedSubject.startsWith("Re:")) {
//            return Optional.absent();
//        }
//        else if(trimmedSubject.isEmpty()) {
//            return Optional.absent();
//        }
//        else {
//            return Optional.of(trimmedSubject);
//        }
//    }
//
//
//    private static String cleanBody(String body) {
//        body = removeNBSP(body);
//        String stripped = removeQuotedReplies(body);
//        stripped = removeTrailingBR(stripped);
//        return removeNL(stripped);
//    }
//
//    private static String removeTrailingBR(String stripped) {
//        Pattern brEnding = Pattern.compile("((<br>)+)\\Z");
//        final Matcher matcher = brEnding.matcher(stripped);
//        if (matcher.find()) {
//            return stripped.substring(0, matcher.start());
//        }
//        else {
//            return stripped;
//        }
//    }
//
//    private static String removeNL(String s) {
//        return s.replaceAll("\\s*\\n\\s*", " ");
//    }
//
//    private static String removeQuotedReplies(String body) {
//        Pattern pattern = Pattern.compile("=====.*", Pattern.DOTALL);
//        Matcher m = pattern.matcher(body);
//        return m.replaceAll("").trim();
//    }
//
//    private static String removeNBSP(String body) {
//        return body.replace("&nbsp;", " ").trim();
//    }
//
//
//    private static String getFirstLiteralValue(Set<OWLLiteral> literals, String defaultValue) {
//        if (literals.isEmpty()) {
//            return defaultValue;
//        }
//        else {
//            return literals.iterator().next().getLiteral();
//        }
//    }
}
