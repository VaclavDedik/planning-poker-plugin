package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Vote;
import com.redhat.engineering.plugins.services.SessionService;
import com.redhat.engineering.plugins.services.VoteService;

import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class VoteAction extends JiraWebActionSupport {

    private final IssueService issueService;
    private final JiraAuthenticationContext authContext;
    private final SessionService sessionService;
    private final VoteService voteService;

    // properties
    private String key;
    private String voteVal;

    public VoteAction(IssueService issueService, JiraAuthenticationContext authContext,
                         SessionService sessionService, VoteService voteService) {
        this.issueService = issueService;
        this.authContext = authContext;
        this.sessionService = sessionService;
        this.voteService = voteService;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVoteVal() {
        return voteVal;
    }

    public void setVoteVal(String voteVal) {
        this.voteVal = voteVal;
    }

    @Override
    public String doDefault() throws Exception {

        Session session = getSessionObject();
        if (session == null) {
            return ERROR;
        }
        if (voteService.isVoter(session, getCurrentUser())) {
            setVoteVal(voteService.getVoteVal(session, getCurrentUser()));
        }

        return INPUT;
    }

    @Override
    public String doExecute() throws Exception {
        Vote vote = new Vote();
        vote.setValue(getVoteVal());
        vote.setVoter(getCurrentUser());
        vote.setSession(sessionService.get(getKey()));
        voteService.save(vote);

        return SUCCESS;
    }

    public String doViewVotes() throws Exception {
        return "viewVotes";
    }

    public List<Vote> getVotes() {
        Session session = sessionService.get(getKey());
        return voteService.getVotesBySession(session);
    }

    private Session getSessionObject() {
        return sessionService.get(getKey());
    }

    private ApplicationUser getCurrentUser() {
        return authContext.getUser();
    }
}
