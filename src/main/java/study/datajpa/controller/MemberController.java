package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repositroy.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id){
        Optional<Member> member = memberRepository.findById(id);
        Member member1 = member.get();
        return member1.getUsername();
    }

    @PostConstruct
    public void init(){
        for(int i=0; i<100; i++){
        memberRepository.save(new Member("user"+i,i));
        }
    }
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable Member member){
        return member.getUsername();
    }
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable){
        Page<MemberDto> map = memberRepository.findAll(pageable)
                .map(MemberDto::new);
        return map;
    }
}
