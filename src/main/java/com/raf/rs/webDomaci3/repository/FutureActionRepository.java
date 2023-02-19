package com.raf.rs.webDomaci3.repository;

import com.raf.rs.webDomaci3.domain.FutureAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FutureActionRepository extends JpaRepository<FutureAction, Long> {
}
