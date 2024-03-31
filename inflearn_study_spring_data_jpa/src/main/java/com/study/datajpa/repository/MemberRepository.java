package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDTO;
import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByUserNameAndAgeGreaterThan(String username, int age);

    @Query("select m.userName from Member m")
    List<String> findUsernameList();

    @Query("select new com.study.datajpa.dto.MemberDTO(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDto();

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
