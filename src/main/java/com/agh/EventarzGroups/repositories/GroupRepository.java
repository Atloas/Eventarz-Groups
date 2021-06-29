package com.agh.EventarzGroups.repositories;

import com.agh.EventarzGroups.model.Group;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<Group, String> {

    Group findByUuid(String uuid);

    List<Group> findByUuidIn(List<String> uuids);

    List<Group> findByNameLikeIgnoreCase(String name);

    @Query("SELECT g FROM group g WHERE g.founderUsername = :username ")
    List<Group> findFoundedGroups(String username);

    @Query("SELECT g FROM group g INNER JOIN g.members m WHERE m.username = :username ")
    List<Group> findJoinedGroups(String username);

    @Modifying
    void deleteByUuid(String uuid);
}
