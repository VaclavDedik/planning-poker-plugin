package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author vdedik@redhat.com
 */
public class SessionAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(SessionAction.class);

    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final SessionService sessionService;
    private final DateTimeFormatterFactory dateTimeFormatterFactory;

    // properties
    private String key;
    private String start;
    private String end;

    public SessionAction(IssueService issueService, JiraAuthenticationContext authContext,
                         SessionService sessionService, DateTimeFormatterFactory dateTimeFormatterFactory) {
        this.issueService = issueService;
        this.authContext = authContext;
        this.sessionService = sessionService;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String doDefault() throws Exception {

        Issue issue = getIssueObject();
        if (issue == null) {
            return ERROR;
        }

        return INPUT;
    }

    @Override
    public String doExecute() throws Exception {
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterFactory.formatter()
                .forLoggedInUser().withStyle(DateTimeStyle.COMPLETE);
        Session session = new Session();
        session.setAuthor(getCurrentUser());
        session.setCreated(new Date());
        session.setIssue(getIssueObject());
        session.setStart(dateTimeFormatter.parse(getStart()));
        session.setEnd(dateTimeFormatter.parse(getEnd()));
        sessionService.save(session);

        return SUCCESS;
    }

    private Issue getIssueObject() {
        IssueService.IssueResult issueResult = issueService.getIssue(getCurrentUser().getDirectoryUser(), getKey());
        if (!issueResult.isValid()) {
            this.addErrorCollection(issueResult.getErrorCollection());
            return null;
        }

        return issueResult.getIssue();
    }

    private ApplicationUser getCurrentUser() {
        return authContext.getUser();
    }
}
