package com.redhat.engineering.plugins.domain;

import com.atlassian.jira.user.ApplicationUser;

/**
 * @author vdedik@redhat.com
 */
public class Vote {
    private Session session;
    private ApplicationUser voter;
    private Integer value;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ApplicationUser getVoter() {
        return voter;
    }

    public void setVoter(ApplicationUser voter) {
        this.voter = voter;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
