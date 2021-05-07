package com.rossi.testfileoss.service;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class AliCloudService {

    @Value("${aliyun.endpoint}")
    private String aliyunEndpoint;

    @Value("${aliyun.accessKeyid}")
    private String accessKeyid;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Value("${aliyun.folderName}")
    private String folderName;


    public String upload (MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();
        String fileName = multipartFile.getOriginalFilename();
        Long size = multipartFile.getSize();
        String directory = Optional.ofNullable(folderName).orElseGet(() -> "");

        log.info("Upload File Information :");
        log.info("File Name         : {}", fileName);
        log.info("Content Type      : {}", contentType);
        log.info("Size              : {}", size);

        try {
            ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();

            InputStream stream = new ByteArrayInputStream(multipartFile.getBytes());
            ObjectMetadata meta = new ObjectMetadata();

            OSS ossClient = new OSSClientBuilder().build(aliyunEndpoint, accessKeyid, accessKeySecret, configuration);
            ossClient.putObject(new PutObjectRequest(bucketName, directory.concat(fileName), stream, meta));
            log.debug("Success Upload file [" + fileName + "] to OSS.");
            ossClient.shutdown();
            return "SUCCESS";
        }
        catch (Exception e){
            log.warn("Error upload file: ", e);
            return "ERROR";
        }
    }

    public String download(String fileName){
        String directory = Optional.ofNullable(folderName).orElseGet(() -> "");
        String fileDir = directory.concat(fileName);
        try {
            ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
            OSS ossClient = new OSSClientBuilder().build(aliyunEndpoint, accessKeyid, accessKeySecret, configuration);
            OSSObject object = ossClient.getObject(bucketName, fileDir);
            Date expiration = new Date(new Date().getTime() + 60 * 1000);
            URL url = ossClient.generatePresignedUrl(bucketName, fileDir, expiration);
            log.info(String.valueOf(url));
            log.info("OSS Downloaded File Information :");
            log.info("Filename (Key) : {}", object.getKey());
            log.info("Bucket         : {}", object.getBucketName());
            log.info("Size           : {}", object.getObjectMetadata().getContentLength());
            log.info("Date           : {}", object.getObjectMetadata().getLastModified());
            log.info("URL            : {}", url);
            log.info("Expiry Link    : {}", expiration);

            ossClient.shutdown();
            return "SUCCESS: ".concat(String.valueOf(url));
        }
        catch (Exception e) {
            log.warn("Error download file: ", e);
            return "ERROR";
        }
    }

}
