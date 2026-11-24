package com.college.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "email"}))
@Getter @Setter
public class Registration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @NotBlank private String studentName;
    @NotBlank @Email private String email;
    @NotBlank private String rollNo;
    @NotBlank private String branch;

    private LocalDateTime registeredAt = LocalDateTime.now();
}
