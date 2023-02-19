package com.raf.rs.webDomaci3.repository;

import com.raf.rs.webDomaci3.domain.ErrorMessage;
import com.raf.rs.webDomaci3.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    Page<ErrorMessage> findAllByUser(User user, Pageable pageable);
}
