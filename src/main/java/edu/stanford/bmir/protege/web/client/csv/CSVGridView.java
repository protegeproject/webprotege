package edu.stanford.bmir.protege.web.client.csv;

import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public interface CSVGridView {

    void setCSVGrid(CSVGrid grid);

    void scrollColumnToVisible(int index);


    void setHeaderText(int index, String headerText);

    void clearHeaderText(int index);

    void clearColumnHighlighting();

    void setColumnHighlighted(int index, boolean highlighted);

    void setColumnBold(int index, boolean bold);

    void addColumnStyleName(int index, String styleName);

    void addTDStyleName(int index, String styleName);

    void removeColumnStyleName(int index, String styleName);

    void removeTDStyleName(int index, String styleName);

    void removeColumnStyleName(String styleName);

//    void setColumnIcon(ImageResource imageResource);
}
