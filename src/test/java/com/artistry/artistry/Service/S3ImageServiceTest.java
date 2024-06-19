package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.S3Repository;
import io.awspring.cloud.s3.S3Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class S3ImageServiceTest {
    @Autowired
    S3ImageService s3ImageService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    S3Repository s3Repository;
    @Autowired
    private ResourceLoader resourceLoader;

    @DisplayName("업로드된 사진을 crop하고 썸네일로 변환하여 업로드한다.")
    @Test
    void uploadThumbnail() throws IOException {
        String fileName = "test.png";
        String fileExtension = fileName.substring(fileName.indexOf(".") + 1);

        Resource resource = resourceLoader.getResource("classpath:static/" + fileName);

        File file = resource.getFile();
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), fileExtension, Files.readAllBytes(file.toPath()));

        Member member = memberRepository.save(new Member("member1","a@a.com"));

        if (file.exists()) {
            String response = s3ImageService.saveThumbnailImage(multipartFile,member,0,0,110,110);

            assertThat(response).isNotNull();

            S3Resource s3Resource = s3Repository.download(S3ImageService.getFilePath(member,fileExtension));

            assertThat(s3Resource.getFilename().endsWith("thumbnail." + fileExtension)).isTrue();
            assertThat(s3Resource.exists()).isTrue();

            s3Repository.delete(s3Resource.getFilename());

        }
    }
}
