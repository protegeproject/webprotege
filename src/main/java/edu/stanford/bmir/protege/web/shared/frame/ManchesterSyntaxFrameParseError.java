package edu.stanford.bmir.protege.web.shared.frame;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.EntityType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class ManchesterSyntaxFrameParseError implements Serializable, IsSerializable {

    private int line;

    private int col;

    private String message;

    private String token;

    private List<EntityType<?>> expectedEntityTypes;

    private ManchesterSyntaxFrameParseError() {

    }

    public ManchesterSyntaxFrameParseError(String message, int col, int line, String token, List<EntityType<?>> expectedEntityTypes) {
        this.message = message;
        this.col = col;
        this.line = line;
        this.token = token;
        this.expectedEntityTypes = new ArrayList<EntityType<?>>(expectedEntityTypes);
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public List<EntityType<?>> getExpectedEntityTypes() {
        return new ArrayList<EntityType<?>>(expectedEntityTypes);
    }
}
