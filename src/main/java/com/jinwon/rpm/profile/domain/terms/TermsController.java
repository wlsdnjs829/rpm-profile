package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.terms.dto.PostTermsDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDefaultDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "약관 컨트롤러")
@RequestMapping(value = "/terms")
public class TermsController implements BaseController {

    private final TermsService termsService;

    @GetMapping( value = "/default")
    @Operation(description = "기본 약관 조회")
    public Mono<TermsDefaultDto> getDefaultTerms() {
        return Mono.just(termsService.getDefaultTerms());
    }

    @GetMapping(value = "/{type}}")
    @Operation(description = "약관 상세 조회")
    public Mono<TermsDetailDto> getTermsDetail(@PathVariable TermsType type) {
        return Mono.just(termsService.getTermsDetail(type));
    }

    @PostMapping
    @Operation(description = "약관 등록")
    public Mono<TermsDetailDto> postTerms(@Valid @RequestBody PostTermsDto postTermsDto) {
        return Mono.just(termsService.postTerms(postTermsDto));
    }

}
