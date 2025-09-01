package com.fitnessapp.userservice.respository;

import com.fitnessapp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);
    boolean existsByKeyClockId(String userId);

    User findByEmail(String email);
}
