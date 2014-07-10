package com.redhat.engineering.plugins.panels;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.userformat.UserFormats;
import com.atlassian.jira.plugin.userformat.UserFormatter;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.web.model.WebPanel;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.Maps;
import com.redhat.engineering.plugins.domain.Session;
import com.redhat.engineering.plugins.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author vdedik@redhat.com
 */
public class PlanningPokerPanel implements WebPanel {
    private static final Logger log = LoggerFactory.getLogger(PlanningPokerPanel.class);

    private final SessionService sessionService;
    private final TemplateRenderer templateRenderer;
    private final DateTimeFormatterFactory dateTimeFormatterFactory;
    private final UserFormats userFormats;
    private final AvatarService avatarService;
    private final JiraAuthenticationContext authContext;

    public PlanningPokerPanel(SessionService sessionService, TemplateRenderer templateRenderer,
                              DateTimeFormatterFactory dateTimeFormatterFactory, UserFormats userFormats,
                              AvatarService avatarService, JiraAuthenticationContext authContext) {
        this.sessionService = sessionService;
        this.templateRenderer = templateRenderer;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
        this.userFormats = userFormats;
        this.avatarService = avatarService;
        this.authContext = authContext;
    }

    @Override
    public String getHtml(Map<String, Object> context) {
        String issueKey = ((Issue) context.get("issue")).getKey();
        Session session = sessionService.get(issueKey);

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

    public String getCreated(Session session) {
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterFactory.formatter().forLoggedInUser();
        String created = dateTimeFormatter.withStyle(DateTimeStyle.COMPLETE).format(session.getCreated());
        return created;
    }

    public String getAuthorHtml(Session session) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("avatarURL", getAvatarURL(session.getAuthor()));
        UserFormatter userFormatter = userFormats.formatter("avatarFullNameHover");
        return userFormatter.formatUserkey(session.getAuthor().getKey(), "poker-author", params);
    }

    public String getAvatarURL(ApplicationUser user) {
        return avatarService.getAvatarURL(authContext.getUser(), user, Avatar.Size.NORMAL).toString();
    }
}
