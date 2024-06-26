package com.study.datajpa.entity;

import com.study.datajpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();
        
        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member1 : members) {
            System.out.println("member  = " + member1);
            System.out.println("-> member.team = " + member1.getTeam());

        }
    }

    @Test
    public void JpaEventBaseEntity() {
        //given
        Member member = new Member("member1");
        memberRepository.save(member);

        member.setUserName("member2");

        em.flush();
        em.clear();
        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.getCreatedDate = " + findMember.getCreateDate());
        System.out.println("findMember = " + findMember.getLastModifiedDate());
        System.out.println("findMember = " + findMember.getCreatedBy());
        System.out.println("findMember = " + findMember.getLastModifiedBy());

    }
}