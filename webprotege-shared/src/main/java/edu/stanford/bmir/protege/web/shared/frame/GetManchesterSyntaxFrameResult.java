package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameResult implements Result {

    private String manchesterSyntax;

    private GetManchesterSyntaxFrameResult() {
    }

    public GetManchesterSyntaxFrameResult(String manchesterSyntax) {
        this.manchesterSyntax = manchesterSyntax;
    }

    public String getManchesterSyntax() {
        return manchesterSyntax;
    }
}
