package com.jinwon.rpm.profile.domain.member;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.dto.JwtTokenDto;
import com.jinwon.rpm.profile.domain.member.dto.LoginDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.NetworkUtil;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 인증 컨트롤러")
@RequestMapping(value = BaseController.PATH_V1 + "/auth")
public class MemberAuthController implements BaseController {

    private final MemberAuthService memberAuthService;

    @GetMapping("/me")
    @Operation(summary = "사용자 정보 조회")
    public Principal member(Principal principal) {
        return principal;
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인 토큰 발급")
    public JwtTokenDto login(HttpServletRequest request, @Valid @RequestBody LoginDto loginDto) {
        final String clientIp = NetworkUtil.getClientIp(request);
        return memberAuthService.login(loginDto, clientIp);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "사용자 토큰 재사용 요청")
    public JwtTokenDto refreshToken(HttpServletRequest request, @Valid @RequestBody JwtTokenDto jwtTokenDto) {
        final String clientIp = NetworkUtil.getClientIp(request);
        return memberAuthService.refreshToken(jwtTokenDto, clientIp);
    }

    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃")
    public Boolean logout(Authentication authentication) {
        final Member member = getMemberOp(authentication)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_MEMBER));

        return memberAuthService.logout(member);
    }

}
