package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2016
 */
public class CommentRenderer {

    private MentionParser mentionParser = new MentionParser();

    public String renderComment(String commentBody) {
        List<ParsedMention> parsedMentions = mentionParser.parseMentions(commentBody);
        StringBuilder rendering = new StringBuilder();
        int currentPos = 0;
        for(ParsedMention pm : parsedMentions) {
            int startIndex = pm.getStartIndex();
            int endIndex = pm.getEndIndex();
            rendering.append(commentBody.substring(currentPos, startIndex));
            Mention mention = pm.getParsedMention();
            if(mention.getMentionedUserId().isPresent()) {
                rendering.append("<span class=\"wp-comment__user-mention\">");
                rendering.append(mention.getMentionedUserId().get().getUserName());
                rendering.append("</span>");
            }
            else {
                rendering.append(commentBody.substring(startIndex, endIndex));
            }
            currentPos = endIndex;
        }
        if(currentPos < commentBody.length()) {
            rendering.append(commentBody.substring(currentPos));
        }
        List<Extension> extensions = Arrays.asList(AutolinkExtension.create(),
                                                   StrikethroughExtension.create(),
                                                   TablesExtension.create(),
                                                   InsExtension.create());
        Parser parser = Parser.builder()
                .extensions(extensions)
                .build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
        Node document = parser.parse(rendering.toString());
        return renderer.render(document);
    }
}
