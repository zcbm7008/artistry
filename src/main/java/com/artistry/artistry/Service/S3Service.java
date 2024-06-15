package com.artistry.artistry.Service;

import com.artistry.artistry.Repository.S3Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private final S3Repository s3Repository;

    public S3Service(final S3Repository s3Repository){
        this.s3Repository = s3Repository;
    }

    public void saveImage(final MultipartFile file, final String fileName){
        s3Repository.upload(fileName,file);
    }


}
