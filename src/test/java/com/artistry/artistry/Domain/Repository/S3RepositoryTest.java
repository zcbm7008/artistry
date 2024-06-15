package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Repository.S3Repository;
import io.awspring.cloud.s3.S3Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class S3RepositoryTest {

    @Autowired
    private S3Repository s3Repository;

    String fileToDelete = "deleteTest.txt";

    List<String> fileNames = new ArrayList<>();

    String testPrefix = "testPrefix";
    @BeforeEach
    void setUp() {
        s3Repository.upload(fileToDelete,"this is testFileToDelete");

        fileNames.add(testPrefix + "-1");
        fileNames.add(testPrefix + "-2");

        fileNames.forEach(fileName -> s3Repository.upload(fileName, "fileName"));

    }

    @AfterEach
    void afterAll(){
        fileNames.forEach(fileName -> s3Repository.delete(fileName));
    }

    @DisplayName("S3에 파일을 업로드한다.")
    @Test
    void uploadFile() throws IOException {
        String fileName = "test.png";
        String filePath = "classpath:static/" + fileName;
        File imageFile = new File(filePath);

        if (imageFile.exists()) {
            s3Repository.upload("fileName",imageFile);
        }

        S3Resource s3Resource = s3Repository.download(fileName);
        assertThat(s3Resource.getFilename()).isEqualTo(fileName);

        s3Repository.delete(fileName);

    }

    @DisplayName("prefix가 붙은 S3의 모든 오브젝트를 가져온다.")
    @Test
    void findObjectsByPrefix(){
        List <S3Resource> foundObjects = s3Repository.listObjects(testPrefix);
        assertThat(foundObjects).hasSize(2).extracting(S3Resource::getFilename).allMatch(fileName -> fileName.startsWith(testPrefix));
    }

    @DisplayName("S3의 파일을 삭제한다.")
    @Test
    void fileDelete() {
        assertThat(s3Repository.objectExists(fileToDelete)).isTrue();
        s3Repository.delete(fileToDelete);
        assertThat(s3Repository.objectExists(fileToDelete)).isFalse();
    }
}
