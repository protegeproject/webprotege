package edu.stanford.bmir.protege.web.server;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.DefaultKnowledgeBase;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.Session;
import edu.stanford.smi.protege.server.framestore.RemoteClientFrameStore;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class KBUtil {

    public static void morphUser(KnowledgeBase kb, String user) {
        if (kb.getProject().isMultiUserClient()) {
            Session s = (Session) RemoteClientFrameStore.getCurrentSession(kb);
            s.setDelegate(user);
        } else {
            ((DefaultKnowledgeBase) kb).setUserName(user);
        }
    }

    public static void restoreUser(KnowledgeBase kb) {
        String defaultUser = ApplicationProperties.getProtegeServerUser();
        if (kb.getProject().isMultiUserClient()) {
            Session s = (Session) RemoteClientFrameStore.getCurrentSession(kb);
            s.setDelegate(null);
        } else {
            ((DefaultKnowledgeBase) kb).setUserName(defaultUser);
        }
    }

    public static boolean shouldRunInTransaction(String operationDescription) {
        return operationDescription != null && operationDescription.length() > 0;
    }

    @SuppressWarnings("unchecked")
    public static <X> Collection<X> getCollection(KnowledgeBase kb, Collection<String> names, Class<? extends X> javaInterface) {
        Collection<X> entities = new HashSet<X>();
        if (names == null) {
            return entities;
        }
        for (String name : names) {
            Frame frame = kb.getFrame(name);
            if (frame != null && javaInterface.isAssignableFrom(frame.getClass())) {
                entities.add((X)frame);
            }
        }
        return entities;
    }

    //TODO: not the best util class for this method.. find a better one
    public static String getUserInSession(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final UserData userData = (UserData) session.getAttribute(SessionConstants.USER_DATA_PARAMETER);
        if (userData == null) {
            return null;
        }
        return userData.getName();
    }

    public static String getRemoteProjectName(Project prj) {
        URI uri = prj.getProjectURI();
        if (uri == null) { return null; }
        try {
            String path = uri.getPath();
            int index = path.lastIndexOf("/");
            if (index > -1) {
                return path.substring(index + 1);
            }
        } catch (Exception e) {
            Log.emptyCatchBlock(e);
            //do nothing
        }
        return null;
    }


    /**
     * This utility method returns a path between a class and its superclass,
     * in REVERSE order, i.e. the path will start with the superclass and will
     * end with the subclass. <BR>
     * <BR>
     * <B>Note:</B>
     * <ul>
     * <li>If there are multiple paths between the class and its superclass this
     *      method will return only one of them (not necessarily the shortest one).
     * <li>If there is no path between the class and its supposed superclass
     *      (i.e. the second argument is not a superclass of the first argument)
     *      the method will return a list containing only the class itself.
     * <li>If both the <code>srcClass</code> and <code>dstSuperclass</code>
     *      refer to the same class, and there is a cycle involving that class
     *      this method will return one of the existing cycles.
     * </ul>
     * @param srcClass a class
     * @param dstSuperclass a superclass of the first argument
     * @param path the result of this method will be collected in the path variable
     */
    public static void getPathToSuperClass(Cls srcClass, Cls dstSuperclass, ArrayList<Cls> path) {
        //in the special case src == dst add the source to the path only at the end of the algorithm,
        //after the recursive calls are over, to not interfere with the cycle check
        if (! srcClass.equals(dstSuperclass)) {
            path.add(0, srcClass);
        }

        for (Iterator<?> it = srcClass.getDirectSuperclasses().iterator(); it.hasNext(); ) {
            Object po = it.next();
            if (po instanceof Cls) {
                Cls p = (Cls) po;
                if (p.equals(dstSuperclass)) {
                    path.add(0, dstSuperclass);
                    break;
                }
                else {
                    //if we have a parent that could lead us to the destination, and is not creating a cycle in the path
                    if (p.getSuperclasses().contains(dstSuperclass) && !path.contains(p)) {
                        getPathToSuperClass(p, dstSuperclass, path);
                        if (path.contains(dstSuperclass)) {
                            break;
                        }
                        else {
                            //this parent will not bring us to the destination
                            if (!path.isEmpty() && path.get(0).equals(p)) {
                                path.remove(0);
                            }
                            else {
                                Log.getLogger().log(Level.SEVERE,
                                        "Assertion failure: something must be wrong with the getPathToSuperClass algorithm. " +
                                        "The first element in the path " + path + " is expected to be: " + p);
                            }
                        }
                    }
                }
            }
            else {
                Log.getLogger().log(Level.WARNING,
                        "Invalid type of " + po + " (superclass of " + srcClass + "). Excpected type is 'Cls'");
            }
        }

        //treat special case src == dst
        if (srcClass.equals(dstSuperclass)) {
            path.add(srcClass);
        }

    }

    public static void getPathToSuperClass(OWLClass srcClass, OWLClass dstSuperclass, ArrayList<OWLClass> path) {
        //in the special case src == dst add the source to the path only at the end of the algorithm,
        //after the recursive calls are over, to not interfere with the cycle check
        if (! srcClass.equals(dstSuperclass)) {
            path.add(0, srcClass);
        }

        for (Iterator<?> it = srcClass.getSuperclasses(false).iterator(); it.hasNext(); ) {
            Object po = it.next();
            if (po instanceof OWLNamedClass) {
                OWLNamedClass p = (OWLNamedClass) po;
                if (p.equals(dstSuperclass)) {
                    path.add(0, dstSuperclass);
                    break;
                }
                else {
                    //if we have a parent that could lead us to the destination, and is not creating a cycle in the path
                    if (p.getSuperclasses(true).contains(dstSuperclass) && !path.contains(p)) {
                        getPathToSuperClass(p, dstSuperclass, path);
                        if (path.contains(dstSuperclass)) {
                            break;
                        }
                        else {
                            //this parent will not bring us to the destination
                            if (!path.isEmpty() && path.get(0).equals(p)) {
                                path.remove(0);
                            }
                            else {
                                Log.getLogger().log(Level.SEVERE,
                                        "Assertion failure: something must be wrong with the getPathToSuperClass algorithm. " +
                                        "The first element in the path " + path + " is expected to be: " + p);
                            }
                        }
                    }
                }
            }
            else {
//                Log.getLogger().log(Level.WARNING,
//                        "Invalid type of " + po + " (superclass of " + srcClass + "). Excpected type is 'OWLNamedClass'");
            }
        }

        //treat special case src == dst
        if (srcClass.equals(dstSuperclass)) {
            path.add(srcClass);
        }

    }
}
