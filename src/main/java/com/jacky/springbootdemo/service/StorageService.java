package com.jacky.springbootdemo.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jacky
 * @time 2021-01-23 10:15
 * @discription  假设我们需要一个存储服务，在本地开发时，直接使用文件存储即可，但是，在测试和生产环境，需要存储到云端如S3上
 */
public interface StorageService {
    /**
     *  根据URI打开InputStream:
     * @param uri
     * @return
     * @throws IOException
     */
    InputStream openInputStream(String uri) throws IOException;

    /**
     * 根据扩展名+InputStream保存并返回URI:
     * @param extName
     * @param input
     * @return
     * @throws IOException
     */
    String store(String extName, InputStream input) throws IOException;
}
