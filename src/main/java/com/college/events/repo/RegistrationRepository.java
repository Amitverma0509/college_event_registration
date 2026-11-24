package com.college.events.repo;

import com.college.events.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    long countByEventId(Long eventId);
    boolean existsByEventIdAndEmail(Long eventId, String email);
    List<Registration> findByEventIdOrderByRegisteredAtAsc(Long eventId);
}
