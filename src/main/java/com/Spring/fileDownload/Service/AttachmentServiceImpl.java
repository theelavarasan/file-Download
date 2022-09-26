package com.Spring.fileDownload.Service;

import com.Spring.fileDownload.entity.Attachment;
import com.Spring.fileDownload.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentServiceImpl implements AttachmentService{
    @Autowired
    private AttachmentRepository repository;
    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new Exception("FileName contains invalid path sequence" + fileName);
            }
            Attachment attachment
                    =new Attachment(fileName,
                    file.getContentType(),
                    file.getBytes());
            return repository.save(attachment);
        }catch (Exception e){
            throw new Exception("Could not save File: "+fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId)throws Exception{
        return repository
                .findById(fileId)
                .orElseThrow(
                        ()->new Exception("File not found with Id: "+ fileId)
                );
    }
}
