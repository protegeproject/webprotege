package edu.stanford.bmir.protege.web.server;

public interface ProjectManager {

    Object getProject(String prjName);

    boolean isSuitable(String prjName);

    void dispose();
}
