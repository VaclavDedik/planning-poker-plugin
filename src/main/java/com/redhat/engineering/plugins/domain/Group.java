package com.redhat.engineering.plugins.domain;

import java.util.Set;

/**
 * @author vdedik@redhat.com
 */
public class Group implements Comparable<Group> {
    private Long id;
    private String name;
    private Set<String> names;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Group group) {
        if (getId() == null && group.getId() == null) {
            return 0;
        } else if (getId() == null) {
            return group.getId().compareTo(getId());
        } else {
            return getId().compareTo(group.getId());
        }
    }
}
