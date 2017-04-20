package com.core.security;

import org.springframework.stereotype.Component;

/**
 * Created by sunpeng
 */
@Component
public abstract class SupportFactory {

    public abstract com.core.security.SecuritySupport getSecuritySupport();

}
