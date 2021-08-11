package com.imooc.controller;

import com.imooc.resource.FileUpload;
import com.imooc.service.FdfsService;
import com.imooc.utils.IMOOCJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("fdfs")
public class CenterUserController {
    @Autowired
    private FdfsService fdfsService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("upload")
    public IMOOCJSONResult upload(
            MultipartFile file) throws IOException {
        String path = "";
        if (file == null) {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }

        // 获得文件上传的文件名称
        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
            // 获取文件后缀名
            String fileNameArr[] = fileName.split("\\.");
            String suffix = fileNameArr[fileNameArr.length - 1];
            // 开始文件上传
            path = fdfsService.upload(file, suffix);
        }

        if (StringUtils.isBlank(path)) {
            return IMOOCJSONResult.errorMsg("文件上传失败");
        }

        String url = fileUpload.getHost() + path;
        return IMOOCJSONResult.ok(url);
    }
}
