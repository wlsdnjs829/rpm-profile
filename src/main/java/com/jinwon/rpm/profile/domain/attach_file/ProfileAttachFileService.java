package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.ProfileAttachFileDto;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.infra.component.S3Component;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.core.io.Resource;
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
public class ProfileAttachFileService {

    private final ProfileAttachFileRepository profileAttachFileRepository;

    private final S3Component s3Component;

    /**
     * 파일 첨부
     *
     * @param multipartFile 첨부 파일
     * @return 첨부된 파일
     */
    public ProfileAttachFile uploadFile(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, ErrorMessage.INVALID_PARAM.name());

        final ProfileAttachFileDto attachFileDto = s3Component.uploadFile(multipartFile);

        try {
            final ProfileAttachFile attachFile = attachFileDto.toEntity();
            return profileAttachFileRepository.save(attachFile);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            s3Component.deleteFile(attachFileDto.filePath(), attachFileDto.fileUid());
            throw new CustomException(ErrorMessage.FAIL_S3_UPLOAD);
        }
    }

    /**
     * 파일 삭제
     *
     * @param fileUid 파일 UID
     */
    public void deleteProfileFile(String fileUid) {
        Assert.notNull(fileUid, ErrorMessage.INVALID_PARAM.name());

        final ProfileAttachFile profileAttachFile = profileAttachFileRepository.findByFileUid(fileUid)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));

        s3Component.deleteFile(profileAttachFile.getFilePath(), profileAttachFile.getFileUid());
        profileAttachFileRepository.delete(profileAttachFile);
    }

    /**
     * 파일 삭제
     *
     * @param profile 프로필
     */
    public void deleteProfileFile(Profile profile) {
        Assert.notNull(profile, ErrorMessage.INVALID_PARAM.name());

        profileAttachFileRepository.findByProfileAndType(profile, AttachFileType.PROFILE)
                .forEach(deleteAttachFile -> {
                    s3Component.deleteFile(deleteAttachFile.getFilePath(), deleteAttachFile.getFileUid());
                    profileAttachFileRepository.delete(deleteAttachFile);
                });
    }

    /**
     * 첨부 파일 다운로드
     *
     * @param fileUid 파일 UID
     * @return 리소스
     */
    public Resource downloadFile(String fileUid) {
        Assert.notNull(fileUid, ErrorMessage.INVALID_PARAM.name());

        final ProfileAttachFile attachFile = profileAttachFileRepository.findByFileUid(fileUid)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));

        return s3Component.downloadFile(attachFile.getFilePath(), attachFile.getFileUid());
    }

    /**
     * 서명된 S3 다운로드 Url 제공
     *
     * @param fileUid 파일 UID
     */
    public String getPreSignedUrl(Profile profile) {
        Assert.notNull(profile, ErrorMessage.INVALID_PARAM.name());

        final ProfileAttachFile attachFile =
                profileAttachFileRepository.findByProfileAndType(profile, AttachFileType.PROFILE)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));

        return s3Component.getPreSignedUrl(attachFile.getFilePath(), attachFile.getFileUid());
    }

}

