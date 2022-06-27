package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Tag(name = "첨부파일 Controller")
@RequestMapping(value = "/attach-file")
public class AttachFileController implements BaseController {

    private final AttachFileService attachFileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "첨부 파일 업로드")
    public Mono<String> uploadFile(@RequestPart MultipartFile multipartFile) {
        final String fileUid = attachFileService.uploadFile(multipartFile);
        return Mono.just(fileUid);
    }

    @DeleteMapping(value = "/{fileUid}")
    @Operation(summary = "첨부 파일 삭제")
    public Mono<Void> deleteFile(@PathVariable String fileUid) {
        return Mono.fromRunnable(() -> attachFileService.deleteFile(fileUid));
    }

}