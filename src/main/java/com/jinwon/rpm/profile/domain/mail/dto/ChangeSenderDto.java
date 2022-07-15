package com.jinwon.rpm.profile.domain.mail.dto;

import com.jinwon.rpm.profile.domain.mail.inner_dto.SenderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메일 변경 발송 객체")
public class ChangeSenderDto extends SenderDto {

}
