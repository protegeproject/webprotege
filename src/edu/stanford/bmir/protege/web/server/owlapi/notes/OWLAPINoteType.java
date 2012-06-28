package edu.stanford.bmir.protege.web.server.owlapi.notes;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public enum OWLAPINoteType {

    // What's the difference between Advice, Comment, Example, Explanation?  See Also already exists in RDFS,
    // as does comment.

    ADVICE("advice", "Advice"), 

    COMMENT("comment", "Comment"), 

    EXAMPLE("example", "Example"), 

    EXPLANATION("explanation", "Explanation"), 

    QUESTION("question", "Question"), 

//    PROPOSAL("proposal", "Proposal"),
//
//    PROPOSAL_FOR_CHANGE_HIERARCHY("proposalForChangeHierarchy", "Proposal for Change Hierarchy"),
//
//    PROPOSAL_FOR_CHANGE_PROPERTY_VALUE("proposalForChangePropertyValue", "Proposal for Change Property Value"),
//
//    PROPOSAL_FOR_CREATE_ENTITY("proposalForCreateEntity", "Proposal for Create Entity"),
//
//    PROPOSAL_FOR_MERGE("proposalForMerge", "Proposal for Merge"),
//
//    PROPOSAL_FOR_RETIRE("proposalForRetire", "Proposal for Retire"),
//
//    PROPOSAL_FOR_SPLIT("proposalForSplit", "Proposal for Split"),

    REVIEW("review", "Review"),

    SEE_ALSO("seeAlso", "See Also");

//    VOTE("vote", "Vote"),
//
//    VOTE_AGREE_DISAGREE("voteAgreeDisagree", "Vote: Agree/Disagree"),
//
//    VOTE_FIVE_STARS("voteFiveStars", "Vote: Star Rating");


    public static final OWLAPINoteType DEFAULT_NOTE_TYPE = OWLAPINoteType.COMMENT;

    private String localName;

    private String displayName;
    
    private IRI iri;

    private OWLAPINoteType(String localName, String displayName) {
        this.localName = localName;
        this.displayName= displayName;
        this.iri = IRI.create(NotesVocabulary.PREFIX + localName);
    }

    public String getLocalName() {
        return localName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public IRI getIRI() {
        return iri;
    }
}
