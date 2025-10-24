package com.eventmanager.repositories;

import com.eventmanager.entities.Event;
import com.eventmanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
    Page<Event> findByOrganizer(User organizer, Pageable pageable);
}