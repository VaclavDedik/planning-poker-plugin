package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Status;
import com.redhat.engineering.plugins.services.SessionService;
import com.redhat.engineering.plugins.services.VoteService;

import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class ShowPokerSessionsAction extends AbstractAction {
    private static final Integer PAGE_COUNT = 30;

    private final SessionService sessionService;
    private final JiraAuthenticationContext authContext;
    private final VoteService voteService;
    private final PermissionManager permissionManager;

    //props
    private List<Session> sessions;
    private String page = "1";
    private Integer pageCount;

    public ShowPokerSessionsAction(SessionService sessionService, JiraAuthenticationContext authContext,
                                   VoteService voteService, PermissionManager permissionManager) {
        this.sessionService = sessionService;
        this.authContext = authContext;
        this.voteService = voteService;
        this.permissionManager = permissionManager;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Session> getSessions() {
        if (sessions == null) {
            Integer page = Integer.parseInt(getPage());
            Integer offset = PAGE_COUNT * (page - 1);
            sessions = this.sessionService.getAll(offset, PAGE_COUNT);
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

    public Integer getPageCount() {
        if (pageCount == null) {
            pageCount = (int) Math.ceil(this.sessionService.count() / (float) PAGE_COUNT);
        }
        return pageCount;
    }

    public Boolean isVoter(Session session) {
        return voteService.isVoter(session, authContext.getUser());
    }

    public Integer getVotesSize(Session session) {
        return voteService.getVoteValsBySession(session).size();
    }

    public boolean hasOwnerPermission(Session session) {
        return session.getAuthor().equals(authContext.getUser());
    }

    public boolean hasVotePermission(Session session) {
        return permissionManager.hasPermission(Permissions.EDIT_ISSUE, session.getIssue(), authContext.getUser());
    }
}
