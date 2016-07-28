package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public interface IssueRecordRepository extends Repository<IssueRecord, Long> {

    IssueRecord save(IssueRecord s);

    Optional<IssueRecord> findByProjectIdAndNumber(ProjectId projectId, long issueNumber);

    Stream<IssueRecord> findByProjectIdAndTargetEntities(ProjectId projectId, OWLEntity targetEntity);

    Iterable<IssueRecord> findAll();

    long count();

    void deleteByProjectIdAndNumber(ProjectId projectId, long number);

    void delete(Iterable<? extends IssueRecord> iterable);
}
