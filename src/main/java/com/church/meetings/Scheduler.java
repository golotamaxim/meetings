package com.church.meetings;

import com.church.meetings.models.MeetingDetails;
import com.church.meetings.repositories.MeetingRepository;
import com.church.meetings.services.MeetingService;
import com.church.meetings.services.ZoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduler {

    @Autowired
    ZoomService zoomService;

    @Autowired
    MeetingService meetingService;

    @Scheduled(fixedRate = 10000)
    public void runTask() {
        MeetingDetails meeting = zoomService.getMeetingDetailsFromZoomAccount();
        meeting.setParticipants(zoomService.getParticipantByMeetingUuid(meeting.getUuid()));
        meetingService.saveMeetingDetails(meeting);
    }
}
