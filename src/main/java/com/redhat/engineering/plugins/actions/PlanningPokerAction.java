package com.redhat.engineering.plugins.actions;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author vdedik@redhat.com
 */
public class PlanningPokerAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(PlanningPokerAction.class);

    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final SessionService sessionService;

    // properties
    private String key;

    public PlanningPokerAction(IssueService issueService, JiraAuthenticationContext authContext,
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

        Issue issue = getIssueObject();
        if (issue == null) {
            return INPUT;
        }

        return INPUT;
    }

    @Override
    public String doExecute() throws Exception {
        Session session = new Session();
        session.setAuthor(getCurrentUser());
        session.setCreated(new Date());
        session.setIssue(getIssueObject());
        sessionService.save(session);

        return SUCCESS;
    }

    private Issue getIssueObject() {
        IssueService.IssueResult issueResult = issueService.getIssue(authContext.getUser().getDirectoryUser(), getKey());
        if (!issueResult.isValid()) {
            this.addErrorCollection(issueResult.getErrorCollection());
            return null;
        }

        return issueResult.getIssue();
    }

    private User getCurrentUser() {
        return authContext.getUser().getDirectoryUser();
    }
}
