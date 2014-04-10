package edu.stanford.bmir.protege.web.client.rpc;

/**
 */
public enum NotificationTimestamp {
    LAST_ONTOLOGY_TIMESTAMP("ontology.notification.timestamp", NotificationType.ONTOLOGY), LAST_COMMENT_TIMESTAMP("comment.notification.timestamp", NotificationType.COMMENT);
    private final String value;
    private final NotificationType type;

    NotificationTimestamp(String value, NotificationType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public NotificationType getType() {
        return type;
    }

    public static NotificationTimestamp fromType(NotificationType type){
        if (LAST_COMMENT_TIMESTAMP.getType().equals(type)){
            return LAST_COMMENT_TIMESTAMP;
        }
        if (LAST_ONTOLOGY_TIMESTAMP.getType().equals(type)){
            return LAST_ONTOLOGY_TIMESTAMP;
        }
        return null;
    }
}