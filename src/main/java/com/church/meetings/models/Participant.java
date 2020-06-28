package com.church.meetings.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "participants")
public class Participant {

    @Id
    String id;
    String name;
    String user_email;
    long userId;
    int duration;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    MeetingDetails meeting;
}
