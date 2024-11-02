package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "First name is required.")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    public RegistrationRequest() {}

    public RegistrationRequest(@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email, @NotBlank(message = "First name is required.") String firstName, @NotBlank(message = "Last name is required.") String lastName, @NotBlank(message = "Password is required.") @Size(min = 8, message = "Password must be at least 8 characters long.") String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
