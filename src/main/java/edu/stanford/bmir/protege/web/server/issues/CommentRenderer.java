package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.plugins.PegDownPlugins;

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
                rendering.append("<span class=\"user-mention\">");
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
        PegDownProcessor processor = new PegDownProcessor(Extensions.ABBREVIATIONS | Extensions.QUOTES | Extensions.STRIKETHROUGH | Extensions.AUTOLINKS | Extensions.FENCED_CODE_BLOCKS,
                                                          new PegDownPlugins.Builder().build());
        String html = processor.markdownToHtml(rendering.toString(), new LinkRenderer() {

        });
        return html;
    }
}
