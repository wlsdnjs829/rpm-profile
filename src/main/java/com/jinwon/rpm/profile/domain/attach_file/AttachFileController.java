package com.jinwon.rpm.profile.domain.attach_file;

import com.jinwon.rpm.profile.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Tag(name = "첨부파일 Controller")
@RequestMapping(value = "/attach-file")
public class AttachFileController implements BaseController {

    private final AttachFileService attachFileService;

    @DeleteMapping(value = "/{fileUid}")
    @Operation(summary = "첨부 파일 삭제")
    public Mono<Void> deleteFile(@PathVariable String fileUid) {
        return Mono.fromRunnable(() -> attachFileService.deleteFile(fileUid));
    }

    @GetMapping(value = "/{fileUid}")
    @Operation(summary = "첨부 파일 다운로드")
    public Mono<Resource> downloadFile(@PathVariable String fileUid) {
        return Mono.just(attachFileService.downloadFile(fileUid));
    }

}