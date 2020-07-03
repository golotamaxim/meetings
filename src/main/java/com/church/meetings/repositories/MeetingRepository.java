package com.church.meetings.repositories;

import com.church.meetings.models.MeetingDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends CrudRepository<MeetingDetails, Long> {

}
