///*
// * SimulationSessionListener
// */
//package ua.kpi.atep.controller;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpSessionEvent;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.WebApplicationContext;
//
///**
// *
// * Keeps track of sessions in the application
// * 
// * Used to safely pass session attributes to websocket endpoint
// * 
// * Works as follows:
// * 
// * 1. Each session is stored in a map by session id
// * 
// * 2. When a client needs to access session attributes, it passes jsession id 
// *    first
// * 
// * 3. Needed attributes are resolved by jsession id's
// * 
// * 4. Session is deleted from map when it expires
// *
// * @author Konstantin Kovalchuk
// */
//@Component
//public class SessionListener implements javax.servlet.http.HttpSessionListener, ApplicationContextAware {
//    
//    /**
//     * store session
//     */
//    private final ConcurrentMap<String, HttpSession> sessions
//            = new ConcurrentHashMap<>();
//
//    @Override
//    public void sessionCreated(final HttpSessionEvent se) {
//        final HttpSession session = se.getSession();
//        
//        sessions.put(session.getId(), session);
//    }
//
//    @Override
//    public void sessionDestroyed(final HttpSessionEvent se) {
//        final HttpSession session = se.getSession();
//        
//        sessions.remove(session.getId());
//    }
//    
//    /**
//     * Get session by jsessionid
//     * 
//     * @param id
//     * @return 
//     */
//    public HttpSession getSession(String id) {
//        return sessions.get(id);
//    }
//    
//    /**
//     * Copied from: http://stackoverflow.com/questions/2433321/how-to-inject-dependencies-into-httpsessionlistener-using-spring
//     * 
//     * @param applicationContext
//     * @throws BeansException 
//     */
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        if (applicationContext instanceof WebApplicationContext) {
//            ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
//        } else {
//            throw new RuntimeException("Must be inside a web application context");
//        }
//    }           
//}
