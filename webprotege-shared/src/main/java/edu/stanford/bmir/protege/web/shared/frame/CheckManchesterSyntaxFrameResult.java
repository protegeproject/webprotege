package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameResult implements Result {

    private ManchesterSyntaxFrameParseResult result;

    private ManchesterSyntaxFrameParseError error;

    @GwtSerializationConstructor
    private CheckManchesterSyntaxFrameResult() {
    }

    public CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseResult result) {
        this.result = result;
    }

    public CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseError error) {
        this.error = error;
        this.result = ManchesterSyntaxFrameParseResult.ERROR;
    }

    public ManchesterSyntaxFrameParseResult getResult() {
        return result;
    }

    public Optional<ManchesterSyntaxFrameParseError> getError() {
        return Optional.ofNullable(error);
    }
}
