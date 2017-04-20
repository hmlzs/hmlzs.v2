package com.core.security;

import com.core.domain.Session;
import com.core.domain.User;
import com.core.repository.SecurityRepository;
import com.core.util.Constant;
import com.core.util.EncryptUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sun
 */
public class SecuritySupport {
    private Log logger = LogFactory.getLog(this.getClass());

    private final static String TOOKEN_COOKIE_NAME = "_sn_";

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    public SecuritySupport() {}

    public ObjectNode login(HttpServletResponse response) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            String account = request.getParameter("account");
            String password = request.getParameter("password");
            ObjectNode msg = objectMapper.createObjectNode();

            // 后台检查用户输入信息
            String result = "failed";
            boolean isCheck = true;

            if (StringUtils.isBlank(account)) {
                msg.put("account", "please input a account!");
                isCheck = false;
            }
            if (StringUtils.isBlank(password)) {
                msg.put("password", "please input your password!");
                isCheck = false;
            }
            // 通过
            if (isCheck) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("account", account);
                param.put("password", EncryptUtil.md5(password));
                param.put("status", Constant.OPEN);
                User user = findAccount(User.class, param);
                if (user != null) {
                    createSession(response, user.getAccount(), user.getId());
                    user.setLastLoginDate(new Date());
                    securityRepository.update(user);
                    result = "success";
                    // 此处记录日志
                }
            }
            objectNode.put("result", result);
            objectNode.put("success", true);
            return objectNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查用户
     * @param tClass
     * @param param
     * @param <T>
     * @return
     */
    private <T> T findAccount(Class<T> tClass, Map<String, Object> param) {
        StringBuffer sql = new StringBuffer();
        T t = null;
        try {
            t = tClass.newInstance();

            if (param != null && param.size() != 0) {
                int size = param.size();
                int count = 0;
                sql.append(" WHERE");
                for (String key : param.keySet()) {
                    count++;
                    sql.append(" ");
                    sql.append(key);
                    sql.append("= :");
                    sql.append(key);
                    if (count < size) {
                        sql.append(" AND");
                    }
                }
            }

            List<T> list = securityRepository.list(tClass, sql.toString(), param);
            if (list != null && list.size() != 0) {
                t = list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("");
        }
        return t;
    }

    /**
     * 保存Session
     * @param response
     * @param account
     * @param id
     */
    @Transactional
    public void createSession(HttpServletResponse response, String account, long id) {

        String sessionKey = EncryptUtil.md5(account + ":" + System.currentTimeMillis());
        try {

            Session session = new Session();
            session.setLoginDate(new Date());
            // session.setExpireDate();
            // TODO
            session.setSessionKey(sessionKey);
            session.setAccount(account);

            session.setStatus(Constant.OPEN);
            session.setUserId(id);

            securityRepository.create(session);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException("");
        }

        Cookie cookie = new Cookie(TOOKEN_COOKIE_NAME, sessionKey);
        // cookie.setSecure(true);
        cookie.setDomain(request.getServerName());
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     *
     * @param response
     */
    public void logout(HttpServletResponse response) {

        try {
            String cookieValue = getCookie(TOOKEN_COOKIE_NAME);

            Session session = findSession(cookieValue);
            if (session != null && session.getId() > 0) {
                securityRepository.delete(Session.class, session.getId());
            }
            if (StringUtils.isNotBlank(cookieValue)) {
                Cookie cookie = new Cookie(TOOKEN_COOKIE_NAME, cookieValue);
                cookie.setPath("/");
                cookie.setDomain(request.getServerName());
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private String getCookie(String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0, c = cookies.length; i < c; ++i) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Session findSession(String key) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sessionKey", key);
        param.put("status", Constant.OPEN);

        List<Session> list = securityRepository.list(Session.class, " WHERE sessionKey = :sessionKey AND status = :status ", param);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }

        return null;
    }

    /**
     * 获取数据库中sessin与user数据
     * @return
     */
    public User getUserInfo() {
        Session session = recognize();
        User user = null;
        if (session != null) {
            user = getUserInfo(session.getUserId(), session.getAccount(), null);
        }
        return user;
    }

    public User getUserInfo(long id, String account, String password) {
        User user = new User();
        Map<String, Object> param = new HashMap<String, Object>();
        if (id > 0) {
            param.put("id", id);
        }
        if (StringUtils.isNotBlank(password)) {
            param.put("password", password);
        }
        param.put("account", account);
        param.put("status", Constant.OPEN);

        User userInfo = findAccount(User.class, param);
        if (userInfo != null && userInfo.getId() != 0) {
            user.setId(userInfo.getId());
            user.setAccount(userInfo.getAccount());
            user.setName(userInfo.getName());
            user.setPhone(userInfo.getPhone());
            user.setMail(userInfo.getMail());
            user.setDepict(userInfo.getDepict());
            user.setCreateDate(userInfo.getCreateDate());
            user.setUpdateDate(userInfo.getUpdateDate());
            user.setLastLoginDate(userInfo.getLastLoginDate());
        }
        return user;
    }

    /**
     * 获取session
     * @return
     */
    private Session recognize() {
        String sessionKey = getCookie(TOOKEN_COOKIE_NAME);
        Session session = null;
        if (StringUtils.isNotBlank(sessionKey)) {
            session = findSession(sessionKey);
        }
        return session;
    }
}
