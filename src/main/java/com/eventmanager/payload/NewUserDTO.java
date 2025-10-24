package com.eventmanager.payload;

import com.eventmanager.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotBlank(message = "il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "il nome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String name,
        @NotBlank(message = "il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "il cognome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String surname,
        @NotBlank(message = "l'email è obbligatoria")
        @Email(message = "l'indirizzo email inserito non è nel formato corretto")
        String email,
        @NotBlank(message = "la password è obbligatoria")
        @Size(min = 4, message = "la password deve avere minimo 4 caratteri")
        String password,
        @NotNull(message = "il ruolo è obbligatorio!")
        Role role) {
}