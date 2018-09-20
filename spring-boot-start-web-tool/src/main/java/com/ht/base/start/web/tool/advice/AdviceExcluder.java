package com.ht.base.start.web.tool.advice;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hanchao
 * @date 2017/10/19 22:56
 */
public class AdviceExcluder {
    private Set<String> ignoreClasses = new HashSet<>();
    private PathMatcher pathMatcher = new AntPathMatcher();
    private List<String> ignoreUrls = new ArrayList<>();

    public AdviceExcluder() {
        this(null, null);
    }

    public AdviceExcluder(Set<String> ignoreClasses, List<String> ignoreUrls) {
        //_monitor不能定于在mapping中
        this.ignoreUrls.add("/**/_monitjor/**");
        this.ignoreUrls.add("/**/actuator/**");
        this.ignoreUrls.add("/**/swagger-resources/**");
        this.ignoreUrls.add("/**/v2/api-docs/**");
        this.ignoreUrls.add("/**/swagger-resources/configuration/security/**");
        this.ignoreClasses.add("org.springframework.hateoas.ResourceSupport");
        this.ignoreClasses.add("org.springframework.http.ResponseEntity");
        this.ignoreClasses.add("springfox.documentation.swagger.web.UiConfiguration");
        this.ignoreClasses.add("byte[]");
        this.ignoreClasses.add("java.lang.String");
        if (CollectionUtils.isNotEmpty(ignoreClasses)) {
            this.ignoreClasses.addAll(ignoreClasses);
        }
        if (CollectionUtils.isNotEmpty(ignoreUrls)) {
            this.ignoreUrls.addAll(ignoreUrls);
        }
    }

    public boolean ignore(Object o, ServerHttpRequest serverHttpRequest) {
        if (o != null && ignoreClasses.contains(o.getClass().getCanonicalName())) {
           // System.out.println(o.getClass().getCanonicalName());
            return true;
        }
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            String path = getRequestPath(((ServletServerHttpRequest) serverHttpRequest).getServletRequest());
            for (String ignoreUrl : ignoreUrls) {
                if (pathMatcher.match(ignoreUrl, path)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();

        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }

        return url;
    }

    private String getRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            url += ("?" + request.getQueryString());
        }
        return url;
    }
}
