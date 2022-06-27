package com.jinwon.rpm.profile.domain.attach_file.inner_dto;

import com.jinwon.rpm.profile.domain.attach_file.AttachFile;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "첨부 파일 DTO")
public record AttachFileDto(
        @NotNull String fileUid,
        @NotNull String filePath,
        @NotNull String fileName,
        @NotNull Long fileSize
) {

    public AttachFile toEntity() {
        return ModelMapperUtil.map(this, AttachFile.class);
    }

}
