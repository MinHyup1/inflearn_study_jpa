package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDTO;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        int totalCount = memberRepository.findAll().size();
        assertThat(totalCount).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        int afterDeleteCount = memberRepository.findAll().size();
        assertThat(afterDeleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndGreaterThen() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findAllByUserNameAndAgeGreaterThan("AAA", 15);

        assertThat(result.getFirst().getUserName()).isEqualTo("AAA");
        assertThat(result.getFirst().getAge()).isEqualTo(20);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String name : usernameList) {
            System.out.println(name);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA", 20);
        m1.setTeam(team);
        memberRepository.save(m1);
        memberRepository.save(m2);


        List<MemberDTO> memberDto = memberRepository.findMemberDto();
        for (MemberDTO memberDTO : memberDto) {
            System.out.println(memberDTO);
        }
    }
    @Test
    public void findByNames() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA", 20);
        m1.setTeam(team);
        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> byNames = memberRepository.findByNames(List.of("AAA","BBB"));
        for (Member byName : byNames) {
            System.out.println(byName);
        }
    }
}