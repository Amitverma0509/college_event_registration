package com.college.events.service;

import com.college.events.model.Event;
import com.college.events.model.Registration;
import com.college.events.repo.EventRepository;
import com.college.events.repo.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository events;
    private final RegistrationRepository regs;

    public EventService(EventRepository events, RegistrationRepository regs) {
        this.events = events;
        this.regs = regs;
    }

    private Event withCount(Event e) {
        e.setRegistered(regs.countByEventId(e.getId()));
        return e;
    }

    public List<Event> upcoming() {
        List<Event> list = events.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now());
        list.forEach(this::withCount);
        return list;
    }

    public List<Event> all() {
        List<Event> list = events.findAllByOrderByEventDateAsc();
        list.forEach(this::withCount);
        return list;
    }

    public Event get(Long id) {
        return withCount(events.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found")));
    }

    @Transactional
    public void save(Event e) { events.save(e); }

    @Transactional
    public void delete(Long id) { events.deleteById(id); }

    @Transactional
    public void register(Long eventId, Registration r) {
        Event e = events.findByIdForUpdate(eventId)
                .orElseThrow(() -> new IllegalStateException("Event not found"));
        if (e.getEventDate().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Registration is closed for this event.");
        r.setId(null);
        r.setEmail(r.getEmail().trim().toLowerCase());
        if (regs.existsByEventIdAndEmail(eventId, r.getEmail()))
            throw new IllegalStateException("This email is already registered for the event.");
        if (regs.countByEventId(eventId) >= e.getMaxParticipants())
            throw new IllegalStateException("Sorry, this event is full.");
        r.setEvent(e);
        regs.save(r);
    }

    public List<Registration> registrationsFor(Long eventId) {
        return regs.findByEventIdOrderByRegisteredAtAsc(eventId);
    }

    @Transactional
    public Long cancel(Long registrationId) {
        Registration r = regs.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        Long eventId = r.getEvent().getId();
        regs.delete(r);
        return eventId;
    }
}
