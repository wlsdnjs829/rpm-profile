package com.jinwon.rpm.profile.domain.terms_agreement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.member.dto.PostMemberDto;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms.TermsService;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.dto.TermsAgreementDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.TermsAgreementDto;
import com.jinwon.rpm.profile.mock_model.MockModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;

/**
 * 약관 동의 서비스 테스트
 */
@ExtendWith(MockitoExtension.class)
class TermsAgreementServiceTest {

    @InjectMocks
    private TermsAgreementService termsAgreementService;

    @Mock
    private TermsService termsService;

    @Mock
    private TermsAgreementRepository termsAgreementRepository;

    private static final ObjectMapper objectMapper =
            Jackson2ObjectMapperBuilder.json()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .modules(new JavaTimeModule())
                    .build();

    private Member member;

    private Terms termsOfService;
    private Terms privacyCollection;

    private TermsAgreement termsOfServiceAgreement;
    private TermsAgreement privacyCollectionAgreement;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        final PostMemberDto postMemberDto = objectMapper.readValue(MockModel.MEMBER, PostMemberDto.class);
        member = postMemberDto.toEntity();

        termsOfService = objectMapper.readValue(MockModel.TERMS_OF_SERVICE, Terms.class);
        privacyCollection = objectMapper.readValue(MockModel.PRIVACY_COLLECTION, Terms.class);

