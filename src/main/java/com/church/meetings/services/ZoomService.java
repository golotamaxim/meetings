package com.church.meetings.services;

import com.church.meetings.dto.ListOfMeetings;
import com.church.meetings.dto.ListOfParticipants;
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
import org.springframework.web.util.UriComponentsBuilder;

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

    public HttpEntity entity;

    public MeetingDetails getMeetingDetailsFromZoomAccount() {
        setHeadersForResponseEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ListOfMeetings> response = restTemplate.exchange(buildUrlForMeetingDetails(allMeetingsUrl),
                HttpMethod.GET, entity, ListOfMeetings.class);
        System.out.println(response.getStatusCode());
        List<MeetingDetails> lastMeetings = response.getBody().getMeetings();

        Optional<MeetingDetails> longMeeting = lastMeetings.stream()
                .filter(meeting -> Constants.LORDS_TABLE.equals(meeting.getTopic()))
                .max(Comparator.comparing(MeetingDetails::getDuration));

        return longMeeting.orElseThrow(() -> new RuntimeException(StringFormatter.format("В это воскресенье не было %s", Constants.LORDS_TABLE).getValue()));
    }

    public Set<Participant> getParticipantByMeetingUuid(String uuid) {
        setHeadersForResponseEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ListOfParticipants> response = restTemplate
                .exchange(meetingWithParticipantsUrl, HttpMethod.GET, entity, ListOfParticipants.class, uuid);
        System.out.println(response.getStatusCode());
        List<Participant> participants = response.getBody().getParticipants();
        return new HashSet<>(participants);
    }

    private Map<String, String> setParamsForMeetingDetails() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", user);
        parameters.put("from", getMeetingDate());
        parameters.put("to", getMeetingDate());
        return parameters;
    }

    private String buildUrlForMeetingDetails(String url) {
        return UriComponentsBuilder.fromHttpUrl(url)
//                .replaceQueryParam("userId", user)
                .queryParam("from", getMeetingDate())
                .queryParam("to", getMeetingDate())
                .buildAndExpand(user)
                .toUriString();
    }

    private void setHeadersForResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.set("Accept", "application/json");
        entity = new HttpEntity<>(headers);
    }

    private String getMeetingDate() {
        lastSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return lastSunday.format(formatter);
    }

}
