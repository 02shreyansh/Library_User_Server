package com.shreyansh.User_Service.dto;
import java.time.Instant;
import com.shreyansh.User_Service.modal.User;
import com.shreyansh.User_Service.modal.UserRole;
import com.shreyansh.User_Service.modal.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String full_name;
    private String email;
    private Long university_id;
    private UserStatus status;
    private UserRole userRole;
    private Instant lastActivityDate;
    private Instant createdAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.full_name = user.getFull_name();
        this.email = user.getEmail();
        this.university_id = user.getUniversity_id();
        this.status = user.getStatus();
        this.userRole = user.getUserRole();
        this.lastActivityDate = user.getLastActivityDate();
        this.createdAt = user.getCreatedAt();
    }

}
