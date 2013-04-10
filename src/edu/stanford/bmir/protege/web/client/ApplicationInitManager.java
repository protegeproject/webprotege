package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class ApplicationInitManager {


    private List<ApplicationInitializationTask> taskList;

    /**
     * Creates an init manager with a list of tasks to be run.  The tasks will be run in the specified order, and each
     * task will be completed before the next task is run.
     * @param taskList The list of tasks to be run.  Not {@code null}.
     * @throws NullPointerException if taskLisk is {@code null}.
     */
    public ApplicationInitManager(List<ApplicationInitializationTask> taskList) {
        this.taskList = new ArrayList<ApplicationInitializationTask>(checkNotNull(taskList));
    }

    /**
     * Runs the tasks in this manager.
     */
    public void runTasks(AsyncCallback<Void> tasksCompleteCallback) {
        runInitTasks(tasksCompleteCallback);
    }


    private void runInitTasks(final AsyncCallback<Void> initComplete) {

        // The final task
        TaskRunner nextRunner = new TaskRunner() {
            @Override
            public void runTask() {
                initComplete.onSuccess(null);
            }
        };

        for(int i = taskList.size() - 1; i > -1; i--) {
            ApplicationInitializationTask task = taskList.get(i);
            nextRunner = new InitTaskRunner(task, nextRunner);
        }
        // Last is the first!  Run from the beginning
        nextRunner.runTask();
    }

    private abstract class TaskRunner {

        public abstract void runTask();

    }

    private class InitTaskRunner extends TaskRunner implements ApplicationInitTaskCallback {

        private ApplicationInitializationTask task;

        private TaskRunner nextRunner;

        private InitTaskRunner(ApplicationInitializationTask task, TaskRunner nextRunner) {
            this.task = task;
            this.nextRunner = nextRunner;
        }

        public void runTask() {
            GWT.log("Running init task: " + task.getName() + " ... ");
            long t0 = new Date().getTime();
            task.run(this);
            long t1 = new Date().getTime();
            GWT.log("    ... completed in " + (t1 - t0) + " ms");
        }

        @Override
        public void taskComplete() {
            nextRunner.runTask();
        }


        @Override
        public void taskFailed(Throwable e) {
            GWT.log("Initialization task failed", e);
        }
    }




    public static interface ApplicationInitializationTask {

        public abstract void run(ApplicationInitTaskCallback callback);

        public abstract String getName();
    }


    public static interface ApplicationInitTaskCallback {

        void taskComplete();

        void taskFailed(Throwable e);
    }







}
