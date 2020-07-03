package com.church.meetings.dto;

import com.church.meetings.models.Participant;
import lombok.Data;

import java.util.List;

@Data
public class ListOfParticipants {
    List<Participant> participants;
}
