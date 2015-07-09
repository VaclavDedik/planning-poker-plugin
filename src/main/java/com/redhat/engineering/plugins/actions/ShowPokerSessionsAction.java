package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Status;
import com.redhat.engineering.plugins.services.SessionService;

import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class ShowPokerSessionsAction extends AbstractAction {
    private final SessionService sessionService;
    private final JiraAuthenticationContext authContext;

    //props
    private List<Session> sessions;

    public ShowPokerSessionsAction(SessionService sessionService, JiraAuthenticationContext authContext) {
        this.sessionService = sessionService;
        this.authContext = authContext;
    }

    public List<Session> getSessions() {
        if (sessions == null) {
            sessions = this.sessionService.getAll();
        }
        return sessions;
    }

    @Override
    public String doExecute() throws Exception {
        return "list";
    }

    public Status getStatus(Session session) {
        return this.sessionService.getStatus(session);
    }
}
