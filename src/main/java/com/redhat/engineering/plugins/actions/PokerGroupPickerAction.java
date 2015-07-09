package com.redhat.engineering.plugins.actions;

import com.redhat.engineering.plugins.domain.Group;
import com.redhat.engineering.plugins.services.GroupService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author vdedik@redhat.com
 */
public class PokerGroupPickerAction extends AbstractAction {

    private GroupService groupService;

    // props
    private String userList;
    private String element;
    private String id;
    private String name;
    private Boolean edit = false;

    public PokerGroupPickerAction(GroupService groupService) {
        this.groupService = groupService;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    @Override
    public String doExecute() throws Exception {
        return INPUT;
    }

    public String doCreate() throws Exception {
        return "create";
    }

    public String doSave() throws Exception {
        Set<String> userList = parseUserList();
        Group group = new Group();
        if (edit) {
            group.setId(Long.parseLong(getId()));
        }
        group.setNames(userList);
        group.setName(getName());

        if (edit) {
            groupService.update(group);
            addMessage("Group " + group.getName() + " successfully updated.");
        } else {
            groupService.create(group);
            addMessage("Group " + group.getName() + " successfully created.");
        }

        return SUCCESS;
    }

    public String doEdit() throws Exception {
        this.edit = true;
        Group group = groupService.get(Long.parseLong(getId()));
        setName(group.getName());
        setUserList(join(group.getNames()));
        return "create";
    }

    public String doDelete() throws Exception {
        Long id = Long.parseLong(getId());
        Group group = groupService.get(id);
        this.groupService.delete(group);
        addMessage("Group " + group.getName() + " successfully deleted.");
        return SUCCESS;
    }

    public Set<Group> getGroups() {
        return new TreeSet<Group>(groupService.getAll());
    }

    public String join(Iterable<String> strings) {
        String delim = "";
        StringBuilder sb = new StringBuilder();
        for (String i : strings) {
            sb.append(delim).append(i);
            delim = ", ";
        }
        return sb.toString();
    }

    private Set<String> parseUserList() {

        Set<String> result = new HashSet<String>();
        if (!"".equals(getUserList())) {
            String[] rawResult = getUserList().split(",");
            for (String r : rawResult) {
                result.add(r.trim());
            }
        }

        return result;
    }
}
