package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yeleichao on 2017-7-23.
 */
public interface IFileService {
     String upload(MultipartFile file, String path);
}
