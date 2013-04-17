package edu.stanford.bmir.protege.web.server.notes.impl.db;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.db.mongodb.ClassDiscussionThreadsCollectionCommand;
import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.db.mongodb.NotesCollectionCommand;
import edu.stanford.bmir.protege.web.server.notes.api.*;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteStoreImpl implements NoteStore {

    private ProjectId projectId;

    public NoteStoreImpl(ProjectId projectId) {
        this.projectId = projectId;
    }


    private DBObject findDBObject(NoteId noteId, DBCollection noteCollection) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", noteId.getLexicalForm());
        return noteCollection.findOne(basicDBObject);
    }


    @Override
    public int getNoteCount() {
        return MongoDBManager.get().runCommand(new NotesCollectionCommand<Integer, RuntimeException>(projectId) {
            @Override
            public Integer run(ProjectId projectId, DBCollection collection) {
                return (int) collection.count();
            }
        });
    }

//    @Override
    public boolean containsNote(final NoteId noteId) {
        return MongoDBManager.get().runCommand(new NotesCollectionCommand<Boolean, RuntimeException>(projectId) {
            @Override
            public Boolean run(ProjectId projectId, DBCollection dbCollection) {
                BasicDBObject basicDBObject = new BasicDBObject();
                basicDBObject.put("noteId", noteId.getLexicalForm());
                return dbCollection.count(basicDBObject) != 0;
            }
        });
    }

//    @Override
//    public Optional<Note> getNote(final NoteId noteId) {
//        return MongoDBManager.get().runCommand(new NotesCollectionCommand<Optional<Note>, Throwable>() {
//            @Override
//            public Optional<Note> run(ProjectId projectId, DBCollection dbCollection) {
//                DBObject dbObject = findDBObject(noteId, dbCollection);
//                if(dbObject == null) {
//                    return Optional.absent();
//                }
//                else {
//                    return Optional.of(fromDBObject(dbObject));
//                }
//            }
//        });
//    }

//    @Override
    private List<Note> getReplies(final NoteId noteId) {
        return MongoDBManager.get().runCommand(new NotesCollectionCommand<List<Note>, RuntimeException>(projectId) {
            @Override
            public List<Note> run(ProjectId projectId, DBCollection dbCollection) {
                BasicDBObject query = new BasicDBObject();
                query.put("replyToId", noteId.getLexicalForm());
                List<Note> result = new ArrayList<Note>();
                DBCursor cursor = dbCollection.find(query);
                while(cursor.hasNext()) {
                    DBObject noteObject = cursor.next();
                    final Note note = fromDBObject(noteObject);
                    result.add(note);
                    result.addAll(getReplies(note.getNoteId()));
                }
                return result;
            }
        });

    }

    @Override
    public int getNoteCount(NoteTarget target) {
        return 0;
    }

