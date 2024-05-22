package com.remind.core.domain.member.repository;

import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByAuthId(Long authId);

    Optional<Member> findByMemberCode(String memberCode);

    @Query("SELECT p " +
            "FROM Connection c " +
            "JOIN c.patient p " +
            "WHERE c.targetMember.id = :targetMemberId AND c.connectionStatus = :connectionStatus")
    List<Member> findPatientInfoByTargetMemberIdAndStatus(@Param("targetMemberId") Long targetMemberId,
                                                              @Param("connectionStatus") ConnectionStatus connectionStatus);



//
//    //위험도가 높은 환자 찾는 쿼리
//    @Query("SELECT new com.remind.api.member.dto.CautionPatientDto(p.id, p.name, 1, 2, p.protectorPhoneNumber, p.phoneNumber) " +
//            "FROM Connection c " +
//            "JOIN c.patient p " +
//            "WHERE c.targetMember.id = :targetMemberId AND c.connectionStatus = 'ACCEPT'")
//    List<CautionPatientDto> findCautionPatients(@Param("targetMemberId") Long targetMemberId);

}
