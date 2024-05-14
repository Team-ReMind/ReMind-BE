//package com.remind.core.domain.observation;
//
//import com.remind.core.domain.member.Member;
//import com.remind.core.domain.connection.enums.ConnectionStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Observation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "observation_id")
//    private Long id;
//
//    @Enumerated(value = EnumType.STRING)
//    private ConnectionStatus connectionStatus;
//
//    @ManyToOne
//    @JoinColumn(name = "patient_id")
//    private Member patient;
//
//    @ManyToOne
//    @JoinColumn(name = "center_id")
//    private Member center;
//
//}
