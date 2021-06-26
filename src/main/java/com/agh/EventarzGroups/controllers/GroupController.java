/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agh.EventarzGroups.controllers;

import com.agh.EventarzGroups.exceptions.GroupNotFoundException;
import com.agh.EventarzGroups.model.Group;
import com.agh.EventarzGroups.model.GroupForm;
import com.agh.EventarzGroups.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/groups", params = {"founderUsername"})
    public List<Group> getFoundedGroups(@RequestParam String founderUsername) {
        return groupService.getFoundedGroups(founderUsername);
    }

    @GetMapping(value = "/groups", params = {"memberUsername"})
    public List<Group> getJoinedGroups(@RequestParam String memberUsername) {
        return groupService.getJoinedGroups(memberUsername);
    }

    @GetMapping(value = "/groups", params = {"username"})
    public List<Group> getMyGroups(@RequestParam String username) {
        return groupService.getMyGroups(username);
    }

    @GetMapping(value = "/groups", params = {"name"})
    public List<Group> getGroupsByName(@RequestParam String name) {
        return groupService.getGroupsByName(name);
    }

    @GetMapping(value = "/groups", params = {"uuids"})
    public List<Group> getGroupsByName(@RequestParam String[] uuids) {
        return groupService.getGroupsByUuids(uuids);
    }

    @PostMapping("/groups")
    public Group createGroup(@RequestBody GroupForm groupForm) {
        return groupService.createGroup(groupForm);
    }

    @GetMapping("/groups/{uuid}")
    public Group getGroupByUuid(@PathVariable String uuid) {
        try {
            return groupService.getGroupByUuid(uuid);
        } catch (GroupNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found!", e);
        }
    }

    @PutMapping("/groups/{uuid}")
    public Group updateGroup(@PathVariable String uuid, @RequestBody GroupForm groupForm) {
        try {
            return groupService.updateGroup(uuid, groupForm);
        } catch (GroupNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found!", e);
        }
    }

    @DeleteMapping("/groups/{uuid}")
    public void deleteGroup(@PathVariable String uuid) {
        groupService.deleteGroup(uuid);
    }

    @PostMapping("/groups/{uuid}/members")
    public Group joinGroup(@PathVariable String uuid, @RequestBody String username) {
        try {
            return groupService.joinGroup(uuid, username);
        } catch (GroupNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found!", e);
        }
    }

    @DeleteMapping("/groups/{uuid}/members/{username}")
    public Group leaveGroup(@PathVariable String uuid, @PathVariable String username) {
        try {
            return groupService.leaveGroup(uuid, username);
        } catch (GroupNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found!", e);
        }
    }
}
