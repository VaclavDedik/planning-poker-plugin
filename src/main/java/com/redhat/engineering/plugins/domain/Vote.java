package com.redhat.engineering.plugins.domain;

import com.atlassian.jira.user.ApplicationUser;

/**
 * @author vdedik@redhat.com
 */
public class Vote {
    private Session session;
    private ApplicationUser voter;
    private String value;
    private String comment;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
