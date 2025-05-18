package com.shreyansh.User_Service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestHandler {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupDto {
        private String email;
        private String password;
        private String companyName;
        private Long id;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SigninDto {
        private String email;
        private String password;
        private Long id;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponseDto {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String companyName;
        private Long id;
    }
    public static class ImmutableSignupDto {
        private final String email;
        private final String password;
        private final String companyName;
        private final Long id;

        public ImmutableSignupDto(String email, String password, String companyName, Long id) {
            this.email = email;
            this.password = password;
            this.companyName = companyName;
            this.id = id;
        }

        // Getters
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getCompanyName() { return companyName; }
        public Long getId() { return id; }
    }

    public static class ImmutableAuthResponseDto {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phoneNumber;
        private final String companyName;
        private final Long id;

        public ImmutableAuthResponseDto(String firstName, String lastName, String email, 
                                      String phoneNumber, String companyName, Long id) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.companyName = companyName;
            this.id = id;
        }

        // Getters
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getCompanyName() { return companyName; }
        public Long getId() { return id; }
    }
}
