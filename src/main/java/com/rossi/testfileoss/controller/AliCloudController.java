package com.rossi.testfileoss.controller;

import com.rossi.testfileoss.service.AliCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AliCloudController {

    @Autowired
    private AliCloudService aliCloudService;


    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFile multipartFile){
        return ResponseEntity.ok(aliCloudService.upload(multipartFile));
    }

    @PostMapping("/download")
    public ResponseEntity<String> download(String fileName){
        return ResponseEntity.ok(aliCloudService.download(fileName));
    }

}
