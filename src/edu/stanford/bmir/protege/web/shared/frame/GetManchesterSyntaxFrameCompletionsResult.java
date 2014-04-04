package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class GetManchesterSyntaxFrameCompletionsResult implements Result {

    private AutoCompletionResult autoCompletionResult;

    private GetManchesterSyntaxFrameCompletionsResult() {
    }

    public GetManchesterSyntaxFrameCompletionsResult(AutoCompletionResult autoCompletionResult) {
        this.autoCompletionResult = autoCompletionResult;
    }

    public AutoCompletionResult getAutoCompletionResult() {
        return autoCompletionResult;
    }
}
