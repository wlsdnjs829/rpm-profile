package com.jinwon.rpm.profile.domain.attach_file.inner_dto;

import com.jinwon.rpm.profile.constants.enums.AttachFileType;
import com.jinwon.rpm.profile.domain.attach_file.MemberAttachFile;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 첨부 파일 DTO")
public class MemberAttachFileDto {

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
    private final AttachFileType type = AttachFileType.MEMBER;

    public MemberAttachFileDto(String fileUid, String filePath, String fileName, Long fileSize) {
        this.fileUid = fileUid;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public MemberAttachFile toEntity() {
        return ModelMapperUtil.map(this, MemberAttachFile.class);
    }

    public static MemberAttachFileDto of(MemberAttachFile attachFile) {
        return ModelMapperUtil.map(attachFile, MemberAttachFileDto.class);
    }

}
