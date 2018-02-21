package edu.stanford.bmir.protege.web.server.revision;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/05/15
 */
public enum RevisionSerializationVocabulary {



    USERNAME_METADATA_ATTRIBUTE("username"),

    REVISION_META_DATA_ATTRIBUTE("revision"),

    DESCRIPTION_META_DATA_ATTRIBUTE("description"),

    REVISION_TYPE_META_DATA_ATTRIBUTE("revisionType");

    private String vocabularyName;

    RevisionSerializationVocabulary(String vocabularyName) {
        this.vocabularyName = vocabularyName;
    }

    public String getVocabularyName() {
        return vocabularyName;
    }
}
