package com.ll.medium.global.rq;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final MemberService memberService;
    private Member member;
    private User user;

    public String redirect(String url, String msg) {
        msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("redirect:").append(url);
        if (msg != null){
            sb.append("?msg=").append(msg);
        }
        return sb.toString();
    }

    public String historyBack(String msg){
        request.setAttribute("failMsg",msg);
        return "global/js";
    }

    public String redirectOrBack(RsData<?> rs, String path){
        if(rs.isFail()) return historyBack(rs.getMsg());
        return redirect(path, rs.getMsg());
    }

    public User getUser(){
        if(user == null) {
            user = Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::getPrincipal)
                    .filter(it -> it instanceof User)
                    .map(it -> (User) it)
                    .orElse(null);
        }
        return user;
    }

    public Member getMember(){
        if(member==null) {
            User user = getUser();
            if (user == null) {
                return null;
            }
            member = memberService
                    .findByUsername(user.getUsername())
                    .orElseThrow(()->new AccessDeniedException("잘못된 사용자 정보입니다."));
        }
        return member;
    }
    public boolean isLogin(){
        return getUser() != null;
    }

    public boolean isLogout(){
        return !isLogin();
    }

    public boolean isAdmin(){
        if(isLogout()) return false;

        return getUser()
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
    }
}
