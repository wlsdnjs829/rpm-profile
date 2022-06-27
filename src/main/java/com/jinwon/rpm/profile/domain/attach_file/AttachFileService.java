package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.AttachFileDto;
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
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;

    private final S3Component s3Component;

    public String uploadFile(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, ErrorMessage.INVALID_PARAM.name());

        final AttachFileDto attachFileDto = s3Component.uploadFile(multipartFile);

        try {
            final AttachFile attachFile = attachFileDto.toEntity();
            attachFileRepository.save(attachFile);
            return attachFile.getFileUid();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            s3Component.deleteFile(attachFileDto.filePath(), attachFileDto.fileUid());
            throw new CustomException(ErrorMessage.FAIL_S3_UPLOAD);
        }
    }

    public void deleteFile(String fileUid) {
        Assert.notNull(fileUid, ErrorMessage.INVALID_PARAM.name());

        final AttachFile attachFile = attachFileRepository.findByFileUid(fileUid)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_ATTACH_FILE));

        s3Component.deleteFile(attachFile.getFilePath(), attachFile.getFileUid());
        attachFileRepository.delete(attachFile);
    }

}

