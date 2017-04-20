package com.core.security;

import com.core.config.Config;
import com.core.domain.User;
import com.core.security.annotation.AsRight;
import com.core.security.annotation.RightCheck;
import com.core.util.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by sunpeng
 */
@Aspect
@Component
public class CheckAdvice implements Ordered {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SupportFactory supportFactory;

    @Autowired
    protected ObjectMapper objectMapper;

    private int order = 1;

    @Autowired
    private HttpServletRequest request;

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Around("execution(* com.*.controller.*.*AH(..))")
    public Object checkRight(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            SecuritySupport securitySupport = supportFactory.getSecuritySupport();
            User user = securitySupport.getUserInfo();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            if (user == null || user.getId() == 0) {
                // spring annotation 'ResponseBody' or not
                if (method.isAnnotationPresent(ResponseBody.class)) {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("success", true);
                    objectNode.put("result", "login");
                    return objectNode.toString();
                } else {
                    return "redirect:login";
                }
            } else {
                // index.jsp (or other code) will use this.
                request.setAttribute("user", user);
                return joinPoint.proceed();
            }
        } catch (Throwable throwable) {
            logger.error(throwable);
            throw throwable;
        }
    }

}
