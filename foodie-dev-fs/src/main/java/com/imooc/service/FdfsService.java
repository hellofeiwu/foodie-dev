package com.imooc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FdfsService {
    public String upload(MultipartFile file, String fileExtName) throws IOException;
}
