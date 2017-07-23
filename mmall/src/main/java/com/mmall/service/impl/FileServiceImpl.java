package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by yeleichao on 2017-7-23.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        //获取上传文件的名字
        String fileName = file.getOriginalFilename();
        //获取上传文件的扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //防止不同用户上传的文件名相同，所以在上传时后台进行更改其名字
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFileName);

        //判断文件夹是否存在,如果不存在则创建文件夹
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);//文件已经上传成功

            //将文件上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完后，删除upload上的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常", e);
            return null;
        }

        return targetFile.getName();
    }


    public static void main(String[] args){
        String fileName = "aaa.jpg";
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(fileExtensionName);
    }
}
