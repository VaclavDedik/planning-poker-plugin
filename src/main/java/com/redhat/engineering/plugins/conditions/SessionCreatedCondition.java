package com.redhat.engineering.plugins.conditions;

import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import com.redhat.engineering.plugins.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author vdedik@redhat.com
 */
public class SessionCreatedCondition implements Condition {
    private static final Logger log = LoggerFactory.getLogger(SessionCreatedCondition.class);

    private final SessionService sessionService;

    public SessionCreatedCondition(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> context) {
        String key = ((Issue) context.get("issue")).getKey();
        return sessionService.get(key) != null;
    }
}
