package com.redhat.engineering.plugins.actions;

import com.redhat.engineering.plugins.services.ConfigService;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class ConfigAction extends AbstractAction {
    private final ConfigService configService;

    private String allowedVotes;

    public ConfigAction(ConfigService configService) {
        this.configService = configService;
    }

    public String getAllowedVotes() {
        return allowedVotes;
    }

    public void setAllowedVotes(String allowedVotes) {
        this.allowedVotes = allowedVotes;
    }

    public List<String> parseAllowedVotes(String rawAllowedVotes) {
        return Arrays.asList(rawAllowedVotes.trim().split(","));
    }

    public String formatAllowedVotes(List<String> allowedVotes) {
        String result = "";
        for (Iterator<String> i = allowedVotes.iterator(); i.hasNext();) {
            result += i.next();
            if (i.hasNext()) {
                result += ",";
            }
        }
        return result;
    }

    @Override
    public String doDefault() throws Exception {
        setAllowedVotes(formatAllowedVotes(configService.getAllowedVotes()));
        return INPUT;
    }

    @Override
    public String doExecute() throws Exception {
        configService.setAllowedVotes(parseAllowedVotes(getAllowedVotes()));
        addMessage("Settings sucessfully saved.");
        return INPUT;
    }
}
