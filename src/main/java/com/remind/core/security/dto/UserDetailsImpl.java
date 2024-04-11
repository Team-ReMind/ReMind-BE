package com.remind.core.security.dto;

import com.remind.core.domain.member.Member;
import java.util.Collection;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long memberId;

    private String name;

    private Set<GrantedAuthority> authorities;

    /**
     * Member entity에서 UserDetails 객체 생성 >>> token 생성에 사용됨
     */
    public static UserDetailsImpl fromMember(Member member) {

        Set<GrantedAuthority> roles =
                member.getRolesType() != null ? Set.of(new SimpleGrantedAuthority(member.getRolesType().name())) : null;

        return UserDetailsImpl.builder()
                .memberId(member.getId())
                .name(member.getName())
                .authorities(roles)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
