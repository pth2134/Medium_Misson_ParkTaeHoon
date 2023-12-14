package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData.RsData;
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
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/write")
    public String showWrite(){
        return "domain/post/post/write";
    }

    @Setter
    @Getter
    public static class PostForm{
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private boolean isPublished;
    }

    @PostMapping("/write")
    public String write(@Valid PostForm postForm , Rq rq){
        RsData<Post> postRs = postService.createPost(rq.getMember()
                ,postForm.getTitle()
        ,postForm.getContent()
        ,postForm.isPublished);
        return rq.redirectOrBack(postRs,"/");
    }
}
