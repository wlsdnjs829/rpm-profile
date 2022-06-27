package com.jinwon.rpm.profile.infra.component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.domain.attach_file.inner_dto.AttachFileDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * S3 파일 관리 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private static final String DOT = ".";
    private static final String SLASH = "/";

    /**
     * 파일 업로드
     *
     * @param multipartFile 멀티 파일
     * @return 업로드 파일 이름
     */
    public AttachFileDto uploadFile(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, ErrorMessage.INVALID_PARAM.name());

        final long size = multipartFile.getSize();
        final String originalFilename = multipartFile.getOriginalFilename();
        final String fileName = createFileName(originalFilename);
        final String contentType = multipartFile.getContentType();

        final ObjectMetadata objectMetadata = getObjectMetadata(size, contentType);
        final String dirPath = uploadS3(multipartFile, fileName, objectMetadata);

        return new AttachFileDto(fileName, dirPath, originalFilename, size);
    }

    /* 오브젝트 메타 데이터 조회 */
    private ObjectMetadata getObjectMetadata(long size, String contentType) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);
        return objectMetadata;
    }

    /* S3 파일 업로드 */
    private String uploadS3(MultipartFile multipartFile, String fileName, ObjectMetadata objectMetadata) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            final String dirPath = SLASH + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            amazonS3.putObject(new PutObjectRequest(bucket + dirPath, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return dirPath;
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new CustomException(ErrorMessage.FAIL_S3_UPLOAD);
        }
    }

    /**
     * 파일 삭제
     *
     * @param fileName 파일 이름
     */
    public void deleteFile(String filePath, String fileName) {
        Assert.notNull(fileName, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(filePath, ErrorMessage.INVALID_PARAM.name());

        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket + filePath, fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    /* 랜덤한 파일 이름 생성 */
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    /* 파일 확장자 조회 */
    private String getFileExtension(String fileName) {
        try {
            final int lastIndexOf = fileName.lastIndexOf(DOT);
            return fileName.substring(lastIndexOf);
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException(ErrorMessage.INVALID_FILE_EXTENSION);
        }
    }

}