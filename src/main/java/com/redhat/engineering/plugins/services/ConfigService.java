package com.redhat.engineering.plugins.services;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vdedik@redhat.com
 */
public class ConfigService extends AbstractPokerService {
    private static final List<String> DEF_VOTES = Arrays.asList("3", "5", "8", "13", "21", "?");

    private final PluginSettings pluginSettings;

    public ConfigService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
    }

    public List<String> getAllowedVotes() {
        List<String> allowedVotes = getList(this.getKey() + ".config.allowedVotes");
        if (allowedVotes.isEmpty()) {
            return DEF_VOTES;
        }
        return allowedVotes;
    }

    public void setAllowedVotes(List<String> allowedVotes) {
        pluginSettings.put(this.getKey() + ".config.allowedVotes", allowedVotes);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList(String storeKey) {
        List<T> result = (List<T>) pluginSettings.get(storeKey);
        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }
}
