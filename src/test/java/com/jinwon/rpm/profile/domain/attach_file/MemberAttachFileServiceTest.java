package com.jinwon.rpm.profile.domain.attach_file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.MemberAttachFileDto;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.member.MemberRepository;
import com.jinwon.rpm.profile.domain.member.dto.PostMemberDto;
import com.jinwon.rpm.profile.infra.component.S3Component;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.mock_model.MockModel;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * 사용자 첨부 파일 서비스 테스트
 */
@ExtendWith(MockitoExtension.class)
class MemberAttachFileServiceTest {

    @Mock
    private MemberAttachFileRepository memberAttachFileRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private S3Component s3Component;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FILE_NAME = "test.txt";
    private static final String NAME = "file";

    private MemberAttachFileService memberAttachFileService;
    private MockMultipartFile mockMultipartFile;
    private MemberAttachFileDto memberAttachFileDto;
    private Member member;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        when(memberRepository.save(any(Member.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        final PostMemberDto postMemberDto = objectMapper.readValue(MockModel.MEMBER, PostMemberDto.class);
        member = memberRepository.save(postMemberDto.toEntity());

        memberAttachFileDto = new MemberAttachFileDto(NAME, FILE_NAME, FILE_NAME, 0L);

        memberAttachFileService = new MemberAttachFileService(memberAttachFileRepository, s3Component);
        mockMultipartFile = new MockMultipartFile(
                NAME, FILE_NAME, ContentType.TEXT_PLAIN.getMimeType(), NAME.getBytes());
    }

    @Nested
    @DisplayName("파일 업로드 테스트")
    class UploadMemberFile {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                when(s3Component.uploadFile(any(MultipartFile.class), any(AttachFileType.class)))
                        .thenReturn(memberAttachFileDto);

                when(memberAttachFileRepository.save(any(MemberAttachFile.class)))
                        .thenAnswer(invocation -> invocation.getArguments()[0]);
            }

            @Test
            @DisplayName("새로운 파일 업로드")
            void case1() {
                final MemberAttachFile memberAttachFile = memberAttachFileService.uploadMemberFile(mockMultipartFile);
                memberAttachFile.linkMember(member);
                assertThat(memberAttachFile.getFileName()).isEqualTo(FILE_NAME);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @BeforeEach
            void setUp() {
                lenient().doNothing()
                        .when(s3Component)
                        .deleteFile(anyString(), anyString());

                final MemberAttachFileDto memberAttachFileDto =
                        new MemberAttachFileDto(NAME, FILE_NAME, FILE_NAME, 0L);

                lenient().when(s3Component.uploadFile(any(MultipartFile.class), any(AttachFileType.class)))
                        .thenReturn(memberAttachFileDto);
            }

            @Test
            @DisplayName("파일이 없는 경우")
            void case1() {
                assertThatThrownBy(() -> memberAttachFileService.uploadMemberFile(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("저장에 실패한 경우")
            void case2() {
                when(memberAttachFileRepository.save(any(MemberAttachFile.class)))
                        .thenThrow(IllegalArgumentException.class);

                assertThatThrownBy(() -> memberAttachFileService.uploadMemberFile(mockMultipartFile))
                        .isInstanceOf(CustomException.class);
            }

        }

    }

    @Nested
    @DisplayName("파일 삭제 테스트")
    class DeleteMemberFile {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            @DisplayName("파일 삭제")
            void case1() {
                memberAttachFileService.deleteMemberFile(member);
                assertThat(memberAttachFileRepository.findByMemberAndType(member, AttachFileType.MEMBER)).isEmpty();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("사용자가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> memberAttachFileService.deleteMemberFile(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("서명된 파일 주소 조회 테스트")
    class GetPreSignedUrl {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @BeforeEach
            void setUp() {
                when(memberAttachFileRepository.findByMemberAndType(any(Member.class), any(AttachFileType.class)))
                        .thenReturn(List.of(memberAttachFileDto.toEntity()));

                when(s3Component.getPreSignedUrl(anyString(), anyString()))
                        .thenReturn(FILE_NAME);
            }

            @Test
            @DisplayName("서명된 파일 주소 조회 성공")
            void case1() {
                assertThat(memberAttachFileService.getPreSignedUrl(member)).isEqualTo(FILE_NAME);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("사용자가 없는 경우")
            void case1() {
                assertThatThrownBy(() -> memberAttachFileService.getPreSignedUrl(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

        }

    }

}