package com.remind.api.member;

import static com.remind.core.security.annotation.WithMockCustomUserSecurityContextFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.api.member.dto.request.RefreshTokenRequestDto;
import com.remind.core.domain.common.repository.RedisRepository;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.infra.TestContainerConfig;
import com.remind.core.security.annotation.WithMockCustomUser;
import com.remind.core.security.dto.UserDetailsImpl;
import com.remind.core.security.jwt.JwtProvider;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TokenControllerTest extends TestContainerConfig {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.builder()
                .name(TEST_MEMBER_NAME)
                .rolesType(TEST_MEMBER_ROLE_TYPE)
                .build());
    }

    @Test
    @Disabled
    @DisplayName("refresh token 저장.조회 단위 테스트")
    public void refreshTokenUnitTest() {
        //given
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .memberId(TEST_MEMBER_ID)
                .name(TEST_MEMBER_NAME)
                .authorities(Set.of(new SimpleGrantedAuthority(TEST_MEMBER_ROLE_TYPE.name())))
                .build();
        String newRefreshToken = jwtProvider.createRefreshToken(userDetails);

        redisRepository.saveToken(TEST_MEMBER_ID, newRefreshToken);

        //when
        String findToken = (String) redisRepository.getRefreshToken(TEST_MEMBER_ID)
                .get(RedisRepository.REFRESH_TOKEN_KEY);

        //then
        Assertions.assertThat(findToken).isEqualTo(newRefreshToken);

    }

    @Test
    @Disabled
    @WithMockCustomUser
    @DisplayName("refresh token 저장.조회 통합 테스트")
    public void refreshTokenIntegrationTest() throws Exception {
        //given
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .memberId(TEST_MEMBER_ID)
                .name(TEST_MEMBER_NAME)
                .authorities(Set.of(new SimpleGrantedAuthority(TEST_MEMBER_ROLE_TYPE.name())))
                .build();
        String newRefreshToken = jwtProvider.createRefreshToken(userDetails);

        redisRepository.saveToken(TEST_MEMBER_ID, newRefreshToken);
        String httpRequestBody = objectMapper.writeValueAsString(new RefreshTokenRequestDto(newRefreshToken));

        //when,then
        mockMvc.perform(
                        post("/member/refresh")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + newRefreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(httpRequestBody)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
