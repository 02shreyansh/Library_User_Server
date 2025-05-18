package com.shreyansh.User_Service.dto;

import java.time.Instant;

import com.shreyansh.User_Service.db.User;
import com.shreyansh.User_Service.db.UserRole;
import com.shreyansh.User_Service.db.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String companyName;
    private String profilePicture;
    private UserStatus status;
    private Boolean isVerified;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private UserRole role;
    private Instant emailVerifiedAt;
    private Instant phoneVerifiedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.companyName = user.getCompanyName();
        this.profilePicture = user.getProfilePicture();
        this.status = user.getStatus();
        this.isVerified = user.getIsVerified();
        this.isActive = user.getIsActive();
        this.isEmailVerified = user.getIsEmailVerified();
        this.isPhoneVerified = user.getIsPhoneVerified();
        this.role = user.getRole();
        this.emailVerifiedAt = user.getEmailVerifiedAt();
        this.phoneVerifiedAt = user.getPhoneVerifiedAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}