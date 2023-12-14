package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.reopository.PostRepository;
import com.ll.medium.global.rsData.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
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

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 30, Sort.by(sorts));
        Page<Post> posts = postRepository.findByIsPublished(true, pageable);
        if (posts == null) {
            // 빈 페이지 반환
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return posts;
    }
    public Page<Post> getMyList(int page, Member member) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 30, Sort.by(sorts));
        Page<Post> posts = postRepository.findByMember(member, pageable);
        if (posts == null) {
            // 빈 페이지 반환
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return posts;
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. id=" + postId));
    }
}
