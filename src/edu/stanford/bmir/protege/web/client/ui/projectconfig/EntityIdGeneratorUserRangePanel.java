package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdUserRange;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityIdGeneratorUserRangePanel extends FlowPanel {

    private EntityIdUserRange userRange;

    private TextBox usernameBox;
    
    private TextBox firstIdBox;
    
    private TextBox lastIdBox;
    
    public EntityIdGeneratorUserRangePanel(EntityIdUserRange userRange) {
        this.userRange = userRange;
        this.usernameBox = new TextBox();
        this.firstIdBox = new TextBox();
        this.lastIdBox = new TextBox();
    
        add(usernameBox);
        usernameBox.getElement().setAttribute("placeholder", "User name");
        usernameBox.addStyleName("web-protege-form-layout-margin");
//        add(new InlineLabel("  First: "));
        firstIdBox.getElement().setAttribute("placeholder", "First Id (leave blank for no min)");
        firstIdBox.addStyleName("web-protege-form-layout-margin");
        add(firstIdBox);
//        add(new InlineLabel("  Last: "));
        lastIdBox.getElement().setAttribute("placeholder", "Last Id  (leave blank for no max)");
        lastIdBox.addStyleName("web-protege-form-layout-margin");
        add(lastIdBox);
        setData(userRange);
    }



    public void setData(EntityIdUserRange data) {
        this.usernameBox.setText(data.getUserId().getUserName());
        long first = data.getFirstId();
        if (first != 0) {
            this.firstIdBox.setText(Long.toString(first));
        }
        long last = data.getLastId();
        if (last != Long.MAX_VALUE) {
            this.lastIdBox.setText(Long.toString(last));
        }
    }

    public EntityIdUserRange getData() {
        return new EntityIdUserRange(getUserId(), getStart(), getEnd());
    }

    private long getStart() {
        try {
            return Long.parseLong(firstIdBox.getText().trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private long getEnd() {
        try {
            return Long.parseLong(lastIdBox.getText().trim());
        }
        catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }

    private UserId getUserId() {
        return UserId.getUserId(getUserName());
    }

    private String getUserName() {
        return usernameBox.getText().trim();
    }


}