        termsOfServiceAgreement = objectMapper.readValue(MockModel.TERMS_AGREEMENT, TermsAgreement.class);
        privacyCollectionAgreement =
                objectMapper.readValue(MockModel.PRIVACY_COLLECTION_TERMS_AGREEMENT, TermsAgreement.class);
    }

    @Nested
    @DisplayName("사용자 동의 정보 삭제")
    class DeleteByMember {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("모든 플로우 동작한 경우")
            void case1() {
                termsAgreementService.deleteByMember(member);

                then(termsAgreementRepository)
                        .should(times(1))
                        .deleteByMember(any(Member.class));
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> termsAgreementService.deleteByMember(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.NOT_EXIST_MEMBER.name());

                then(termsAgreementRepository)
                        .should(times(0))
                        .deleteByMember(any(Member.class));
            }

        }

    }

    @Nested
    @DisplayName("약관 동의 상세 조회")
    class GetTermsAgreementDetail {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                given(termsService.getTermsByType(TermsType.TERMS_OF_SERVICE))
                        .willReturn(termsOfService);
            }

            @Test
            @DisplayName("동의 내역이 없는 경우")
            void case1() {
                final TermsAgreementDetailDto termsAgreementDetail =
                        termsAgreementService.getTermsAgreementDetail(member, TermsType.TERMS_OF_SERVICE);

                then(termsAgreementRepository)
                        .should(times(1))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));

                assertThat(termsAgreementDetail).isNotNull();
                assertThat(termsAgreementDetail.agreeDateTime()).isNull();
                assertThat(termsAgreementDetail.agreeType()).isEqualTo(UseType.UNUSED);

                final TermsDetailDto detailDto = termsAgreementDetail.termsDetail();
                assertThat(detailDto).isNotNull();
                assertThat(detailDto.getType()).isEqualTo(TermsType.TERMS_OF_SERVICE);
            }

            @Test
            @DisplayName("동의 내역이 있는 경우")
            void case2() {
                given(termsAgreementRepository.findByMemberAndTerms(any(Member.class), any(Terms.class)))
                        .willReturn(Optional.of(termsOfServiceAgreement));

                final TermsAgreementDetailDto termsAgreementDetail =
                        termsAgreementService.getTermsAgreementDetail(member, TermsType.TERMS_OF_SERVICE);

                then(termsAgreementRepository)
                        .should(times(1))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));

                assertThat(termsAgreementDetail).isNotNull();
                assertThat(termsAgreementDetail.agreeDateTime())
                        .isEqualTo(termsOfServiceAgreement.getLastModifiedDateTime());
                assertThat(termsAgreementDetail.agreeType())
                        .isEqualTo(termsOfServiceAgreement.getAgreeType());

                final TermsDetailDto detailDto = termsAgreementDetail.termsDetail();
                assertThat(detailDto).isNotNull();
                assertThat(detailDto.getType()).isEqualTo(TermsType.TERMS_OF_SERVICE);
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터 사용자가 없는 경우")
            void case1() {
                assertThatThrownBy(() ->
                        termsAgreementService.getTermsAgreementDetail(null, TermsType.TERMS_OF_SERVICE))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.NOT_EXIST_MEMBER.name());

                then(termsService)
                        .should(times(0))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));
            }

            @Test
            @DisplayName("파라미터 타입이 없는 경우")
            void case2() {
                assertThatThrownBy(() ->
                        termsAgreementService.getTermsAgreementDetail(member, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.NOT_EXIST_TERMS.name());

                then(termsService)
                        .should(times(0))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));
            }

        }

    }

    @Nested
    @DisplayName("기본 약관 동의 리스트 저장")
    class PutDefaultTermsAgreements {

        @BeforeEach
        void setUp() {
            lenient().when(termsService.getDefaultTerms())
                    .thenReturn(List.of(termsOfService, privacyCollection));
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("기본 약관 모두 등록한 경우")
            void case1() {
                termsAgreementService.putDefaultTermsAgreements(
                        member, List.of(TermsType.TERMS_OF_SERVICE, TermsType.PRIVACY_COLLECTION));

                then(termsAgreementRepository)
                        .should(times(1))
                        .saveAll(anyList());
            }

            @Test
            @DisplayName("필수 약관만 등록한 경우")
            void case2() {
                termsAgreementService.putDefaultTermsAgreements(member, List.of(TermsType.TERMS_OF_SERVICE));

                then(termsAgreementRepository)
                        .should(times(1))
                        .saveAll(anyList());
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터 사용자가 없는 경우")
            void case1() {
                final List<TermsType> termsTypes = List.of(TermsType.TERMS_OF_SERVICE);

                assertThatThrownBy(() -> termsAgreementService.putDefaultTermsAgreements(null, termsTypes))
                        .isInstanceOf(IllegalArgumentException.class);

                then(termsService)
                        .should(times(0))
                        .getDefaultTerms();

                then(termsAgreementRepository)
                        .should(times(0))
                        .saveAll(anyList());
            }

            @Test
            @DisplayName("파라미터 사용자가 없는 경우")
            void case2() {
                assertThatThrownBy(() -> termsAgreementService.putDefaultTermsAgreements(member, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PARAM.name());

                then(termsService)
                        .should(times(0))
                        .getDefaultTerms();

                then(termsAgreementRepository)
                        .should(times(0))
                        .saveAll(anyList());
            }

            @Test
            @DisplayName("필수 약관을 동의하지 않는 경우")
            void case3() {
                final List<TermsType> termsTypes = List.of(TermsType.PRIVACY_COLLECTION);

                assertThatThrownBy(() -> termsAgreementService.putDefaultTermsAgreements(member, termsTypes))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.DONT_MEET_TERMS_OF_CONSENT.name());

                then(termsService)
                        .should(times(1))
                        .getDefaultTerms();

                then(termsAgreementRepository)
                        .should(times(0))
                        .saveAll(anyList());
            }

        }

    }

    @Nested
    @DisplayName("약관 동의 생성 / 수정")
    class PutTermsAgreement {

        private TermsAgreementDto privacyDto;
        private TermsAgreementDto termsOfServiceDto;
        private TermsAgreementDto notAgreeTermsOfServiceDto;

        @BeforeEach
        void setUp() {
            privacyDto = new TermsAgreementDto(member, TermsType.PRIVACY_COLLECTION, UseType.UNUSED);
            termsOfServiceDto = new TermsAgreementDto(member, TermsType.TERMS_OF_SERVICE, UseType.USE);
            notAgreeTermsOfServiceDto = new TermsAgreementDto(member, TermsType.TERMS_OF_SERVICE, UseType.UNUSED);

            lenient().when(termsService.getTermsByType(eq(TermsType.PRIVACY_COLLECTION)))
                    .thenReturn(privacyCollection);

            lenient().when(termsService.getTermsByType(eq(TermsType.TERMS_OF_SERVICE)))
                    .thenReturn(termsOfService);

            lenient().when(termsAgreementRepository.findByMemberAndTerms(any(Member.class), any(Terms.class)))
                    .thenReturn(Optional.empty());
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("새로 등록하는 선택 동의인 경우")
            void case1() {
                termsAgreementService.putTermsAgreement(privacyDto);

                then(termsService)
                        .should(times(1))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(1))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));

                then(termsAgreementRepository)
                        .should(times(1))
                        .save(any(TermsAgreement.class));
            }

            @Test
            @DisplayName("기존에 등록한 선택 동의인 경우")
            void case2() {
                lenient().when(termsAgreementRepository.findByMemberAndTerms(any(Member.class), eq(privacyCollection)))
                        .thenReturn(Optional.of(privacyCollectionAgreement));

                termsAgreementService.putTermsAgreement(privacyDto);

                then(termsService)
                        .should(times(1))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(1))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .save(any(TermsAgreement.class));
            }

            @Test
            @DisplayName("새로 등록하는 필수 동의인 경우")
            void case3() {
                termsAgreementService.putTermsAgreement(termsOfServiceDto);

                then(termsService)
                        .should(times(1))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(1))
                        .findByMemberAndTerms(any(Member.class), any(Terms.class));

                then(termsAgreementRepository)
                        .should(times(1))
                        .save(any(TermsAgreement.class));
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> termsAgreementService.putTermsAgreement(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PARAM.name());

                then(termsService)
                        .should(times(0))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .save(any(TermsAgreement.class));
            }

            @Test
            @DisplayName("필수 동의임에도 미동의 처리한 경우")
            void case2() {
                assertThatThrownBy(() -> termsAgreementService.putTermsAgreement(notAgreeTermsOfServiceDto))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.DONT_MEET_TERMS_OF_CONSENT.name());

                then(termsService)
                        .should(times(1))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .save(any(TermsAgreement.class));
            }

            @Test
            @DisplayName("이미 등록된 동의에 동의 타입이 같은 경우")
            void case3() {
                lenient().when(termsAgreementRepository.findByMemberAndTerms(any(Member.class), eq(termsOfService)))
                        .thenReturn(Optional.of(termsOfServiceAgreement));

                assertThatThrownBy(() -> termsAgreementService.putTermsAgreement(termsOfServiceDto))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.SAME_AGREE_TYPE.name());

                then(termsService)
                        .should(times(1))
                        .getTermsByType(any(TermsType.class));

                then(termsAgreementRepository)
                        .should(times(0))
                        .save(any(TermsAgreement.class));
            }

        }

    }

}