package com.church.meetings;

import com.church.meetings.models.MeetingDetails;
import com.church.meetings.models.Participant;
import com.church.meetings.services.ZoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EnableScheduling
public class Scheduler {

    @Autowired
    ZoomService zoomService;

    @Scheduled(fixedRate = 10000)
    public void runTask() {
        MeetingDetails meeting = zoomService.getMeetingDetailsFromZoomAccount();
        meeting.setParticipants(zoomService.getParticipantByMeetingUuid(meeting.getUuid()));

    }
}
