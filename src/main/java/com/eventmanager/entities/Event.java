package com.eventmanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"bookings"})
public class Event {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String title;
    @Column(length = 1000)
    private String description;
    private LocalDate date;
    private String location;
    @Column(name = "available_seats")
    private int availableSeats;
    @Column(name = "total_seats")
    private int totalSeats;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Booking> bookings;

    public Event(String title, String description, LocalDate date, String location, int totalSeats, User organizer) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.organizer = organizer;
    }
}