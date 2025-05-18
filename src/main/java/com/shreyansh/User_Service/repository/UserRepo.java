package com.shreyansh.User_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shreyansh.User_Service.db.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    public User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.university_id = :universityId")
    User findByUniversityId(@Param("universityId") Long universityId);
}
