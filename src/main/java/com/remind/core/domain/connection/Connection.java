package com.remind.core.domain.connection;

import com.remind.core.domain.member.Member;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Member patient;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    @Enumerated(value = EnumType.STRING)
    private ConnectionStatus connectionStatus;

    public void updateConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


}
