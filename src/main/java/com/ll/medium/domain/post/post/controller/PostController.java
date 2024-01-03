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
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/write")
    @PreAuthorize("isAuthenticated()")
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

        private Boolean isPublished;
    }

    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String write(@Valid PostForm postForm){
        RsData<Post> postRs = postService.createPost(rq.getMember()
                ,postForm.getTitle()
        ,postForm.getContent()
        ,postForm.getIsPublished());
        return rq.redirectOrBack(postRs,"/");
    }

    @GetMapping("/list")
    public String showList(@RequestParam(defaultValue = "1") int page) {
        Page<Post> paging = postService.getList(page);
        rq.setAttribute("page",paging);

        return "domain/post/post/list";
    }

    @GetMapping("/myList")
    @PreAuthorize("isAuthenticated()")
    public String myList(@RequestParam(defaultValue = "1") int page) {
        Page<Post> paging = postService.getMyList(page,rq.getMember());
        rq.setAttribute("page",paging);

        return "domain/post/post/list";
    }

    @GetMapping("/list/nickname/{kw}")
    public String searchList(@PathVariable String kw
                             ,@RequestParam(defaultValue = "1") int page) {
        Page<Post> paging = postService.getSearchListByNickname(kw,page);
        rq.setAttribute("page",paging);

        return "domain/post/post/list";
    }

    @GetMapping("/{postId}")
    public String PostDetail(@PathVariable Long postId){
        Post post = postService.getPostById(postId);
        rq.setAttribute("post",post);
        return "domain/post/post/post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    String showModify(@PathVariable long id) {
        Post post = postService.getPostById(id);

        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");

        rq.setAttribute("post", post);

        return "domain/post/post/modify";
    }
    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private Boolean isPublished;

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    String modify(@PathVariable long id, @Valid ModifyForm modifyForm) {
        Post post = postService.getPostById(id);

        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");

        RsData<Post> postRs = postService.modify(post,modifyForm.getTitle(),modifyForm.getContent(),modifyForm.getIsPublished());
        postService.modify(post, modifyForm.title, modifyForm.content, modifyForm.isPublished);

        return rq.redirectOrBack(postRs,"/");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    String DeletePost(@PathVariable long id) {
        Post post = postService.getPostById(id);

        if (!postService.canModify(rq.getMember(), post)) throw new RuntimeException("수정권한이 없습니다.");

        rq.setAttribute("post", post);

        return rq.redirect("/", "%d번 글이 삭제되었습니다.".formatted(id));
    }
}
