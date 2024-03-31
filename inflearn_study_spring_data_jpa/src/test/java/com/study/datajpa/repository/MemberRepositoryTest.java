package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDTO;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


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

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDTO> map = page.map(member -> new MemberDTO(member.getId(), member.getUserName(), null));

        for (MemberDTO memberDTO : map) {
            System.out.println(memberDTO);
        }

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        //given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamA");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //select Member
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }
}