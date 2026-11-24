package com.college.events.config;

import com.college.events.model.Event;
import com.college.events.repo.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(EventRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            add(repo, "Hackathon 2026", "24-hour coding challenge. Teams of up to 4.", "Computer Lab 2", 7, 50);
            add(repo, "Tech Talk: AI in Industry", "Guest lecture by an industry expert.", "Main Auditorium", 12, 200);
            add(repo, "Cultural Fest Auditions", "Singing, dance and drama auditions.", "Open Air Theatre", 15, 3);
        };
    }

    private void add(EventRepository repo, String title, String desc, String venue, int daysAhead, int max) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setVenue(venue);
        e.setEventDate(LocalDateTime.now().plusDays(daysAhead).withHour(10).withMinute(0).withSecond(0).withNano(0));
        e.setMaxParticipants(max);
        repo.save(e);
    }
}
