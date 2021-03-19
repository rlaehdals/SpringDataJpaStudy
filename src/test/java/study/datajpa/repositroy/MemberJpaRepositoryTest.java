package study.datajpa.repositroy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);
        assertThat(member).isEqualTo(saveMember);
    }
    void test2Member(){
        Member member = new Member("userA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(member).isEqualTo(saveMember);
    }
    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        assertThat(member1).isEqualTo(findMember1);
    }
    @Test
    void compare(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberJpaRepository.save(m2);
        memberJpaRepository.save(m1);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("aaa", 15);
        assertThat(result.get(0)).isEqualTo(m2);
    }
    @Test
    void compare2(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        assertThat(result.get(0)).isEqualTo(m2);
    }
    @Test
    void hello(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }
    @Test
    void nameQuery(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberJpaRepository.save(m2);
        memberJpaRepository.save(m1);

        List<Member> findMember = memberJpaRepository.findByUsername("aaa");

        assertThat(findMember.size()).isEqualTo(2);
    }
    @Test
    void nameQuery2(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<Member> findMember = memberRepository.findByUsername("aaa");

        assertThat(findMember.size()).isEqualTo(2);
    }
    @Test
    void jpaNameQuery2(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<Member> findMember = memberRepository.findUser("aaa", 10);

        assertThat(findMember.size()).isEqualTo(1);
    }
    @Test
    void NameQuery3(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<String> findMember = memberRepository.findUsernameList();
        assertThat(findMember.size()).isEqualTo(2);
    }
    @Test
    void NameQuery4(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        Team team = new Team("teamA");
        teamRepository.save(team);
        m1.setTeam(team);
        memberRepository.save(m2);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        assertThat(memberDto.get(0).getUsername()).isEqualTo(m1.getUsername());
    }
    @Test
    void findByNames(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<Member> findMember = memberRepository.findByNames(Arrays.asList("aaa"));

        assertThat(findMember.size()).isEqualTo(1);
    }
    @Test
    void returnType(){
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m2);
        memberRepository.save(m1);

        List<Member> listByUsername = memberRepository.findListByUsername("aaa");
        Member member = memberRepository.findMemberByUsername("aaa");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("aaa");
    }

    @Test
    void paging(){
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));

        int age=10;
        int offset=0;
        int limit=3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long l = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(l).isEqualTo(6);
    }
    @Test
    void springDataPaging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));


        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }
    @Test
    void springDataSlice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));


        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));


        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
    @Test
    void bulkUpdate(){
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 105));
        memberJpaRepository.save(new Member("member4", 104));
        memberJpaRepository.save(new Member("member5", 103));
        memberJpaRepository.save(new Member("member6", 102));

        int i = memberJpaRepository.bulkAgePlus(20);
        assertThat(i).isEqualTo(4);
    }
    @Test
    void bulkUpdate2(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 105));
        memberRepository.save(new Member("member4", 104));
        memberRepository.save(new Member("member5", 103));
        memberRepository.save(new Member("member6", 102));

//        em.clear();
        int i = memberJpaRepository.bulkAgePlus(20);
        assertThat(i).isEqualTo(4);
    }
    @Test
    void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }
    @Test
    void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());

        findMember.setUsername("member2");

        em.flush();
    }
    @Test
    void queryLock(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();
        List<Member> locKByUsername = memberRepository.findLocKByUsername(member1.getUsername());
    }
    @Test
    void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }
    @Test
    void JpaEventBaseEntity()throws Exception{
        Member member1 = new Member("member1");
        memberRepository.save(member1);//prePersist 발생

        Thread.sleep(100);
        member1.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member1.getId()).get();

        findMember.getCreatedDate();
    }
    @Test
    void specBasic(){
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        
        em.persist(m1);
        em.persist(m2);
        
        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.userName("m1").and(MemberSpec.teamName("teamA"));

        List<Member> all = memberRepository.findAll(spec);
    }
    @Test
    void queryByExample(){
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();


        Member member =new Member("m1");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase("age");

        Example<Member> example = Example.of(member,matcher);

        List<Member> result = memberRepository.findAll(example);
    }

    @Test
    void projections(){

        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnly> m11 = memberRepository.findProjectionsByUsername("m1");

//        List<NestedClosedProjections> m12 = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections);

    }
    @Test
    void nativeQuery(){
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result = memberRepository.findByNativeQuery("m1");
    }
    @Test
    void pro(){
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
    }
}