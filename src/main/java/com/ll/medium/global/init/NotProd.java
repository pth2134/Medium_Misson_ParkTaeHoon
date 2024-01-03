package com.ll.medium.global.init;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.stream.IntStream;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.getMemberCount() == 2) {
                memberService.join("user1", "user1", "1234", false);
                memberService.join("user2", "user2", "1234", false);
                IntStream.rangeClosed(5, 6).forEach(i -> {
                    memberService.join("user" + i, "user" + i, "1234", true);
                });
            }


            if (postService.getPostCount() == 0) {
                IntStream.rangeClosed(1, 2).forEach(i -> {
                    postService.createPost(memberService.findByUserId(3).get(), "제목 " + i, "내용 " + i, true, false);
                });
                IntStream.rangeClosed(3, 4).forEach(i -> {
                    postService.createPost(memberService.findByUserId(3).get(), "제목 " + i, "내용 " + i, true, true);
                });
            }
        };
    }
}
