package com.agh.EventarzGroups.repositories;

import com.agh.EventarzGroups.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMembersRepository extends CrudRepository<Member, String> {

}
