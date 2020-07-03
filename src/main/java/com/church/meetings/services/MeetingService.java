package com.church.meetings.services;

import com.church.meetings.models.MeetingDetails;
import com.church.meetings.repositories.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    public void saveMeetingDetails(MeetingDetails meeting) {
        meetingRepository.save(meeting);
    }
}
