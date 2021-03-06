package com.church.meetings.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name = "meetings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingDetails {

    @Id
    String uuid;
    String topic;
    String user_name;
    int duration;
    String host_id;
    int participants_count;
    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Participant> participants;

}