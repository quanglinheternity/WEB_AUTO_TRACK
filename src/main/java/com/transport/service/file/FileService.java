package com.transport.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.transport.dto.file.FileResponse;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileService {

    FileStorageService fileStorageService;

    public FileResponse downloadFile(String path) {
        return buildFileResponse(path, true);
    }

    public FileResponse viewFile(String path) {
        return buildFileResponse(path, false);
    }

    private FileResponse buildFileResponse(String path, boolean isDownload) {
        try {
            Path filePath = fileStorageService.getFilePath(path);

            if (!Files.exists(filePath)) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new AppException(ErrorCode.FILE_NOT_READABLE);
            }

            String contentType;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return FileResponse.builder()
                    .fileName(resource.getFilename())
                    .contentType(contentType)
                    .url("/files/view?path=" + path)
                    .downloadUrl("/files/download?path=" + path)
                    .build();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing file: {}", path, e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
