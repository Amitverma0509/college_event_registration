package com.college.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String title;

    @Column(length = 1000)
    private String description;

    @NotBlank private String venue;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventDate;

    @Min(value = 1, message = "Limit must be at least 1")
    private int maxParticipants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Registration> registrations = new ArrayList<>();

    @Transient private long registered;

    public long getSeatsLeft() { return Math.max(0, maxParticipants - registered); }
    public boolean isFull() { return registered >= maxParticipants; }
}
