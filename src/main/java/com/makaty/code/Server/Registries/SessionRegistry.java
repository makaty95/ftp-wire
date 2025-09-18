package com.makaty.code.Server.Registries;

import com.makaty.code.Server.Handshaking.Session;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class SessionRegistry {

    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    // Private constructor to enforce creation through the factory method
    private SessionRegistry() {}

    /**
     * Factory method — creates a new, independent SessionRegistry.
     */
    public static SessionRegistry createRegistry() {
        return new SessionRegistry();
    }

    /**
     * Adds a new session.
     */
    public void add(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    /**
     * Returns a session by ID.
     */
    public Session get(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Terminates and removes a session.
     */
    public void terminateSession(String sessionId) {
        Session session = sessions.remove(sessionId);
        if (session != null) {
            session.close();
        }
    }

    /**
     * Removes a session without terminating it.
     */
    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * Removes all sessions.
     */
    public void removeAll() {
        this.sessions.clear();
    }



    /**
     * Returns all sessions.
     */
    public List<Session> getAll() {
        return this.sessions.values().stream().toList();
    }

    /**
     * Returns the first session if available, otherwise null.
     */
    public Session getFirst() {
        return this.sessions.values().stream().findFirst().orElse(null);
    }

    /**
     * Returns the total number of sessions.
     */
    public int howManySessions() {
        return sessions.size();
    }
}