package edu.stanford.bmir.protege.web.client.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.issues.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
@Module
public class ClientIssuesModule {

    @Provides
    DiscussionThreadListView provideEntityDiscussionThreadListView(DiscussionThreadListViewImpl view) {
        return view;
    }

    @Provides
    DiscussionThreadView provideEntityDiscussionThreadView(DiscussionThreadViewImpl view) {
        return view;
    }

    @Provides
    CommentView provideCommentView(CommentViewImpl view) {
        return view;
    }

    @Provides
    CommentEditorView provideCommentEditorView(CommentEditorViewImpl view) {
        return view;
    }
}
