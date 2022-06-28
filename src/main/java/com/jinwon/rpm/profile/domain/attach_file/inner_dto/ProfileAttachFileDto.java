package com.jinwon.rpm.profile.domain.attach_file.inner_dto;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.attach_file.ProfileAttachFile;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Schema(description = "프로필 첨부 파일 DTO")
public class ProfileAttachFileDto {

    @NotNull
    @Schema(description = "파일 고유 키")
    private String fileUid;

    @NotNull
    @Schema(description = "파일 경로")
    private String filePath;

    @NotNull
    @Schema(description = "파일 이름")
    private String fileName;

    @NotNull
    @Schema(description = "파일 사이즈")
    private Long fileSize;

    @NotNull
    @Schema(description = "파일 타입")
    private final AttachFileType type = AttachFileType.PROFILE;

    public ProfileAttachFileDto(String fileUid, String filePath, String fileName, Long fileSize) {
        this.fileUid = fileUid;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public ProfileAttachFile toEntity() {
        return ModelMapperUtil.map(this, ProfileAttachFile.class);
    }

    public static ProfileAttachFileDto of(ProfileAttachFile attachFile) {
        return ModelMapperUtil.map(attachFile, ProfileAttachFileDto.class);
    }

}
