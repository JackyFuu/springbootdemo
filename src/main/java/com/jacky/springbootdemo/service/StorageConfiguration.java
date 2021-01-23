package com.jacky.springbootdemo.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author jacky
 * @time 2021-01-23 10:30
 * @discription  定义一个Java Bean，持有该组配置(定义在配置文件application.yml中)
 *               保证Java Bean的属性名称与配置一致即可。
 *
 *               @ConfigurationProperties("storage.local")表示将从配置项storage.local读取该项的所有子项配置
 *               @Configuration表示StorageConfiguration也是一个Spring管理的Bean，可直接注入到其他Bean中
 */

@Configuration
@ConfigurationProperties("storage.local")
public class StorageConfiguration {

    private String rootDir;
    private int maxSize;
    private boolean allowEmpty;
    private List<String> allowTypes;

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public List<String> getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(List<String> allowTypes) {
        this.allowTypes = allowTypes;
    }
}
