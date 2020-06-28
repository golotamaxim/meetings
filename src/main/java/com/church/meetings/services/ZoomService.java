package com.church.meetings.services;

import com.church.meetings.dto.ListOfMeetings;
import com.church.meetings.models.MeetingDetails;
import com.church.meetings.models.Participant;
import com.church.meetings.utils.Constants;
import com.sun.javafx.binding.StringFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class ZoomService {

    @Value(("${zoom.token}"))
    private String token;

    @Value("${meeting.participants.url}")
    private String meetingWithParticipantsUrl;

    @Value("${meeting.reports.url}")
    private String allMeetingsUrl;

    @Value("${user.id}")
    private String user;

    LocalDate lastSunday;

    public HttpEntity<MeetingDetails> entity;

    {

    }

    public MeetingDetails getMeetingDetailsFromZoomAccount() {
        setHeadersForResponseEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ListOfMeetings> response = restTemplate.exchange(allMeetingsUrl, HttpMethod.GET, entity, ListOfMeetings.class, setParamsForMeetingDetails());
        System.out.println(response.getStatusCode());
        List<MeetingDetails> lastMeetings = response.getBody().getMeetings();
        System.out.println(lastMeetings.size());

        Optional<MeetingDetails> longMeeting = lastMeetings.stream()
                .filter(meeting -> Constants.LORDS_TABLE.equals(meeting.getTopic()))
                .max(Comparator.comparing(MeetingDetails::getDuration));
        System.out.println(longMeeting.isPresent());

        return longMeeting.orElseThrow(() -> new RuntimeException(StringFormatter.format("There wasn't %s on last sunday", Constants.LORDS_TABLE).toString()));
    }

    public Set<Participant> getParticipantByMeetingUuid(String uuid) {
        return null;
    }

    private Map<String, String> setParamsForMeetingDetails() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", user);
        parameters.put("from", getMeetingDate());
        parameters.put("to", getMeetingDate());
        return parameters;
    }

    private void setHeadersForResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.set("Accept", "application/json");
        entity = new HttpEntity<>(headers);
    }

    private String getMeetingDate() {
        lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return lastSunday.format(formatter);
    }

}
