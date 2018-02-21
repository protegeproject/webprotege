package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class PersonIdItemRenderer implements ItemRenderer<PersonId> {

    @Override
    public String getDisplayString(PersonId item) {
        return item.getId();
    }

    @Override
    public String getReplacementString(PersonId item) {
        return item.getId();
    }
}
