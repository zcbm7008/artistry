package com.artistry.artistry.Repository;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Repository
public class S3Repository {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3Repository(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public void readFile() throws IOException {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                request -> request.bucket(bucketName).key("file-test.txt"));

        String fileContent = StreamUtils.copyToString(response, StandardCharsets.UTF_8);

        System.out.println(fileContent);
    }

    public CompleteMultipartUploadResponse upload(RequestBody data, String filePath){
        ObjectCannedACL acl = ObjectCannedACL.PUBLIC_READ; // 읽기 공개 옵션
        CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName).key(filePath)
                .acl(acl)
                .build();

        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(createMultipartUploadRequest);

        UploadPartRequest uploadPartRequest1 = UploadPartRequest.builder().bucket(bucketName).key( filePath )
                .uploadId( response.uploadId() )
                .partNumber(1).build();

        String etag1 = s3Client.uploadPart(uploadPartRequest1, data).eTag();

        CompletedPart part1 = CompletedPart.builder().partNumber(1).eTag(etag1).build();

        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder().parts(part1).build();
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                CompleteMultipartUploadRequest.builder().bucket(bucketName).key( filePath ).uploadId( response.uploadId() )
                        .multipartUpload(completedMultipartUpload)
                        .build();

        return s3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }

}
