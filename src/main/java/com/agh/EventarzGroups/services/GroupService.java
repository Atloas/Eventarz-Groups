package com.agh.EventarzGroups.services;

import com.agh.EventarzGroups.exceptions.GroupNotFoundException;
import com.agh.EventarzGroups.model.Group;
import com.agh.EventarzGroups.model.GroupForm;
import com.agh.EventarzGroups.repositories.GroupRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Retry(name = "GroupServiceRetry")
@CircuitBreaker(name = "GroupServiceCircuitBreaker")
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group getGroupByUuid(String uuid) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        return group;
    }

    public List<Group> getFoundedGroups(String founderUsername) {
        List<Group> groups = groupRepository.findFoundedGroups(founderUsername);
        return groups;
    }

    public List<Group> getJoinedGroups(String memberUsername) {
        List<Group> groups = groupRepository.findJoinedGroups(memberUsername);
        return groups;
    }

    public List<Group> getMyGroups(String username) {
        // Effectively the same as getJoinedGroups since you can't have founded a group and not be in it, at least for now
        List<Group> joinedGroups = groupRepository.findJoinedGroups(username);
        return joinedGroups;
    }

    public List<Group> getGroupsByName(String name) {
        List<Group> groups = groupRepository.findByNameLikeIgnoreCase("%" + name + "%");
        return groups;
    }

    public List<Group> getGroupsByUuids(String[] uuids) {
        List<Group> groups = groupRepository.findByUuidIn(Arrays.asList(uuids));
        return groups;
    }

    public Group createGroup(GroupForm groupForm) {
        Group group = new Group(groupForm);
        group.join(groupForm.getFounderUsername());
        group = groupRepository.save(group);
        return group;
    }

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

    public Group joinGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.join(username);
        group = groupRepository.save(group);
        return group;
    }

    public Group leaveGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.leave(username);
        group = groupRepository.save(group);
        return group;
    }

    public void deleteGroup(String uuid) {
        groupRepository.deleteByUuid(uuid);
    }
}
