package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.UUID;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class BranchIdFactory {

  public static BranchId getFreshBranchId() {
    UUID uuid = UUID.randomUUID();
    return BranchId.get(uuid.toString(), true);
  }
}
