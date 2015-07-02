package com.redhat.engineering.plugins.services;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.redhat.engineering.plugins.domain.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author vdedik@redhat.com
 */
public class GroupService extends AbstractPokerService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final PluginSettings pluginSettings;
    private final String key;

    public GroupService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
        this.key = getKey() + ".groups";
    }

    public Group create(Group group) {
        Object currId = this.pluginSettings.get(key + ".currId");
        Long nextId;
        if (currId == null) {
            nextId = 1L;
        } else {
            nextId = Long.parseLong((String) currId) + 1;
        }


        this.pluginSettings.put(key + "." + nextId, toList(group.getNames()));
        this.pluginSettings.put(key + "." + nextId + ".name", group.getName());
        this.pluginSettings.put(key + ".currId", nextId.toString());
        group.setId(nextId);

        return group;
    }

    public void update(Group group) {
        this.pluginSettings.put(key + "." + group.getId(), toList(group.getNames()));
        this.pluginSettings.put(key + "." + group.getId() + ".name", group.getName());
    }

    public void delete(Group group) {
        this.pluginSettings.remove(key + "." + group.getId());
    }

    public Group get(Long id) {
        Group group = new Group();
        group.setId(id);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) this.pluginSettings.get(key + "." + id);
        String name = (String) this.pluginSettings.get(key + "." + id + ".name");

        if (names == null) {
            return null;
        }
        group.setNames(new HashSet<String>(names));
        group.setName(name);

        return group;
    }

    public Set<Group> getAll() {
        Set<Group> groups = new HashSet<Group>();
        Object currIdRaw = this.pluginSettings.get(key + ".currId");

        if (currIdRaw == null) {
            return groups;
        }
        Long currId = Long.parseLong((String) currIdRaw);

        for (Long i = 1L; i <= currId; i++) {
            Group group = get(i);
            if (group != null) {
                groups.add(group);
            }
        }

        return groups;
    }

    private <T> List<T> toList(Set<T> set) {
        return new ArrayList<T>(set);
    }
}
