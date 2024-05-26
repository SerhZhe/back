package com.example.demo.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Document(collection = "Candidate")
@Getter
public class Candidate {
    @Id
    private String id;

    @Setter
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Setter
    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @Setter
    @NotBlank(message = "Patronymic cannot be empty")
    private String patronymic;

    @Setter
    @NotBlank(message = "Year cannot be empty")
    private Number year;

    @Setter
    @NotBlank(message = "Month cannot be empty")
    private Number month;

    @Setter
    @NotBlank(message = "Day cannot be empty")
    private Number day;

    @Setter
    @NotBlank(message = "Place of birth cannot be empty")
    private String birthPlace;

    @Setter
    @NotBlank(message = "Index cannot be empty")
    private Number popularity;

    @Setter
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }
}