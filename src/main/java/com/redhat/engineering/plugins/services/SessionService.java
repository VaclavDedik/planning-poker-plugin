package com.redhat.engineering.plugins.services;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.redhat.engineering.plugins.domain.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;

/**
 * @author vdedik@redhat.com
 */
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private static final String KEY = "com.redhat.engineering.plugins.planningpoker.sessions";

    private final PluginSettingsFactory pluginSettingsFactory;
    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;

    public SessionService(PluginSettingsFactory pluginSettingsFactory, IssueService issueService,
                          JiraAuthenticationContext authContext) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.issueService = issueService;
        this.authContext = authContext;
    }

    public void save(Session session) {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        Properties sessionProps = new Properties();
        sessionProps.setProperty("created", Long.toString(session.getCreated().getTime()));
        sessionProps.setProperty("authorId", Long.toString(session.getAuthor().getDirectoryId()));

        pluginSettings.put(getIssueStoreKey(session.getIssue()), sessionProps);
    }

    public Session get(String issueKey) {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        Issue issue = issueService.getIssue(authContext.getUser().getDirectoryUser(), issueKey).getIssue();
        Properties sessionProps = (Properties) pluginSettings.get(getIssueStoreKey(issue));
        Session session = new Session();
        session.setCreated(new Date(Long.parseLong(sessionProps.getProperty("created"))));
        session.setIssue(issue);

        return session;
    }

    private String getIssueStoreKey(Issue issue) {
        return KEY + "." + issue.getKey();
    }
}
