package com.redhat.engineering.plugins.panels;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.userformat.UserFormats;
import com.atlassian.jira.plugin.userformat.UserFormatter;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.web.model.WebPanel;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.Maps;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.services.SessionService;
import com.redhat.engineering.plugins.services.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

/**
 * @author vdedik@redhat.com
 */
public class PlanningPokerPanel implements WebPanel {
    private static final Logger log = LoggerFactory.getLogger(PlanningPokerPanel.class);

    private final SessionService sessionService;
    private final VoteService voteService;
    private final TemplateRenderer templateRenderer;
    private final DateTimeFormatter dateTimeFormatter;
    private final UserFormats userFormats;
    private final AvatarService avatarService;
    private final JiraAuthenticationContext authContext;
    private final PermissionManager permissionManager;

    public PlanningPokerPanel(SessionService sessionService, TemplateRenderer templateRenderer,
                              DateTimeFormatter dateTimeFormatter, UserFormats userFormats,
                              AvatarService avatarService, JiraAuthenticationContext authContext,
                              VoteService voteService, PermissionManager permissionManager) {
        this.sessionService = sessionService;
        this.voteService = voteService;
        this.templateRenderer = templateRenderer;
        this.dateTimeFormatter = dateTimeFormatter.forLoggedInUser();
        this.userFormats = userFormats;
        this.avatarService = avatarService;
        this.authContext = authContext;
        this.permissionManager = permissionManager;
    }

    @Override
    public String getHtml(Map<String, Object> context) {
        if (!authContext.isLoggedInUser()) {
            return "You must be logged in to view planning poker session.";
        }

        String issueKey = ((Issue) context.get("issue")).getKey();
        Session session = sessionService.get(issueKey);
        if (session == null) {
            return "No session.";
        }

        context.put("session", session);
        context.put("pokerComponent", this);
        String baseurl = ComponentAccessor.getApplicationProperties().getString(APKeys.JIRA_BASEURL);
        context.put("baseurl", baseurl);

        StringWriter stringWriter = new StringWriter();
        try {
            templateRenderer.render("views/panel.vm", context, stringWriter);
        } catch (IOException e) {
            log.error("Failed to render Planning Poker panel, exception message: {}", e.getMessage());
            return null;
        }
        return stringWriter.toString();
    }

    @Override
    public void writeHtml(Writer writer, Map<String, Object> context) throws IOException {
        writer.write(getHtml(context));
    }

    public String formatDate(Date date) {
        return this.dateTimeFormatter.withStyle(DateTimeStyle.RELATIVE_ALWAYS_WITH_TIME).format(date);
    }

    public String getAuthorHtml(Session session) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("avatarURL", getAvatarURL(session.getAuthor()));
        UserFormatter userFormatter = userFormats.formatter("avatarFullNameHover");
        return userFormatter.formatUserkey(session.getAuthor().getKey(), "poker-author", params);
    }

    public String getAvatarURL(ApplicationUser user) {
        return avatarService.getAvatarUrlNoPermCheck(user, Avatar.Size.NORMAL).toString();
    }

    public Integer getVotesSize(Session session) {
        return voteService.getVoteValsBySession(session).size();
    }

    public boolean isVoter(Session session) {
        return voteService.isVoter(session, authContext.getUser());
    }

    public boolean hasVotePermission(Session session) {
        return permissionManager.hasPermission(Permissions.EDIT_ISSUE, session.getIssue(), authContext.getUser());
    }
}
