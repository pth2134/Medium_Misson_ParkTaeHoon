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
    private PostService postService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            memberService.join("user1", "1234");
            memberService.join("user2", "1234");

            IntStream.rangeClosed(7, 50).forEach(i -> {
                postService.createPost(memberService.findByUsername("user1").get(), "제목 " + i, "내용 " + i, true);
            });

        };
    }
}
