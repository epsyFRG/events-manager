package com.eventmanager.payload;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NewEventDTO(
        @NotBlank(message = "il titolo è obbligatorio")
        @Size(min = 3, max = 100, message = "il titolo deve avere una lunghezza compresa tra 3 e 100 caratteri")
        String title,
        @NotBlank(message = "la descrizione è obbligatoria")
        @Size(min = 10, max = 1000, message = "la descrizione deve avere una lunghezza compresa tra 10 e 1000 caratteri")
        String description,
        @NotNull(message = "la data è obbligatoria")
        @Future(message = "la data deve essere futura!")
        LocalDate date,
        @NotBlank(message = "il luogo è obbligatorio!")
        @Size(min = 3, max = 100, message = "il luogo deve avere una lunghezza compresa tra 3 e 100 caratteri")
        String location,
        @NotNull(message = "il numero di posti è obbligatorio!")
        @Min(value = 1, message = "deve esserci almeno un posto disponibile")
        Integer totalSeats) {
}