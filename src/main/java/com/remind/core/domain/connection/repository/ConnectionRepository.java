package com.remind.core.domain.connection.repository;

import com.remind.core.domain.connection.Connection;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Optional<Connection> findByTargetMemberIdAndPatientId(Long TargetMemberId, Long patientId);

}
