package com.redhat.engineering.plugins.services;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author vdedik@redhat.com
 */
@SuppressWarnings("unchecked")
public class SessionService extends AbstractPokerService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final PluginSettings pluginSettings;
    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final UserManager userManager;
    private final VoteService voteService;

    public SessionService(PluginSettingsFactory pluginSettingsFactory, IssueService issueService,
                          JiraAuthenticationContext authContext, UserManager userManager,
                          VoteService voteService) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
        this.issueService = issueService;
        this.authContext = authContext;
        this.userManager = userManager;
        this.voteService = voteService;
    }

    public void save(Session session) {
        Properties sessionProps = new Properties();
        sessionProps.setProperty("created", Long.toString(session.getCreated().getTime()));
        sessionProps.setProperty("start", Long.toString(session.getStart().getTime()));
        sessionProps.setProperty("end", Long.toString(session.getEnd().getTime()));
        sessionProps.setProperty("authorKey", session.getAuthor().getKey());

        pluginSettings.put(getIssueStoreKey(session.getIssue()), sessionProps);
        voteService.removeAllVotes(session);

        List<String> allSessions = (List<String>) pluginSettings.get(getKey() + ".all");
        if (allSessions == null) {
            allSessions = new ArrayList<String>();
        }
        if (allSessions.contains(session.getIssue().getKey())) {
            allSessions.remove(session.getIssue().getKey());
        }
        allSessions.add(session.getIssue().getKey());
        pluginSettings.put(getKey() + ".all", allSessions);
    }

    public Session get(String issueKey) {
        log.info("Get session by issue key: " + issueKey);
        Issue issue = issueService.getIssue(authContext.getUser().getDirectoryUser(), issueKey).getIssue();
        Properties sessionProps = (Properties) pluginSettings.get(getIssueStoreKey(issue));
        if (sessionProps == null) {
            return null;
        }

        Session session = new Session();
        session.setCreated(new Date(Long.parseLong(sessionProps.getProperty("created"))));
        session.setStart(new Date(Long.parseLong(sessionProps.getProperty("start"))));
        session.setEnd(new Date(Long.parseLong(sessionProps.getProperty("end"))));
        session.setIssue(issue);
        session.setAuthor(userManager.getUserByKey(sessionProps.getProperty("authorKey")));

        return session;
    }

    public List<Session> getAll() {
        return this.getAll(0, Integer.MAX_VALUE);
    }

    public List<Session> getAll(Integer offset, Integer limit) {
        List<String> allSessionKeys = (List<String>) pluginSettings.get(getKey() + ".all");
        List<Session> sessions = new ArrayList<Session>();

        if (allSessionKeys == null) {
            return sessions;
        }

        for (Integer i = offset; i - offset < limit && i < allSessionKeys.size(); i++) {
            String key = allSessionKeys.get((allSessionKeys.size() - 1) - i); // Start from the newest session
            Session session = this.get(key);
            if (session != null) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public void update(Session session) {
        String storeKey = getIssueStoreKey(session.getIssue());
        Properties sessionProps = (Properties) pluginSettings.get(storeKey);
        sessionProps.setProperty("start", Long.toString(session.getStart().getTime()));
        sessionProps.setProperty("end", Long.toString(session.getEnd().getTime()));

        pluginSettings.put(storeKey, sessionProps);
    }

    public void delete(Session session) {
        String storeKey = getIssueStoreKey(session.getIssue());
        pluginSettings.remove(storeKey);

        List<String> allSessions = (List<String>) pluginSettings.get(getKey() + ".all");
        allSessions.remove(session.getIssue().getKey());
        pluginSettings.put(getKey() + ".all", allSessions);
    }

    public Status getStatus(Session session) {
        if (System.currentTimeMillis() < session.getStart().getTime()) {
            return Status.SCHEDULED;
        } else if (System.currentTimeMillis() < session.getEnd().getTime()) {
            return Status.IN_PROGRESS;
        } else {
            return Status.FINISHED;
        }
    }
}
