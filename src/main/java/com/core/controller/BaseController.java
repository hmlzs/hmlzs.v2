package com.core.controller;

import com.core.config.Config;
import com.core.repository.BaseRepository;
import com.core.security.SupportFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sun
 */
public class BaseController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected BaseRepository baseRepository;

    @Autowired
    protected SupportFactory supportFactory;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Config config;

    protected Log logger = LogFactory.getLog(this.getClass());

    /**
     * 打开view路径
     *
     * @param viewName
     * @return
     */
    public String getView(String viewName) {
        return "admin/" + viewName;
    }

    /**
     * 重定向
     *
     * @param viewName
     * @return
     */
    public String getViewRedirect(String viewName) {
        return "redirect:" + viewName;
    }
}
