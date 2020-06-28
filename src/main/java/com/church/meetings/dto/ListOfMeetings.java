package com.church.meetings.dto;

import com.church.meetings.models.MeetingDetails;
import lombok.Data;

import java.util.List;

@Data
public class ListOfMeetings {
    List<MeetingDetails> meetings;
    String from;
    String to;
}
