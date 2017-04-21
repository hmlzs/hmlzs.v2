package com.core.controller;

import com.core.domain.User;
import com.core.security.SecuritySupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by sun
 */
@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController {

    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * request: http:// xx.yy.com/aa/admin/
     * 重定向到/
     *
     * @return
     */
    @RequestMapping(value = "/")
    public String simpleRequest() {
        return getViewRedirect("index");
    }

    /**
     * 重定向到首页
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String simpleRootRequest() {
        return getViewRedirect("admin/index");
    }

    /**
     * 首页
     * AH -> AUTH 授权
     *
     * @return
     */
    @RequestMapping("/index")
    public String indexAH() {
        return getView("index");
    }

    /**
     * 打开登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginShow() throws Exception {
        SecuritySupport securitySupport = supportFactory.getSecuritySupport();
        // 检查用户登录信息
        User user = securitySupport.getUserInfo();
        if (user != null && user.getId() > 0) {
            return getViewRedirect("index");
        }
        return getView("login");
    }

    /**
     *
     * 登录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String login(HttpServletResponse resp) {
        SecuritySupport support = supportFactory.getSecuritySupport();
        ObjectNode objectNode = support.login(resp);
        objectNode.put("success", true);
        return objectNode.toString();
    }

    /**
     * 退出
     * @param resp
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletResponse resp) {
        SecuritySupport securitySupport = supportFactory.getSecuritySupport();
        securitySupport.logout(resp);

        return getViewRedirect("login");
    }
}
