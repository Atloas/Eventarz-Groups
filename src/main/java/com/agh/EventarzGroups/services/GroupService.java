package com.agh.EventarzGroups.services;

import com.agh.EventarzGroups.exceptions.GroupNotFoundException;
import com.agh.EventarzGroups.model.EventForm;
import com.agh.EventarzGroups.model.Group;
import com.agh.EventarzGroups.model.GroupForm;
import com.agh.EventarzGroups.repositories.GroupRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Retry(name = "getGroupByUuidRetry")
    public Group getGroupByUuid(String uuid) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        return group;
    }

    @Retry(name = "getFoundedGroupsRetry")
    public List<Group> getFoundedGroups(String founderUsername) {
        List<Group> groups = groupRepository.findFoundedGroups(founderUsername);
        return groups;
    }

    @Retry(name = "getJoinedGroupsRetry")
    public List<Group> getJoinedGroups(String memberUsername) {
        List<Group> groups = groupRepository.findJoinedGroups(memberUsername);
        return groups;
    }

    @Retry(name = "getMyGroupsRetry")
    public List<Group> getMyGroups(String username) {
        // TODO: Do it in one query??
        List<Group> foundedGroups = groupRepository.findFoundedGroups(username);
        List<Group> joinedGroups = groupRepository.findJoinedGroups(username);
        List<Group> groups = new ArrayList<>(foundedGroups);
        groups.addAll(joinedGroups);
        return groups;
    }

    @Retry(name = "getGroupsByNameRetry")
    public List<Group> getGroupsByName(String name) {
        List<Group> groups = groupRepository.findByNameLikeIgnoreCase(name);
        return groups;
    }

    @Retry(name = "getGroupsByUuidsRetry")
    public List<Group> getGroupsByUuids(String[] uuids) {
        List<Group> groups = groupRepository.findByUuidIn(Arrays.asList(uuids));
        return groups;
    }

    @Retry(name = "createGroupRetry")
    public Group createGroup(GroupForm groupForm) {
        Group group = new Group(groupForm);
        group.join(groupForm.getFounderUsername());
        group = groupRepository.save(group);
        return group;
    }

    @Retry(name = "updateGroupRetry")
    public Group updateGroup(String uuid, GroupForm groupForm) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.setName(groupForm.getName());
        group.setDescription(groupForm.getDescription());
        group = groupRepository.save(group);
        return group;
    }

    @Retry(name = "joinGroupRetry")
    public Group joinGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.join(username);
        group = groupRepository.save(group);
        return group;
    }

    @Retry(name = "leaveGroupRetry")
    public Group leaveGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.leave(username);
        group = groupRepository.save(group);
        return group;
    }

    @Retry(name = "deleteGroupRetry")
    public void deleteGroup(String uuid) {
        groupRepository.deleteByUuid(uuid);
    }

    @Retry(name = "postEventRetry")
    public void postEvent(String uuid, EventForm eventForm) {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.postEvent(eventForm);
        groupRepository.save(group);
    }

    @Retry(name = "removeEventRetry")
    public void removeEvent(String uuid, String[] eventUuids) {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.removeEventsByEventUuids(eventUuids);
        groupRepository.save(group);
    }
}
