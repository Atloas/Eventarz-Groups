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

    public Group(GroupForm groupForm) {
        this.name = groupForm.getName();
        this.description = groupForm.getDescription();
        this.createdDate = groupForm.getCreatedDate();
        this.founderUsername = groupForm.getFounderUsername();
        this.members = new ArrayList<>();
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
}
