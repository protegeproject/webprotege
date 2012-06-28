package edu.stanford.bmir.protege.web.server;

import java.util.Date;

import edu.stanford.bmir.protege.web.client.rpc.NotificationType;

public class NotificationChangeData {
    private final String author;
    private final String description;
    private final Date timestamp;
    private final String project;
    private final String valueType;
    private final String name;
    private final NotificationType type;

    public NotificationChangeData(String author, String description, Date timestamp, String project, String valueType,
            String name, NotificationType type) {
        this.author = author;
        this.description = description;
        this.timestamp = timestamp;
        this.project = project;
        this.valueType = valueType;
        this.name = name;
        this.type = type;
    }

    public NotificationType getType() {
        return type;
    }

    public String getProject() {
        return project;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getValueType() {
        return valueType;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationChangeData)) {
            return false;
        }

        NotificationChangeData that = (NotificationChangeData) o;

        if (author != null ? !author.equals(that.author) : that.author != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (project != null ? !project.equals(that.project) : that.project != null) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        if (valueType != null ? !valueType.equals(that.valueType) : that.valueType != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}