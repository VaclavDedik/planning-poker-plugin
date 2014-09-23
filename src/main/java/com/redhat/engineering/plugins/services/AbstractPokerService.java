package com.redhat.engineering.plugins.services;

import com.atlassian.jira.issue.Issue;

/**
 * @author vdedik@redhat.com
 */
public abstract class AbstractPokerService {
    private static final String KEY = "com.redhat.engineering.plugins.planningpoker";

    protected String getIssueStoreKey(Issue issue) {
        return KEY + ".sessions." + issue.getKey();
    }

    protected String getKey() {
        return KEY;
    }
}
