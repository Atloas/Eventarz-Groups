package com.agh.EventarzGroups.services;

import com.agh.EventarzGroups.exceptions.GroupNotFoundException;
import com.agh.EventarzGroups.model.Group;
import com.agh.EventarzGroups.model.GroupForm;
import com.agh.EventarzGroups.repositories.GroupRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Group getGroupByUuid(String uuid) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        return group;
    }

    @Transactional(readOnly = true)
    public List<Group> getFoundedGroups(String founderUsername) {
        List<Group> groups = groupRepository.findFoundedGroups(founderUsername);
        return groups;
    }

    @Transactional(readOnly = true)
    public List<Group> getJoinedGroups(String memberUsername) {
        List<Group> groups = groupRepository.findJoinedGroups(memberUsername);
        return groups;
    }

    @Transactional(readOnly = true)
    public List<Group> getMyGroups(String username) {
        // Effectively the same as getJoinedGroups since you can't have founded a group and not be in it, at least for now
        List<Group> joinedGroups = groupRepository.findJoinedGroups(username);
        return joinedGroups;
    }

    @Transactional(readOnly = true)
    public List<Group> getGroupsByName(String name) {
        List<Group> groups = groupRepository.findByNameLikeIgnoreCase("%" + name + "%");
        return groups;
    }

    @Transactional(readOnly = true)
    public List<Group> getGroupsByUuids(String[] uuids) {
        List<Group> groups = groupRepository.findByUuidIn(Arrays.asList(uuids));
        return groups;
    }

    @Transactional
    public Group createGroup(GroupForm groupForm) {
        Group group = new Group(groupForm);
        group.join(groupForm.getFounderUsername());
        group = groupRepository.save(group);
        return group;
    }

    @Transactional
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

    @Transactional
    public Group joinGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.join(username);
        group = groupRepository.save(group);
        return group;
    }

    @Transactional
    public Group leaveGroup(String uuid, String username) throws GroupNotFoundException {
        Group group = groupRepository.findByUuid(uuid);
        if (group == null) {
            throw new GroupNotFoundException("Group " + uuid + " not found!");
        }
        group.leave(username);
        group = groupRepository.save(group);
        return group;
    }

    @Transactional
    public void deleteGroup(String uuid) {
        groupRepository.deleteByUuid(uuid);
    }
}
