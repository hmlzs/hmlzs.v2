package com.core.config;


public class Config {
    //private String platform;
    private String database;

    private String dbName;

    private String domain;
    private String listDomain;
    private String listParam;
    private String staticResourceURLPrefix;

    private long dbIdBuffSize;

    private long articleIdAddend;

    private String templateDir;
    private String previewDir;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getListDomain() {
        return listDomain;
    }

    public void setListDomain(String listDomain) {
        this.listDomain = listDomain;
    }

    public String getListParam() {
        return listParam;
    }

    public void setListParam(String listParam) {
        this.listParam = listParam;
    }

    public String getStaticResourceURLPrefix() {
        return staticResourceURLPrefix;
    }

    public void setStaticResourceURLPrefix(String staticResourceURLPrefix) {
        this.staticResourceURLPrefix = staticResourceURLPrefix;
    }

    public long getDbIdBuffSize() {
        return dbIdBuffSize;
    }

    public void setDbIdBuffSize(long dbIdBuffSize) {
        this.dbIdBuffSize = dbIdBuffSize;
    }

    public long getArticleIdAddend() {
        return articleIdAddend;
    }

    public void setArticleIdAddend(long articleIdAddend) {
        this.articleIdAddend = articleIdAddend;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public String getPreviewDir() {
        return previewDir;
    }

    public void setPreviewDir(String previewDir) {
        this.previewDir = previewDir;
    }
}
