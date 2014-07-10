package com.redhat.engineering.plugins.domain;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class Session implements Serializable {

    private Issue issue;

    private Date created;

    private ApplicationUser author;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
