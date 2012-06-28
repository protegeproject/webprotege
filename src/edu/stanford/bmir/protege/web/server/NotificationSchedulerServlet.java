package edu.stanford.bmir.protege.web.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import edu.stanford.bmir.protege.web.client.rpc.NotificationInterval;
import edu.stanford.bmir.protege.web.client.rpc.NotificationType;
import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.bmir.protegex.chao.annotation.api.AnnotatableThing;
import edu.stanford.bmir.protegex.chao.annotation.api.Annotation;
import edu.stanford.bmir.protegex.chao.annotation.api.AnnotationFactory;
import edu.stanford.bmir.protegex.chao.change.api.Change;
import edu.stanford.bmir.protegex.chao.change.api.ChangeFactory;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Ontology_Component;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.User;
import edu.stanford.smi.protege.code.generator.wrapping.AbstractWrappedInstance;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.Transaction;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.server_changes.RetrieveChangesProtegeJob;

/**
 * Server-side services for notification management.
 */
public class NotificationSchedulerServlet extends HttpServlet {
    private static final long serialVersionUID = -7869180204904741210L;

    private static final Logger log = Log.getLogger(NotificationSchedulerServlet.class);

    private static final String PROPERTY_PREFIX = "scheduler.last.run.time.";

    private ScheduledExecutorService service;
    private final Map<NotificationInterval, Boolean> hasRescheduledTaskPending = new HashMap<NotificationInterval, Boolean>();
    private final MessageFormat ontologyChangeMessage = new MessageFormat("{1,date} {1,time}: {2} made the change: \n\t{0}\n");
    private final MessageFormat noteChangeMessage = new MessageFormat("{1,date} {1,time}: {2} added a new comment: \n\t{0}\n");
    private final MessageFormat linkMessage = new MessageFormat("\tDirect link: {0}?ontology={1}&tab={2}&id={3}\n\n");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory(){
            public Thread newThread(Runnable r) {
                final Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("Email Notification Thread");
                return thread;
            }
        });
        if (ApplicationProperties.getImmediateThreadsEnabled()) {
            service.scheduleAtFixedRate(new NotificationScheduler(NotificationInterval.IMMEDIATELY),
                    ApplicationProperties.getImmediateThreadStartupDelay(),     // delay
                    ApplicationProperties.getImmediateThreadInterval(), //period
                    TimeUnit.MILLISECONDS);
        }
        service.scheduleAtFixedRate(new NotificationScheduler(NotificationInterval.DAILY),
                ApplicationProperties.getDailyThreadStartupDelay(),     // delay
                NotificationInterval.DAILY.getIntervalInMilliseconds(), //period
                TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(new NotificationScheduler(NotificationInterval.HOURLY),
                ApplicationProperties.getHourlyThreadStartupDelay(),     // delay
                NotificationInterval.HOURLY.getIntervalInMilliseconds(),   //period
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void destroy() {
        service.shutdownNow();
    }

    private class NotificationScheduler extends TimerTask {
        private final NotificationInterval interval;
        private final boolean isRescheduled;

        private NotificationScheduler(NotificationInterval interval) {
            this(interval, false);
        }

        private NotificationScheduler(NotificationInterval interval, boolean rescheduled) {
            this.interval = interval;
            isRescheduled = rescheduled;
        }

        /**
         * The run method.
         * <p/>
         * Note that this method will only run when scheduled; there is nothing to do with scheduling in this method.
         */
        @Override
        public void run() {
            // Set the time we poll until to one minute ago. This is to correct a rare issue to do with the change processor not processing updates.
            final Date now = new Date(System.currentTimeMillis() - 60000);

            long t0 = System.currentTimeMillis();

            try {
                if (!ApplicationProperties.getAllNotificationEnabled()) {
                    return;
                }
                if (hasRescheduledTaskPending.get(interval) != null && hasRescheduledTaskPending.get(interval) && !isRescheduled) {
                    return;
                }

                Map<String, Set<NotificationChangeData>> notificationChangeDataCollector = new HashMap<String, Set<NotificationChangeData>>();

                final Collection<ServerProject<Project>> projects = Protege3ProjectManager.getProjectManager().getOpenServerProjects();
                final String intervalPropertyName = PROPERTY_PREFIX + interval.getValue();
                final MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
                final Set<ServerProject<Project>> notReadySet = new HashSet<ServerProject<Project>>();

                for (ServerProject<Project> serverProject : projects) {
                    if (serverProject == null || serverProject.getProject() == null || !serverProject.isLoaded() || WatchedEntitiesCache.getCache(serverProject.getProject()) == null) {
                        notReadySet.add(serverProject);
                        continue;
                    }
                    try {
                        Date lastRunTime = null;
                        final ProjectInstance instance = metaProject.getProject(serverProject.getProjectName());
                        final String threadIntervalPropertyValue = instance.getPropertyValue(intervalPropertyName);
                        if (threadIntervalPropertyValue != null) {
                            lastRunTime = new Date(Long.parseLong(threadIntervalPropertyValue));
                        }

                        KnowledgeBase kb = serverProject.getProject().getKnowledgeBase();
                        final KnowledgeBase changesKb = ChAOKbManager.getChAOKb(kb);

                        if (changesKb == null) {
                            if (log.isLoggable(Level.FINE)) {
                                log.log(Level.FINE, "Unable to open Chao KB of " + serverProject.getProjectName());
                            }
                            continue;
                        }
                        // first run only - initialize lastRunTime
                        if (lastRunTime == null) {
                            // if we do not have a last run date, then the last run date was 24 hours ago
                            lastRunTime = new Date(new Date().getTime() - (1000 * 60 * 24));

                        }

                        //get changes
                        boolean shouldContinue = getOntologyChanges(serverProject.getProjectName(), lastRunTime, now, notificationChangeDataCollector, kb, changesKb);

                        if (!shouldContinue) {
                            hasRescheduledTaskPending.put(interval, Boolean.TRUE);
                            return;
                        }

                        //get annotations
                        getAnnotationChanges(serverProject.getProjectName(), lastRunTime, now, notificationChangeDataCollector, kb, changesKb);

                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Caught error in email notification thread, continuing.", e);
                    }
                }

                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Notifying " + notificationChangeDataCollector);
                }

                for (Map.Entry<String, Set<NotificationChangeData>> userToNotificationChanges : notificationChangeDataCollector.entrySet()) {
                    Set<NotificationChangeData> notificationChangeData = userToNotificationChanges.getValue();
                    if (notificationChangeData.isEmpty()) {
                        continue;
                    }

                    final edu.stanford.smi.protege.server.metaproject.User user = metaProject.getUser(userToNotificationChanges.getKey());

                    if (user == null) {
                        if (log.isLoggable(Level.FINE)) {
                            log.log(Level.FINE, "Could not find user " + userToNotificationChanges.getKey() + " in MetaProject, continuing.");
                        }
                        continue;
                    }

                    final NotificationInterval frequencyOntology = NotificationInterval.fromString(user.getPropertyValue(NotificationType.ONTOLOGY.getValue()));
                    NotificationInterval frequencyComment = NotificationInterval.fromString(user.getPropertyValue(NotificationType.COMMENT.getValue()));
                    //if user has not set their frequency, then we default to immediate notification.
                    if (frequencyOntology == null) {
                        notificationChangeData = removeUnwantedChanges(notificationChangeData, NotificationInterval.IMMEDIATELY, NotificationType.ONTOLOGY);
                    } else {
                        notificationChangeData = removeUnwantedChanges(notificationChangeData, frequencyOntology, NotificationType.ONTOLOGY);
                    }

                    //if user has not set their frequency, then we default to immediate notification.
                    if (frequencyComment == null) {
                        notificationChangeData = removeUnwantedChanges(notificationChangeData, NotificationInterval.IMMEDIATELY, NotificationType.COMMENT);
                    } else {
                        notificationChangeData = removeUnwantedChanges(notificationChangeData, frequencyComment, NotificationType.COMMENT);
                    }

                    List<NotificationChangeData> notificationChangeDataList = new ArrayList<NotificationChangeData>(notificationChangeData);
                    Collections.sort(notificationChangeDataList, new Comparator<NotificationChangeData>() {
                        public int compare(NotificationChangeData o1, NotificationChangeData o2) {
                            return new Long(o1.getTimestamp().getTime() - o2.getTimestamp().getTime()).intValue(); //FIXME: can be null
                        }
                    });

                    //send the notifications
                    sendNotification(user, notificationChangeDataList);
                }

                if (this.interval.equals(NotificationInterval.DAILY)) {
                    purgeCache(now);
                }
                for (ServerProject<Project> serverProject : projects) {
                    if (notReadySet.contains(serverProject)) {
                        continue;
                    }
                    final ProjectInstance instance = metaProject.getProject(serverProject.getProjectName());
                    String threadIntervalPropertyValue = instance.getPropertyValue(intervalPropertyName);
                    while (threadIntervalPropertyValue != null) {
                        instance.removePropertyValue(intervalPropertyName, threadIntervalPropertyValue);
                        threadIntervalPropertyValue = instance.getPropertyValue(intervalPropertyName);
                    }
                    instance.addPropertyValue(intervalPropertyName, Long.toString(now.getTime()));
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "Caught in notification thread", e);
            }

            long notificationRuntime = System.currentTimeMillis() - t0;
            if (notificationRuntime > 10 * 1000) {
                log.info("Finished email notification with interval=" + interval + " isRescheduled=" + isRescheduled + " took " +
                        notificationRuntime / 1000 + " seconds.");
            }

            hasRescheduledTaskPending.put(interval, Boolean.FALSE);
        }

        private Set<NotificationChangeData> removeUnwantedChanges(final Set<NotificationChangeData> changes, final NotificationInterval frequencyOntology, final NotificationType notificationType) {
            Set<NotificationChangeData> tempChanges = new HashSet<NotificationChangeData>(changes);
            if (!frequencyOntology.equals(this.interval)) {
                for (NotificationChangeData change : changes) {
                    if (change.getType().equals(notificationType)) {
                        tempChanges.remove(change);
                    }
                }
            }
            return tempChanges;
        }

        /**
         * clean up cache by making sure that we use only the earliest time of the immediate/hourly/daily runs, preventing the last thread (daily) from purging elements the other threads have yet to use
         *
         * @param now    The time to purge the cache for.
         */
        private void purgeCache(Date now) {
            Long earliestCommentTime = now.getTime();
            String hourlyProperty = NotificationInterval.HOURLY.getValue();
            String immediatelyProperty = NotificationInterval.IMMEDIATELY.getValue();
            String hourlyValue = System.getProperty(hourlyProperty);
            String immediatelyValue = System.getProperty(immediatelyProperty);

            if (hourlyValue != null) {
                if (earliestCommentTime > Long.parseLong(hourlyValue)) {
                    earliestCommentTime = Long.parseLong(hourlyValue);
                }
            }
            if (immediatelyValue != null) {
                if (earliestCommentTime > Long.parseLong(immediatelyValue)) {
                    earliestCommentTime = Long.parseLong(immediatelyValue);
                }
            }
            AnnotationCache.purge(new Date(0), new Date(earliestCommentTime));
        }

        /**
         * Retrieves the notifications, and adds the changes to a map.
         * <p/>
         * Note that the map is userName -> List of change data. This allows us to easily post-process, as we know the changes that each user
         * needs to be informed of.
         *
         * @param projectName
         * @param lastRunDate
         * @param end
         * @param collector
         * @param knowledgeBase
         * @param changesKb
         * @return true if the process should continue, false if it should not.
         */
        @SuppressWarnings("unchecked")
        private boolean getOntologyChanges(String projectName, final Date lastRunDate, Date end, Map<String, Set<NotificationChangeData>> collector,
                KnowledgeBase knowledgeBase, final KnowledgeBase changesKb) {

            final Collection<Change> changes = (Collection<Change>) new RetrieveChangesProtegeJob(knowledgeBase,  lastRunDate, end).execute();

            // if we are in a transaction, then reschedule
            if (changes == null){
                service.schedule(new NotificationScheduler(this.interval, true), ApplicationProperties.getEmailRetryDelay(), TimeUnit.MILLISECONDS);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Found transaction in service, rescheduling " + interval + "  thread to run in " + (ApplicationProperties.getEmailRetryDelay()/1000) + " seconds.");
                }
                return false;
            }

            LocalizeUtils.localize(changes, changesKb);

            for (Change change : changes) {
                final Ontology_Component ontologyComponent = change.getApplyTo();

                final NotificationChangeData dataWithProject = new NotificationChangeData(change.getAuthor(), change.getContext(),
                        change.getTimestamp().getDateParsed(),
                        projectName, ontologyComponent.getComponentType(), ontologyComponent.getCurrentName(), NotificationType.ONTOLOGY);

                final Set<String> users = WatchedEntitiesCache.getCache(knowledgeBase.getProject()).getEntityWatches(ontologyComponent.getCurrentName());
                for (String user : users) {
                    Set<NotificationChangeData> existingChangeDataWithProject = collector.get(user);
                    if (existingChangeDataWithProject == null) {
                        existingChangeDataWithProject = new HashSet<NotificationChangeData>();
                    }
                    if (!existingChangeDataWithProject.contains(dataWithProject)) {
                        existingChangeDataWithProject.add(dataWithProject);
                    }
                    collector.put(user, existingChangeDataWithProject);
                }

                // if the name is null, then we're a delete, which we don't handle.
                final String currentName = ontologyComponent.getCurrentName();
                if (currentName != null) {
                    Frame frame = knowledgeBase.getFrame(currentName);
                    if (frame != null && frame instanceof Cls) {
                        addBranchWatch(collector, (Cls) frame, dataWithProject);
                    }
                }
            }
            return true;

        }

        private void getAnnotationChanges(String projectName, final Date lastRunDate, Date end, Map<String, Set<NotificationChangeData>> collector,
                KnowledgeBase knowledgeBase, final KnowledgeBase chaoKb) {

            final Slot authorSlot = new ChangeFactory(chaoKb).getAuthorSlot();

            final Collection<Instance> annotationInstances = AnnotationCache.getAnnotations(chaoKb.getProject().getName(), lastRunDate, end);

            for (Instance annotationInstance : annotationInstances) {
                final Ontology_Component rootNode = AnnotationCache.getRootNode(annotationInstance);

                final String author = (String) annotationInstance.getDirectOwnSlotValue(authorSlot);

                String currentRootName = rootNode.getCurrentName();
                final String className = currentRootName == null ? "" : currentRootName;

                final Annotation annotation = AnnotationFactory.getGenericAnnotation(annotationInstance);
                final NotificationChangeData notifChangeData = new NotificationChangeData(author, 
                        annotationInstance.getBrowserText() + (annotation.hasBody() ? "\n  Details: " + annotation.getBody() : ""),
                        AnnotationCache.getChangeDate(chaoKb.getProject().getName(), annotationInstance), 
                        projectName, rootNode.getComponentType(), className, NotificationType.COMMENT);

                final Collection<User> users = rootNode.getWatchedBy();
                for (User user : users) {
                    Set<NotificationChangeData> changeData = collector.get(user.getName());
                    if (changeData == null) {
                        changeData = new HashSet<NotificationChangeData>();
                    }

                    changeData.add(notifChangeData);
                    collector.put(user.getName(), changeData);
                }

                final Collection<AnnotatableThing> annotatedThings = annotation.getAnnotates();
                if (annotatedThings != null) {
                    for (AnnotatableThing annotatedThing : annotatedThings) {
                        Ontology_Component oc = null;
                        if (annotatedThing.canAs(Ontology_Component.class)) {
                            oc = annotatedThing.as(Ontology_Component.class);
                        }
                        else if (annotatedThing.canAs(Annotation.class)) {
                            if (annotatedThing instanceof AbstractWrappedInstance) {
                                oc = AnnotationCache.getRootNode(((AbstractWrappedInstance)annotatedThing).getWrappedProtegeInstance());
                            }
                        }
                        if (oc != null) {
                            // if the name is null, then we're a delete, which we don't handle.
                            String currentName = oc.getCurrentName();
                            if (currentName != null) {
                                Frame frame = knowledgeBase.getFrame(currentName);
                                if (frame !=null && frame instanceof Cls) {
                                    addBranchWatch(collector, (Cls) frame, notifChangeData);
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * Add the change to our collector if it occurs on a branch that is being watched by any user.
         *
         * TT: Code below might cause performance issues with the main ontology, especially with the coarse grain lock.
         * With the readers-writers branch, should be fine.
         * @param collector
         * @param watchedBranchNodeToUserMap
         * @param frame
         * @param dataWithProject
         */
        private void addBranchWatch(Map<String, Set<NotificationChangeData>> collector, Cls cls, NotificationChangeData dataWithProject) {

            Collection<Cls> superclasses = cls.getSuperclasses();
            if (superclasses == null){
               return;
            }

            superclasses = new ArrayList(superclasses);
            superclasses.add(cls);

            for (Cls supercls : superclasses) {
                final Set<String> userNames = WatchedEntitiesCache.getCache(supercls.getProject()).getBranchWatches(supercls.getName());
                for (String user : userNames) {
                    Set<NotificationChangeData> changeData = collector.get(user);
                    if (changeData == null) {
                        changeData = new HashSet<NotificationChangeData>();
                    }
                    changeData.add(dataWithProject);
                    collector.put(user, changeData);
                }
            }
        }
    }

    private void sendNotification(edu.stanford.smi.protege.server.metaproject.User user, Collection<NotificationChangeData> notificationChangeData) {

        final String userEmail = user.getEmail();
        if (userEmail == null || userEmail.trim().length() == 0) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Could not send notification to user " + user.getName() +" on " + new Date() +". No email address for this user in the metaproject.");
            }
            return;
        }

        if (notificationChangeData.isEmpty()) {
            return;
        }

        //TODO: add a property to the user in the metaproject of whether the own changes should be sent
        //check the flag before filtering out own changes
        //default: filter out own changes
        boolean filterOutOwnChanges = true;

        StringBuffer email = new StringBuffer();
        boolean foundOneNotification = false;

        for (NotificationChangeData notificationChange : notificationChangeData) {

            if (filterOutOwnChanges && user.getName().equals(notificationChange.getAuthor())) { //filter out own changes
                continue;
            }
            foundOneNotification = true;

            Object[] messageParams = new Object[]{Transaction.removeApplyTo(notificationChange.getDescription()), notificationChange.getTimestamp(), notificationChange.getAuthor()};

            if (NotificationType.COMMENT.equals(notificationChange.getType())) {
                noteChangeMessage.format(messageParams, email, new FieldPosition(0));
            } else {
                ontologyChangeMessage.format(messageParams, email, new FieldPosition(0));
            }

            try {
                String tabName = getTabName(notificationChange);
                linkMessage.format(new Object[]{
                        ApplicationProperties.getApplicationUrl(),
                        URLEncoder.encode(notificationChange.getProject(), "UTF-8"),
                        tabName,
                        notificationChange.getName() == null ? "" : URLEncoder.encode(notificationChange.getName(), "UTF-8")
                }, email, new FieldPosition(0));
            } catch (UnsupportedEncodingException e) {
                Log.getLogger().log(Level.SEVERE, "Error formatting to URLEncoding projectName = " + notificationChange.getProject() + ", tab = " + notificationChange.getValueType() + ", id = " + notificationChange.getName(), e);
            }
        }

        final String applicationName = ApplicationProperties.getApplicationName();
        email.append("\n-----\n* To change the frequency of your notifications, or to stop receiving them altogether, please edit your profile by going to ");
        email.append(ApplicationProperties.getApplicationUrl());
        email.append(" and clicking Options -> Edit Profile.");
        final String subject = MessageFormat.format("{1} change report generated on {0,date} {0,time}", new Date(), applicationName);

        if (foundOneNotification) {
            //send email
            EmailUtil.sendEmail(userEmail, subject, email.toString(), ApplicationProperties.getEmailAccount());

            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Emailing notifications to user '" + userEmail + "' with subject '" + subject + "' message '" + email.toString() + "'");
            }
        }

    }

    private String getTabName(NotificationChangeData change) throws UnsupportedEncodingException {
        String tabName = "ClassesTab";
        if ("Individual".equals(change.getValueType())) {
            tabName = "IndividualsTab";
        } else if (NotificationType.COMMENT.equals(change.getType())) {
            tabName = "NotesTab";
        }
        return tabName;
    }
}
