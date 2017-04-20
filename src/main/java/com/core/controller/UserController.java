package com.core.controller;

import com.core.domain.User;
import com.core.security.SecuritySupport;
import com.core.security.annotation.RightCheck;
import com.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by sun
 */
@Controller
@RequestMapping("/admin")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userInfoShow")
    public String userInfoAH(ModelMap modelMap) throws Exception {

        SecuritySupport securitySupport = supportFactory.getSecuritySupport();
        User user = securitySupport.getUserInfo();
        modelMap.put("user", user);
        return getView("userInfo");
    }

    @ResponseBody
    @RequestMapping(value = "/userUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String userUpdateAH(User user) throws Exception {
        return userService.updateInfo(user).toString();
    }

    @RequestMapping(value = "/userAddShow", method = RequestMethod.GET)
    public String userAddShowAH() throws Exception {
        request.setAttribute("addStatus",1);
        return getView("userInfo");
    }

    @RequestMapping(value = "/userChangePassWordShow", method = RequestMethod.GET)
    public String userChangePassWordShowAH() throws Exception {
        return getView("userChangePassWord");
    }

    @ResponseBody
    @RequestMapping(value = "/userChangePassWord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String userChangePassWordAH(User user) throws Exception {
        return userService.changePassWord(user).toString();
    }
}
