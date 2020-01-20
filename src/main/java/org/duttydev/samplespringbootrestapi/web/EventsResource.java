package org.duttydev.samplespringbootrestapi.web;

import com.github.javafaker.Faker;
import org.duttydev.samplespringbootrestapi.domain.Event;
import org.duttydev.samplespringbootrestapi.exceptions.ResourceNotFoundException;
import org.duttydev.samplespringbootrestapi.repo.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/api/events")
public class EventsResource {

    private final EventRepository repo;

    public EventsResource(EventRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void init() {

        Faker faker = new Faker();
        Random r = new Random();

        int numEvents = r.nextInt(250 + 1);

        for (int i = 0; i < numEvents; i++) {
            Instant start = faker.date().future(5, TimeUnit.DAYS).toInstant();
            Instant end = faker.date().future(r.nextInt(4+1) + 1, TimeUnit.HOURS, Date.from(start)).toInstant();

            int numGuests = r.nextInt(1 +5);
            List<String> guests = new ArrayList<>(numGuests);
            for (int j = 0; j < numGuests; j++) {
                guests.add(faker.internet().emailAddress());
            }

            Event event = Event.builder()
                    .name(faker.company().name() + " Event")
                    .description(faker.lorem().paragraph())
                    .startTime(start)
                    .endTime(end)
                    .guesEmails(guests)
                    .build();

            repo.save(event);
        }
    }

    @GetMapping
    public Page<Event> getEvents(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "20") int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable String id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @PostMapping
    public Event create(@RequestBody Event event) {
        return repo.save(event);
    }

    @PutMapping("/{id}") 
    public Event update(@PathVariable String id, @RequestBody Event event) {
        event.setId(id);
        return repo.save(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }


}
