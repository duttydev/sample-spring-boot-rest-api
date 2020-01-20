package org.duttydev.samplespringbootrestapi.repo;

import org.duttydev.samplespringbootrestapi.domain.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, String> {
}
