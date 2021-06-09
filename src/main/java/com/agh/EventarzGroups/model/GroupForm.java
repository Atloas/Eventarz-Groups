package com.agh.EventarzGroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupForm {
    private String name;
    private String description;
    private String founderUsername;
    private String createdDate;
}
