package com.fan.yang.service;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fan.yang.domain.AccessLogEntity;
import com.fan.yang.repository.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhang_fan
 * @since 2023/1/9 下午 02:13
 */
@Service
@Slf4j
public class YangService {
    @Resource
    private AccessLogRepository accessLogRepository;

    @Transactional(rollbackOn = Exception.class)
    public void logRequestInfo(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
        log.info("ip: {}", request.getRemoteAddr());
        StringBuffer requestUrl = request.getRequestURL();
        log.info("url: {}", requestUrl);
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String osName = userAgent.getOs().getName();
        String browserName = userAgent.getBrowser().getName();
        String browserVersion = userAgent.getVersion();
        log.info("os: {}, browser: {}", osName, browserName + browserVersion);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> userCookie = Arrays.stream(cookies).filter(cookie -> "user".equals(cookie.getName())).findFirst();
            if (userCookie.isPresent()) {
                String userId = userCookie.get().getValue();
                Optional<AccessLogEntity> accessLogEntityOptional = accessLogRepository.findById(userId);

                if (accessLogEntityOptional.isPresent()) {
                    AccessLogEntity accessLogEntity = accessLogEntityOptional.get();
                    long visits = accessLogEntity.getVisits() + 1;
                    accessLogEntity.setVisits(visits);
                    log.info("第{}次访问", visits);
                    accessLogRepository.save(accessLogEntity);
                    return;
                }
            }
        }
        String userId = UUID.randomUUID().toString();
        Cookie user = new Cookie("user", userId);
        user.setMaxAge(Integer.MAX_VALUE);
        user.setPath("/");
        response.addCookie(user);
        AccessLogEntity accessLogEntity = new AccessLogEntity();
        accessLogEntity.setUserId(userId);
        accessLogEntity.setVisits(1L);
        accessLogRepository.save(accessLogEntity);
        log.info("第1次访问");
    }
}
