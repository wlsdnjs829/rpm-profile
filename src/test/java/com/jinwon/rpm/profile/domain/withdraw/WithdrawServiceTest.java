package com.jinwon.rpm.profile.domain.withdraw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.member.dto.DeleteMemberDto;
import com.jinwon.rpm.profile.domain.member.dto.PostMemberDto;
import com.jinwon.rpm.profile.domain.withdraw.inner_dto.PostWithdrawReasonDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * 회원 탈퇴 서비스 테스트
 */
@ExtendWith(MockitoExtension.class)
class WithdrawServiceTest {

    @InjectMocks
    private WithdrawService withdrawService;

    @Mock
    private WithdrawRepository withdrawRepository;

    private static final ObjectMapper objectMapper =
            Jackson2ObjectMapperBuilder.json()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .modules(new JavaTimeModule())
                    .build();

    private static final String EMAIL = "ljw0829@midasin.com";
    private static final String NAME = "이진원";

    private DeleteMemberDto deleteMemberDto;
    private Withdraw withdraw;
    private Member member;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        deleteMemberDto = objectMapper.readValue(MockModel.DELETE_MEMBER_DTO, DeleteMemberDto.class);

        final PostWithdrawReasonDto postWithdrawReasonDto =
                new PostWithdrawReasonDto(EMAIL, NAME, deleteMemberDto.getReason());

        withdraw = postWithdrawReasonDto.toEntity();

        final PostMemberDto postMemberDto = objectMapper.readValue(MockModel.MEMBER, PostMemberDto.class);
        postMemberDto.userEssentialInfo(EMAIL);
        member = postMemberDto.toEntity();
    }

    @Nested
    @DisplayName("사용자 탈퇴 사유 등록")
    class PostWithdrawReason {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                given(withdrawRepository.save(any(Withdraw.class)))
                        .willReturn(withdraw);
            }

            @Test
            @DisplayName("사용자 탈퇴 사유 정상 등록된 경우")
            void case1() {
                final PostWithdrawReasonDto withdrawDto = withdrawService.postWithdrawReason(deleteMemberDto, member);

                then(withdrawRepository)
                        .should(times(1))
                        .save(any(Withdraw.class));

                assertThat(withdrawDto).isNotNull();

                assertThat(withdrawDto.email())
                        .isNotNull()
                        .isEqualTo(EMAIL);

                assertThat(withdrawDto.name())
                        .isNotNull()
                        .isEqualTo(NAME);

                assertThat(withdrawDto.reason())
                        .isNotNull()
                        .isEqualTo(deleteMemberDto.getReason());
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("파라미터 사용자 삭제 객체가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> withdrawService.postWithdrawReason(null, member))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.INVALID_PARAM.name());

                then(withdrawRepository)
                        .should(times(0))
                        .save(any(Withdraw.class));
            }

            @Test
            @DisplayName("파라미터 사용자가 없는 경우")
            void case2() {
                assertThatThrownBy(() -> withdrawService.postWithdrawReason(deleteMemberDto, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(ErrorMessage.NOT_EXIST_MEMBER.name());

                then(withdrawRepository)
                        .should(times(0))
                        .save(any(Withdraw.class));
            }

        }

    }
}