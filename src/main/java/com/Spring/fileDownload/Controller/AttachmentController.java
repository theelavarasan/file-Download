package com.Spring.fileDownload.Controller;

import com.Spring.fileDownload.ResponseData;
import com.Spring.fileDownload.Service.AttachmentService;
import com.Spring.fileDownload.entity.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AttachmentController {
    @Autowired
    private AttachmentService service;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file)throws Exception{
        Attachment attachment=null;
        String downloadURL="";
        attachment = service.saveAttachment(file);
        downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();
        return new ResponseData(attachment.getFileName(),
                downloadURL,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileId)throws Exception{
        Attachment attachment = null;
        attachment = service.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                +"\"")
                .body(new ByteArrayResource(attachment.getData()));

    }
}
