package com.redhat.engineering.plugins.services;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author vdedik@redhat.com
 */
@SuppressWarnings("unchecked")
public class VoteService extends AbstractPokerService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final PluginSettingsFactory pluginSettingsFactory;
    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final UserManager userManager;

    public VoteService(PluginSettingsFactory pluginSettingsFactory, IssueService issueService,
                          JiraAuthenticationContext authContext, UserManager userManager) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.issueService = issueService;
        this.authContext = authContext;
        this.userManager = userManager;
    }

    public void save(Vote vote) {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        String issueStoreKey = getIssueStoreKey(vote.getSession().getIssue());

        List<String> voters = (List<String>) pluginSettings.get(issueStoreKey + ".voters");
        if (voters.contains(vote.getVoter().getKey())) {
            return;
        }
        voters.add(vote.getVoter().getKey());
        pluginSettings.put(issueStoreKey + ".voters", voters);

        pluginSettings.put(issueStoreKey + "." + vote.getVoter().getKey(), vote.getValue().toString());

        List<String> votes = (List<String>) pluginSettings.get(issueStoreKey + ".votes");
        votes.add(vote.getValue().toString());
        pluginSettings.put(issueStoreKey + ".votes", votes);
    }

    public List<Integer> getVoteValsBySession(Session session) {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        String storeKey = getIssueStoreKey(session.getIssue()) + ".votes";
        return (List<Integer>) pluginSettings.get(storeKey);
    }
}