//    @Override
//    public List<Note> getNotes(final NoteTarget target) {
//        if(target.getEntity().isPresent() && target.getEntity().get() instanceof OWLClass) {
//            return MongoDBManager.get().runCommand(new ClassDiscussionThreadsCollectionCommand<List<Note>, Throwable>() {
//                @Override
//                public List<Note> run(ProjectId projectId, DBCollection dbCollection) {
//
//                }
//            });
//        }
//        return Collections.emptyList();
//    }

    @Override
    public DiscussionThread getDiscussionThread(final NoteTarget target) {
        return MongoDBManager.get().runCommand(new ClassDiscussionThreadsCollectionCommand<DiscussionThread, RuntimeException>(projectId) {
            @Override
            public DiscussionThread run(ProjectId projectId, DBCollection dbCollection) {
                BasicDBObject query = new BasicDBObject();
                query.put("iri", target.getEntity().get().getIRI().toString());
                DBCursor cursor = dbCollection.find(query);
                Set<Note> result = new HashSet<Note>();
                while(cursor.hasNext()) {
                    DBObject currentObject = cursor.next();
                    Note note = fromDBObject(currentObject);
                    result.add(note);
                    result.addAll(getReplies(note.getNoteId()));
                }
                return new DiscussionThread(new HashSet<Note>(result));
            }
        });
    }




    @SuppressWarnings("null")
    @Override
    public void addNote(final Note note) throws NoteChangeException {
        if(containsNote(note.getNoteId())) {
            throw new NoteChangeException(note);
        }
        MongoDBManager.get().runCommand(new NotesCollectionCommand<Void, RuntimeException>(projectId) {
            @Override
            public Void run(ProjectId projectId, DBCollection dbCollection) {
                DBObject dbObject = toDBOBject(note);
                dbCollection.save(dbObject);
                return null;
            }
        });
    }

    @SuppressWarnings("null")
    @Override
    public void removeNote(final NoteId noteId) throws NoteChangeException {
        MongoDBManager.get().runCommand(new NotesCollectionCommand<Void, RuntimeException>(projectId) {
            @Override
            public Void run(ProjectId projectId, DBCollection dbCollection) {
                dbCollection.remove(new BasicDBObject("_id", noteId.getLexicalForm()));
                List<Note> replies = getReplies(noteId);
                for(Note reply : replies) {
                    dbCollection.remove(new BasicDBObject("_id", reply.getNoteId().getLexicalForm()));
                }
                return null;
            }
        });
    }



    private Note fromDBObject(DBObject dbObject) {
        BasicDBObject basicDBObject = (BasicDBObject) dbObject;
        NoteId noteId = NoteId.createNoteIdFromLexicalForm(basicDBObject.getString("_id"));
        long timestamp = ((BasicDBObject) dbObject).getLong("timestamp");
        UserId author = UserId.getUserId(basicDBObject.getString("author"));
        String replyObjectId = basicDBObject.getString("replyToId");
        Optional<NoteId> replyToId;
        if(replyObjectId != null) {
            replyToId = Optional.of(NoteId.createNoteIdFromLexicalForm(replyObjectId));
        }
        else {
            replyToId = Optional.absent();
        }
        NoteHeader noteHeader = new NoteHeader(noteId, replyToId, author, timestamp);
        NoteContent.Builder builder = NoteContent.builder();
        builder.setSubject(Optional.fromNullable(basicDBObject.getString("subject")));
        builder.setBody(Optional.fromNullable(basicDBObject.getString("body")));
        String typeName = basicDBObject.getString("noteType");
        if(typeName != null) {
            builder.setNoteType(NoteType.valueOf(typeName));
        }
        String statusName = basicDBObject.getString("status");
        if(statusName != null) {
            builder.setNoteStatus(NoteStatus.valueOf(statusName));
        }
        NoteContent noteContent = builder.build();
        return Note.createNote(noteHeader, noteContent);
    }


    private DBObject toDBOBject(Note note) {
        Gson gson = new Gson();
        String s = gson.toJson(note);
        System.out.println(s);
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", note.getNoteId().getLexicalForm());
        basicDBObject.put("timestamp", note.getTimestamp());
        basicDBObject.put("author", note.getHeader().getAuthor().getUserName());
        final Optional<NoteId> inReplyTo = note.getInReplyTo();
        if (inReplyTo.isPresent()) {
            basicDBObject.put("replyToId", inReplyTo.get().getLexicalForm());
        }
        final NoteContent content = note.getContent();
        final Optional<String> subject = content.getSubject();
        if (subject.isPresent()) {
            basicDBObject.put("subject", subject.get());
        }
        final Optional<String> body = content.getBody();
        if (body.isPresent()) {
            basicDBObject.put("body", body.get());
        }
        final Optional<NoteType> noteType = content.getNoteType();
        if(noteType.isPresent()) {
            basicDBObject.put("noteType", noteType.get().name());
        }
        final Optional<NoteStatus> noteStatus = content.getNoteStatus();
        if(noteStatus.isPresent()) {
           basicDBObject.put("status", noteStatus.get().name());
        }
        return basicDBObject;
    }

    @Override
    public Note addNote(UserId author, NoteContent noteContent) {
        NoteHeader noteHeader = new NoteHeader(NoteIdGenerator.createNoteId(), Optional.<NoteId>absent(), author, System.currentTimeMillis());
        return Note.createNote(noteHeader, noteContent);
    }
}
