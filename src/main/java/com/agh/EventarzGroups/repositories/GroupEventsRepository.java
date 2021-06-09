package com.agh.EventarzGroups.repositories;

import com.agh.EventarzGroups.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupEventsRepository extends CrudRepository<Event, String> {

}
