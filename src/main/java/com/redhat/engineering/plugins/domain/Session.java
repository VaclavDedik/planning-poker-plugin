package com.redhat.engineering.plugins.domain;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vdedik@redhat.com
 */
public class Session implements Serializable {

    private Issue issue;

    private Date created;

    private User author;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
