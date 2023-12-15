package com.ll.medium.domain.post.post.reopository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop30ByIsPublishedOrderByIdDesc(boolean isPublished);

    Page<Post> findByIsPublished(boolean isPublished ,Pageable pageable);

    Page<Post> findByMember(Member member, Pageable pageable);

    Optional<Post> findById(Long id);
}
