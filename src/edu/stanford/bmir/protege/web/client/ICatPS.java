package edu.stanford.bmir.protege.web.client;

import edu.stanford.bmir.protege.web.client.ui.ICPSTopPanel;
import edu.stanford.bmir.protege.web.client.ui.TopPanel;

/**
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class ICatPS extends ICat {

    private final static String PROJECT_NAME = "ICPS";



    public String getProjectName() {
        return PROJECT_NAME;
    }

    public TopPanel getTopPanel(){
        return new ICPSTopPanel();
    }


}
