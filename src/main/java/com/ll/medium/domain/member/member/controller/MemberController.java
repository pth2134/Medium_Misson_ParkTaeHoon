package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;
    @GetMapping("/join")
    public String showJoin(){
        return "domain/member/member/join";
    }

    @Setter
    @Getter
    public static class JoinForm{
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @PostMapping("/join")
    public String signup(@Valid JoinForm joinForm){
        Member member = memberService.join(joinForm.getUsername(), joinForm.getPassword());
        if(member == null){
            return rq.historyBack("이미 존재하는 회원입니다.");
        }
        return rq.redirect("/",
                "%d님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getId())
        );
    }
}
