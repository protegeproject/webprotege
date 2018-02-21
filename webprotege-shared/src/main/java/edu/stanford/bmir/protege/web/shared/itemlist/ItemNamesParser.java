package edu.stanford.bmir.protege.web.shared.itemlist;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public interface ItemNamesParser {

    List<String> parseItemNames(String itemNamesString);
}
