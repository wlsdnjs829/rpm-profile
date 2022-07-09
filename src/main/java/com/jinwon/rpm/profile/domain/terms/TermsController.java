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

import javax.validation.Valid;

@Tag(name = "약관")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.PATH_V1 + "/terms")
public class TermsController implements BaseController {

    private final TermsService termsService;

    @GetMapping(value = "/default")
    @Operation(summary = "기본 약관 조회")
    public TermsDefaultDto getDefaultTerms() {
        return termsService.getDefaultTermsDto();
    }

    @GetMapping(value = "/{type}")
    @Operation(summary = "약관 상세 조회")
    public TermsDetailDto getTermsDetail(@PathVariable TermsType type) {
        return termsService.getTermsDetail(type);
    }

    @PostMapping
    @Operation(summary = "약관 등록")
    public TermsDetailDto postTerms(@Valid @RequestBody PostTermsDto postTermsDto) {
        return termsService.postTerms(postTermsDto);
    }

}
