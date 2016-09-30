package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.shared.issues.Issue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public interface IssueRepository extends Repository<Issue, Long> {

    Issue save(Issue s);

    Optional<Issue> findByProjectIdAndNumber(ProjectId projectId, long issueNumber);

    Optional<Issue> findOneByProjectIdOrderByNumberDesc(ProjectId projectId);

    Stream<Issue> findByProjectId(ProjectId projectId);

    long countByProjectId(ProjectId projectId);
}
