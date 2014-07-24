package com.redhat.engineering.plugins.actions;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.plugin.userformat.UserFormats;
import com.atlassian.jira.plugin.userformat.UserFormatter;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.ApplicationUser;
import com.google.common.collect.Maps;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.domain.Vote;
import com.redhat.engineering.plugins.services.SessionService;
import com.redhat.engineering.plugins.services.VoteService;

import java.util.List;
import java.util.Map;

/**
 * @author vdedik@redhat.com
 */
public class VoteAction extends AbstractAction {

    private final JiraAuthenticationContext authContext;
    private final SessionService sessionService;
    private final VoteService voteService;
    private final UserFormats userFormats;
    private final AvatarService avatarService;
    private final PermissionManager permissionManager;

    // properties
    private String key;
    private String voteVal;
    private List<String> messages;

    public VoteAction(JiraAuthenticationContext authContext, SessionService sessionService,
                      VoteService voteService, UserFormats userFormats, AvatarService avatarService,
                      PermissionManager permissionManager) {
        this.authContext = authContext;
        this.sessionService = sessionService;
        this.voteService = voteService;
        this.userFormats = userFormats;
        this.avatarService = avatarService;
        this.permissionManager = permissionManager;
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

        if (!authContext.isLoggedInUser()) {
            addErrorMessage("You must be logged in to be able to vote.");
            return ERROR;
        }

        Session session = getSessionObject();
        if (session == null) {
            return ERROR;
        }
        if (System.currentTimeMillis() < session.getStart().getTime()) {
            this.addErrorMessage("You cannot vote because the planning poker session hasn't started yet.");
            return ERROR;
        }
        if (System.currentTimeMillis() > session.getEnd().getTime()) {
            this.addErrorMessage("You cannot vote because the planning poker session has already ended.");
            return ERROR;
        }

        if (voteService.isVoter(session, getCurrentUser())) {
            setVoteVal(voteService.getVoteVal(session, getCurrentUser()));
        }

        return INPUT;
    }

    @Override
    public String doExecute() throws Exception {
        if (!permissionManager.hasPermission(Permissions.EDIT_ISSUE, getSessionObject().getIssue(), getCurrentUser())) {
            addErrorMessage("You don't have permission to vote.");
            return ERROR;
        }

        Vote vote = new Vote();
        vote.setValue(getVoteVal());
        vote.setVoter(getCurrentUser());
        vote.setSession(getSessionObject());
        voteService.save(vote);

        this.addMessage("Your vote has been successfully saved.");
        return SUCCESS;
    }

    public String doViewVotes() throws Exception {
        if (!authContext.isLoggedInUser()) {
            addErrorMessage("You must be logged in to be able to view votes.");
            return ERROR;
        }

        Session session = getSessionObject();
        if (session == null) {
            return ERROR;
        }
        if (System.currentTimeMillis() < session.getEnd().getTime()) {
            this.addErrorMessage("You cannot view votes because the planning poker session hasn't ended yet.");
            return ERROR;
        }

        return "viewVotes";
    }

    public String doViewVoters() throws Exception {
        if (!authContext.isLoggedInUser()) {
            addErrorMessage("You must be logged in to be able to view votes.");
            return ERROR;
        }

        Session session = getSessionObject();
        if (session == null) {
            return ERROR;
        }
        return "viewVoters";
    }

    public List<Vote> getVotes() {
        Session session = getSessionObject();
        return voteService.getVotesBySession(session);
    }

    public String getUserHtml(ApplicationUser user) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("avatarURL", getAvatarURL(user));
        UserFormatter userFormatter = userFormats.formatter("avatarFullNameHover");
        return userFormatter.formatUserkey(user.getKey(), "poker-author", params);
    }

    public String getAvatarURL(ApplicationUser user) {
        return avatarService.getAvatarUrlNoPermCheck(user, Avatar.Size.NORMAL).toString();
    }

    public List<ApplicationUser> getVoters() {
        Session session = getSessionObject();
        return voteService.getVotersBySession(session);
    }

    private Session currentSession;

    private Session getSessionObject() {
        if (currentSession == null) {
            currentSession = sessionService.get(getKey());
        }
        return currentSession;
    }

    private ApplicationUser getCurrentUser() {
        return authContext.getUser();
    }
}
