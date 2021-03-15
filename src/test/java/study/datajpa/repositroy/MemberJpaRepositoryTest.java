package study.datajpa.repositroy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired MemberRepository memberRepository;
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
}