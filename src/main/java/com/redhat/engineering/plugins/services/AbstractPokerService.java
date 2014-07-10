package com.redhat.engineering.plugins.services;

import com.atlassian.jira.issue.Issue;

/**
 * @author vdedik@redhat.com
 */
public abstract class AbstractPokerService {
    private static final String KEY = "com.redhat.engineering.plugins.planningpoker.sessions";

    protected String getIssueStoreKey(Issue issue) {
        return KEY + "." + issue.getKey();
    }
}
