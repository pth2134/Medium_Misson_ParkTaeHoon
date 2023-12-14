package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.reopository.PostRepository;
import com.ll.medium.global.rsData.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public RsData<Post> createPost(Member author, String title, String content, boolean isPublished) {
        Post post = Post.builder()
                .member(author)
                .title(title)
                .content(content)
                .isPublished(isPublished)
                .build();
        postRepository.save(post);
        return RsData.of("200"
                , "글작성이 완료되었습니다."
                , post
        );
    }

    public Object findTop30ByIsPublishedOrderByIdDesc(boolean b) {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(b);
    }

}
