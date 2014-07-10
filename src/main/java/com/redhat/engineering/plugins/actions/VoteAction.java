package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.services.SessionService;

/**
 * @author vdedik@redhat.com
 */
public class VoteAction extends JiraWebActionSupport {

    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final SessionService sessionService;

    // properties
    private String key;

    public VoteAction(IssueService issueService, JiraAuthenticationContext authContext,
                         SessionService sessionService) {
        this.issueService = issueService;
        this.authContext = authContext;
        this.sessionService = sessionService;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String doDefault() throws Exception {

        Session session = getSessionObject();
        if (session == null) {
            return ERROR;
        }

        return INPUT;
    }

    private Session getSessionObject() {
        return sessionService.get(getKey());
    }

    private ApplicationUser getCurrentUser() {
        return authContext.getUser();
    }
}
