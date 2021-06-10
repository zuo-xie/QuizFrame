package com.quizFrame.core.util.file;

import java.io.File;

/**
 * 文件统一接口
 */
public interface ossService {

    /**
     * 上传文件
     */
    Boolean upload(File file, String fileName);

    /**
     * 获取文件地址
     */
    String  getFileUrl(String fileName);
}
