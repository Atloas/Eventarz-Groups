package com.agh.EventarzGroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventForm {

    private String eventUuid;
    private String organizerUsername;
}
