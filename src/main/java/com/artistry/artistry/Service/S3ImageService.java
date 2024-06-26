package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.member.ProfileImage;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.S3Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.s3.S3Resource;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class S3ImageService {
    private final S3Repository s3Repository;
    private final ObjectMapper objectMapper;


    public S3ImageService(final S3Repository s3Repository,ObjectMapper objectMapper,MemberRepository memberRepository){
        this.s3Repository = s3Repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public String saveThumbnailImage(final MultipartFile file, final Member member,
                                     int x, int y, int width, int height) throws IOException {

        String imageType = file.getContentType();
        String fileExtension = imageType.substring(imageType.indexOf("/") + 1);
        String filePath = getFilePath(member, fileExtension);

        // 원본 이미지 로드
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        // 크롭된 이미지 생성
        BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);

        BufferedImage thumbnail = resizeImage(croppedImage, ProfileImage.SIZE,ProfileImage.SIZE);
        String base64Image = encodeImageToBase64(thumbnail, imageType);

        s3Repository.upload(filePath, base64Image);

        // S3에서 이미지를 가져와 Base64로 인코딩
        return getImageBase64FromS3(filePath);
    }

    public static String getFilePath(Member member, String fileExtension) {
        return "member/" + member.getId().toString() + "/images/thumbnail." + fileExtension;
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) throws IOException {
        return Thumbnails.of(image)
                .imageType(BufferedImage.TYPE_INT_ARGB)
                .size(width, height)
                .asBufferedImage();
    }

    private static String encodeImageToBase64(BufferedImage thumbnail, String imageType) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, imageType.substring(imageType.indexOf("/")+1), baos);
        baos.flush();

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public String getImageBase64FromS3(String fileName) throws IOException {
        S3Resource s3Resource = s3Repository.download(fileName);
        byte[] imageBytes = s3Resource.getContentAsByteArray();

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

}
