package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GridControlOrderByDirection {

    @JsonProperty("asc")
    ASC(1),

    @JsonProperty("desc")
    DESC(-1);

    int dir;

    GridControlOrderByDirection(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }
}
