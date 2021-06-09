package com.agh.EventarzGroups.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "group")
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;
    private String name;
    private String description;
    @Column(name = "created_date")
    private String createdDate;
    @Column(name = "founder_username")
    private String founderUsername;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    public Group(GroupForm groupForm) {
        this.name = groupForm.getName();
        this.description = groupForm.getDescription();
        this.createdDate = groupForm.getCreatedDate();
        this.founderUsername = groupForm.getFounderUsername();
        this.members = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public void join(String username) {
        this.members.add(new Member(this, username));
    }

    public void leave(String username) {
        for (Member member : members) {
            if (member.getUsername().equals(username)) {
                members.remove(member);
                return;
            }
        }
    }

    public void postEvent(EventForm eventForm) {
        this.events.add(new Event(this, eventForm.getEventUuid(), eventForm.getOrganizerUsername()));
    }

    public void removeEventsByEventUuids(String[] eventUuids) {
        for (String eventUuid : eventUuids) {
            this.removeEventByEventUuid(eventUuid);
        }
    }

    public void removeEventsByUsername(String username) {
        for (Event event : events) {
            if (event.getOrganizerUsername().equals(username)) {
                events.remove(event);
            }
        }
    }

    public void removeEventByEventUuid(String eventUuid) {
        for (Event event : events) {
            if (event.getEventUuid().equals(eventUuid)) {
                events.remove(event);
            }
        }
    }
}
