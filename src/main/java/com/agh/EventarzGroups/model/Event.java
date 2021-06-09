package com.agh.EventarzGroups.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "event")
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;
    @JsonBackReference
    @ManyToOne
    private Group group;
    @Column(name = "event_uuid")
    private String eventUuid;
    @Column(name = "organizer_username")
    private String organizerUsername;

    public Event(Group group, String eventUuid, String organizerUsername) {
        this.group = group;
        this.eventUuid = eventUuid;
        this.organizerUsername = organizerUsername;
    }
}
