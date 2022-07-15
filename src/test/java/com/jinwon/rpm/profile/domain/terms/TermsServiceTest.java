package com.jinwon.rpm.profile.domain.terms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.terms.dto.PostTermsDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDefaultDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDto;
import com.jinwon.rpm.profile.mock_model.MockModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

/**
 * 약관 서비스 테스트 코드
 */
@ExtendWith(MockitoExtension.class)
class TermsServiceTest {

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private TermsRepositoryImpl termsRepositoryImpl;

    private TermsService termsService;

    private Terms TERMS_OF_SERVICE;
    private Terms PRIVACY_COLLECTION;
    private Terms RECEIVE_MARKETING_INFO;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Terms terms = mock(Terms.class);
    private static final List<Terms> termsList = List.of(terms);

    @BeforeEach
    void setUp() throws JsonProcessingException {
        termsService = new TermsService(termsRepository, termsRepositoryImpl);

        lenient().when(termsRepositoryImpl.findListUseTerms(anyList()))
                .thenReturn(termsList);

        TERMS_OF_SERVICE = objectMapper.readValue(MockModel.TERMS_OF_SERVICE, Terms.class);
        PRIVACY_COLLECTION = objectMapper.readValue(MockModel.PRIVACY_COLLECTION, Terms.class);
        RECEIVE_MARKETING_INFO = objectMapper.readValue(MockModel.RECEIVE_MARKETING_INFO, Terms.class);
    }

    @Nested
    @DisplayName("기본 약관 객체 조회")
    class GetDefaultTermsDto {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("기본 약관 조회 및 주입 받은 숫자 맞는 경우")
            void case1() {
                final TermsDefaultDto defaultTermsDto = termsService.getDefaultTermsDto();
                assertThat(defaultTermsDto).isNotNull();

                final List<TermsDto> defaultTerms = defaultTermsDto.terms();
                assertThat(defaultTerms)
                        .isNotEmpty()
                        .hasSize(termsList.size());
            }

            @RepeatedTest(3)
            @DisplayName("기본 반복 테스트")
            void case2() {
                case1();
            }

        }

    }

    @Nested
    @DisplayName("기본 약관 조회")
    class GetDefaultTerms {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("기본 약관 조회 및 주입 받은 숫자 맞는 경우")
            void case1() {
                final List<Terms> defaultTerms = termsService.getDefaultTerms();

                assertThat(defaultTerms).isNotEmpty()
                        .hasSize(termsList.size())
                        .contains(terms);
            }

        }

    }

    @Nested
    @DisplayName("약관 상세 조회")
    class GetTermsDetail {

        @BeforeEach
        void setUp() {
            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.TERMS_OF_SERVICE))
                    .thenReturn(TERMS_OF_SERVICE);

            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.RECEIVE_MARKETING_INFO))
                    .thenReturn(RECEIVE_MARKETING_INFO);

            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.PRIVACY_COLLECTION))
                    .thenReturn(PRIVACY_COLLECTION);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("서비스 이용 약관인 경우")
            void case1() {
                final TermsDetailDto termsDetail = termsService.getTermsDetail(TermsType.TERMS_OF_SERVICE);
                assertThat(termsDetail).isNotNull();

                final TermsType type = termsDetail.getType();
                assertThat(type).isEqualTo(TermsType.TERMS_OF_SERVICE);
            }

            @Test
            @DisplayName("마케팅 정보 수신 동의인 경우")
            void case2() {
                final TermsDetailDto termsDetail = termsService.getTermsDetail(TermsType.RECEIVE_MARKETING_INFO);
                assertThat(termsDetail).isNotNull();

                final TermsType type = termsDetail.getType();
                assertThat(type).isEqualTo(TermsType.RECEIVE_MARKETING_INFO);
            }

            @Test
            @DisplayName("개인 정보 수집 이용 안내인 경우")
            void case3() {
                final TermsDetailDto termsDetail = termsService.getTermsDetail(TermsType.PRIVACY_COLLECTION);
                assertThat(termsDetail).isNotNull();

                final TermsType type = termsDetail.getType();
                assertThat(type).isEqualTo(TermsType.PRIVACY_COLLECTION);
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> termsService.getTermsDetail(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("약관 저장")
    class PostTerms {

        private PostTermsDto postTermsDto;

        @BeforeEach
        void setUp() throws JsonProcessingException {
            postTermsDto = objectMapper.readValue(MockModel.TERMS_OF_SERVICE, PostTermsDto.class);

            lenient().when(termsRepository.save(any(Terms.class)))
                    .thenReturn(TERMS_OF_SERVICE);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                lenient().when(termsRepository.findByTypeAndVersion(any(TermsType.class), anyString()))
                        .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("타입에 맞게 저장된 경우")
            void case1() {
                final TermsDetailDto termsDetailDto = termsService.postTerms(postTermsDto);
                assertThat(termsDetailDto).isNotNull();

                final TermsType type = termsDetailDto.getType();
                assertThat(type).isEqualTo(TermsType.TERMS_OF_SERVICE);
            }

            @RepeatedTest(3)
            @DisplayName("반복 테스트")
            void case2() {
                case1();
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> termsService.postTerms(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("이미 약관이 존재하는 경우")
            void case2() {
                lenient().when(termsRepository.findByTypeAndVersion(any(TermsType.class), anyString()))
                        .thenReturn(Optional.of(TERMS_OF_SERVICE));

                assertThatThrownBy(() -> termsService.postTerms(postTermsDto))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("타입에 따른 약관 조회")
    class GetTermsByType {

        @BeforeEach
        void setUp() {
            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.TERMS_OF_SERVICE))
                    .thenReturn(TERMS_OF_SERVICE);

            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.RECEIVE_MARKETING_INFO))
                    .thenReturn(RECEIVE_MARKETING_INFO);

            lenient().when(termsRepositoryImpl.findUseTermsByType(TermsType.PRIVACY_COLLECTION))
                    .thenReturn(PRIVACY_COLLECTION);
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("서비스 이용 약관인 경우")
            void case1() {
                final Terms terms = termsService.getTermsByType(TermsType.TERMS_OF_SERVICE);
                assertThat(terms).isNotNull();

                final TermsType type = terms.getType();
                assertThat(type).isEqualTo(TermsType.TERMS_OF_SERVICE);
            }

            @Test
            @DisplayName("마케팅 정보 수신 동의인 경우")
            void case2() {
                final Terms terms = termsService.getTermsByType(TermsType.RECEIVE_MARKETING_INFO);
                assertThat(terms).isNotNull();

                final TermsType type = terms.getType();
                assertThat(type).isEqualTo(TermsType.RECEIVE_MARKETING_INFO);
            }

            @Test
            @DisplayName("개인 정보 수집 이용 안내인 경우")
            void case3() {
                final Terms terms = termsService.getTermsByType(TermsType.PRIVACY_COLLECTION);
                assertThat(terms).isNotNull();

                final TermsType type = terms.getType();
                assertThat(type).isEqualTo(TermsType.PRIVACY_COLLECTION);
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> termsService.getTermsByType(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

}