package com.artistry.artistry.Repository;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@Repository
public class S3Repository {

    private final S3Client s3Client;
    private final S3Template s3Template;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3Repository(S3Client s3Client, S3Template s3Template) {
        this.s3Client = s3Client;
        this.s3Template = s3Template;
    }

    public void upload(String fileName, Object object){
        s3Template.store(bucketName,fileName,object);
    }

    public S3Resource download(String key){
        return s3Template.download(bucketName, key);
    }

    public void delete(final String key){
        s3Template.deleteObject(bucketName, key);
    }

    public boolean objectExists(String key){
        return s3Template.objectExists(bucketName,key);
    }

    public List<S3Resource> listObjects(String prefix){
        return s3Template.listObjects(bucketName,prefix);
    }

}
