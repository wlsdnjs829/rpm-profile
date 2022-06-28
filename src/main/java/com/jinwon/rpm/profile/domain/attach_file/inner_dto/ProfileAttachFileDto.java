package com.jinwon.rpm.profile.domain.attach_file.inner_dto;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.attach_file.AttachFile;
import com.jinwon.rpm.profile.domain.attach_file.ProfileAttachFile;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "첨부 파일 DTO")
public record ProfileAttachFileDto(
        @NotNull String fileUid,
        @NotNull String filePath,
        @NotNull String fileName,
        @NotNull Long fileSize,
        @NotNull AttachFileType attachFileType
        ) {

    public ProfileAttachFileDto(String fileUid, String filePath, String fileName, Long fileSize) {
        this(fileUid, filePath, fileName, fileSize, AttachFileType.PROFILE);
    }

    public ProfileAttachFile toEntity() {
        return ModelMapperUtil.map(this, ProfileAttachFile.class);
    }

    public static ProfileAttachFileDto of(AttachFile attachFile) {
        return ModelMapperUtil.map(attachFile, ProfileAttachFileDto.class);
    }

}
