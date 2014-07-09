package com.redhat.engineering.plugins.panels;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.web.model.WebPanel;
import com.atlassian.templaterenderer.TemplateRenderer;
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

    public PlanningPokerPanel(SessionService sessionService, TemplateRenderer templateRenderer,
                              DateTimeFormatterFactory dateTimeFormatterFactory) {
        this.sessionService = sessionService;
        this.templateRenderer = templateRenderer;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
    }

    @Override
    public String getHtml(Map<String, Object> context) {
        String issueKey = ((Issue) context.get("issue")).getKey();
        Session session = sessionService.get(issueKey);

        context.put("session", session);
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterFactory.formatter().forLoggedInUser();
        String created = dateTimeFormatter.withStyle(DateTimeStyle.COMPLETE).format(session.getCreated());
        context.put("created", created);

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
}
