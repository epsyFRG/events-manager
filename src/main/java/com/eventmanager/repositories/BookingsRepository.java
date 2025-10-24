package com.eventmanager.repositories;

import com.eventmanager.entities.Booking;
import com.eventmanager.entities.Event;
import com.eventmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByUserAndEvent(User user, Event event);

    List<Booking> findByUser(User user);
}