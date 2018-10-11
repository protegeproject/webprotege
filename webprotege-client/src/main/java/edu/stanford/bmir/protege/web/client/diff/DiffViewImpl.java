package edu.stanford.bmir.protege.web.client.diff;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.WebProtege;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;

import java.io.Serializable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
public class DiffViewImpl extends Composite implements DiffView {

    interface DiffViewImplUiBinder extends UiBinder<HTMLPanel, DiffViewImpl> {
    }

    private static DiffViewImplUiBinder ourUiBinder = GWT.create(DiffViewImplUiBinder.class);

    @UiField
    protected HTML diffRendering;

    public DiffViewImpl() {
        DiffClientBundle.INSTANCE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public <S extends Serializable, E extends Serializable> void setDiff(List<DiffElement<S, E>> diff,
                                                                         DiffLineElementRenderer<E> renderer,
                                                                         DiffSourceDocumentRenderer<S> sourceDocumentRenderer) {
        StringBuilder sb = new StringBuilder();
        for(DiffElement<S, E> element : diff) {
            DiffOperation op = element.getDiffOperation();
            DiffClientBundle.DiffCssResource style = DiffClientBundle.INSTANCE.style();
            String operationStyle = op == DiffOperation.ADD ? style.add() : style.remove();
            sb.append("<div class=\"").append(style.line()).append(" ").append(operationStyle).append("\">");
            renderBulletSpan(sb, op);
            renderSourceDocumentSpan(sb, element.getSourceDocument(), sourceDocumentRenderer);
            sb.append("<div class=\" "+ style.lineElement() +" \">");
            sb.append(renderer.getRendering(element.getLineElement()).asString());
            sb.append("</div>");
            sb.append("</div>");
        }
        diffRendering.setHTML(new SafeHtmlBuilder().appendHtmlConstant(sb.toString()).toSafeHtml());
    }

    private void renderBulletSpan(StringBuilder sb, DiffOperation op) {
        DiffClientBundle.DiffCssResource style = DiffClientBundle.INSTANCE.style();
        String bulletStyle = op == DiffOperation.ADD ? style.addBullet() : style.removeBullet();
        sb.append("<div class=\"").append(bulletStyle).append(" \">").append("</div>");
    }

    private <S extends Serializable, E extends Serializable> void renderSourceDocumentSpan(StringBuilder sb, S sourceDocument, DiffSourceDocumentRenderer<S> sourceDocumentRenderer) {
        DiffClientBundle.DiffCssResource style = DiffClientBundle.INSTANCE.style();
        String str = sourceDocumentRenderer.renderSourceDocument(sourceDocument).asString();
        if(!str.isEmpty()) {
            sb.append("<span class=\"").append(style.source()).append("\">[").append(str).append("]</span>");
        }

    }
}