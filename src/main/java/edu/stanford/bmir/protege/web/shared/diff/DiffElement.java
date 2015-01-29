package edu.stanford.bmir.protege.web.shared.diff;


import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */

/**
 * Represents a line in a diff.
 * @param <S> The type of source document.
 * @param <E> The type of lineElement contained within each line in the diff.
 */
public class DiffElement<S extends Serializable, E extends Serializable> {

    private DiffOperation diffOperation;

    private S sourceDocument;

    private E lineElement;

    public DiffElement(DiffOperation diffOperation, S sourceDocument, E lineElement) {
        this.diffOperation = checkNotNull(diffOperation);
        this.sourceDocument = checkNotNull(sourceDocument);
        this.lineElement = checkNotNull(lineElement);
    }

    public DiffOperation getDiffOperation() {
        return diffOperation;
    }

    public S getSourceDocument() {
        return sourceDocument;
    }

    public E getLineElement() {
        return lineElement;
    }

//    @Override
//    public int compareTo(DiffElement<S, E> o) {
//        int elementDiff = this.lineElement.compareTo(o.lineElement);
//        if(elementDiff != 0) {
//            return elementDiff;
//        }
//        int operationDiff = this.diffOperation.compareTo(o.diffOperation);
//        if(operationDiff != 0) {
//            return operationDiff;
//        }
//        return this.sourceDocument.compareTo(o.sourceDocument);
//    }

    @Override
    public int hashCode() {
        return Objects.hashCode(diffOperation, sourceDocument, lineElement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DiffElement)) {
            return false;
        }
        DiffElement other = (DiffElement) obj;
        return this.diffOperation.equals(other.diffOperation)
                && this.sourceDocument.equals(other.sourceDocument)
                && this.lineElement.equals(other.lineElement);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("DiffElement")
                .addValue(diffOperation)
                .add("sourceDocument", sourceDocument)
                .add("lineElement", lineElement)
                .toString();
    }
}
