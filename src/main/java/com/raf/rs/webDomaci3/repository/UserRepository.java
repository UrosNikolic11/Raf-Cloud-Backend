package com.raf.rs.webDomaci3.repository;

import com.raf.rs.webDomaci3.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findUserByEmail(String email);
}
