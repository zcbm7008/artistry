package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Repository.S3Repository;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class S3RespostoryTest {

    @Autowired
    private S3Repository s3Repository;

    @Autowired
    ResourceLoader resourceLoader;

    @DisplayName("S3에 파일을 업로드한다.")
    @Test
    void uploadFile() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:static/test.png"); // test.jpg 이미지 가져옴

        if (resource.exists()) {
            RequestBody requestBody = RequestBody.fromFile(resource.getFile());
            CompleteMultipartUploadResponse completeMultipartUploadResponse= s3Repository.upload(requestBody, resource.getFilename());
            assertThat(completeMultipartUploadResponse.location()).isNotNull();
        }

    }
    @Test
    void readFile() throws IOException {

        s3Repository.readFile();
    }
//    @DisplayName("S3의 파일을 삭제한다.")
//    @Test
//    void fileDelete() {
//
//        Resource resource = resourceLoader.getResource("classpath:static/test.jpg"); // test.jpg 이미지 가져옴
//
//        if ( resource.exists() ) {
//
//            AmazonS3Service amazonS3Service = new AmazonS3Service();
//            amazonS3Service.delete( resource.getFilename() , "bonsookoo");
//        }
//
//    }
}
