package com.example.getawaydrives.repositories;

import com.example.getawaydrives.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Qualifier("user")
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    List<User> findByNameContaining (String kw);
    List<User> findUserByEmail(String email);
    List<User> findUserById(int id);
}
