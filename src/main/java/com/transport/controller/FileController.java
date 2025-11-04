package com.transport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.file.FileResponse;
import com.transport.service.file.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileController {
    FileService fileService;

    @GetMapping("/download")
    public ApiResponse<FileResponse> downloadFile(@RequestParam String path) {
        return ApiResponse.<FileResponse>builder()
                .message("Tải file thành công")
                .data(fileService.downloadFile(path))
                .build();
    }

    @GetMapping("/view")
    public ApiResponse<FileResponse> viewFile(@RequestParam String path) {
        return ApiResponse.<FileResponse>builder()
                .message("Xem file thành công")
                .data(fileService.viewFile(path))
                .build();
    }
}
