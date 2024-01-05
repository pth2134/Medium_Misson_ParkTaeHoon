package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rsData.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String nickname, String username, String password, boolean isPaid) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원아이디입니다.");
        }
        if (findByNickname(nickname).isPresent()) {
            return RsData.of("400-3", "이미 존재하는 별명입니다.");
        }
        Member member = Member.builder()
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .isPaid(isPaid)
                .build();
        memberRepository.save(member);

        return RsData.of("200"
                , "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername())
                , member
        );
    }

    private Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findByUserId(long id) {
        return memberRepository.findById(id);
    }

    public long getMemberCount() {
        return memberRepository.count();
    }
}
