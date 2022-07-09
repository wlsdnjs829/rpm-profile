package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.MemberAttachFileDto;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.component.S3Component;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 첨부파일 서비스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAttachFileService {

    private final MemberAttachFileRepository memberAttachFileRepository;

    private final S3Component s3Component;

    /**
     * 파일 첨부
     *
     * @param multipartFile 첨부 파일
     * @return 첨부된 파일
     */
    public MemberAttachFile uploadMemberFile(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, ErrorMessage.INVALID_PARAM.name());

        final MemberAttachFileDto attachFileDto = s3Component.uploadFile(multipartFile, AttachFileType.MEMBER);

        try {
            final MemberAttachFile attachFile = attachFileDto.toEntity();
            return memberAttachFileRepository.save(attachFile);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            s3Component.deleteFile(attachFileDto.getFilePath(), attachFileDto.getFileUid());
            throw new CustomException(ErrorMessage.FAIL_S3_UPLOAD);
        }
    }

    /**
     * 파일 삭제
     *
     * @param member 사용자
     */
    public void deleteMemberFile(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        memberAttachFileRepository.findByMemberAndType(member, AttachFileType.MEMBER)
                .forEach(deleteAttachFile -> {
                    s3Component.deleteFile(deleteAttachFile.getFilePath(), deleteAttachFile.getFileUid());
                    memberAttachFileRepository.delete(deleteAttachFile);
                });
    }

    /**
     * 서명된 S3 다운로드 Url 제공
     *
     * @param fileUid 파일 UID
     */
    public String getPreSignedUrl(Member member) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());

        final MemberAttachFile attachFile =
                memberAttachFileRepository.findByMemberAndType(member, AttachFileType.MEMBER)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));

        return s3Component.getPreSignedUrl(attachFile.getFilePath(), attachFile.getFileUid());
    }

}

