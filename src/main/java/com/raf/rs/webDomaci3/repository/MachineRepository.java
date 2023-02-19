package com.raf.rs.webDomaci3.repository;

import com.raf.rs.webDomaci3.domain.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Query(value = "SELECT * FROM machine m WHERE name like %:name% AND status like %:status% AND " +
            "created_by = :id AND created_date BETWEEN :from AND :to",
            nativeQuery = true)
    Page<Machine> search(Pageable pageable, @Param("name") String name, @Param("status") String status,
                         @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("id") Long id);
}
