package com.scott.mmall.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by scott on 2017/6/8.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
