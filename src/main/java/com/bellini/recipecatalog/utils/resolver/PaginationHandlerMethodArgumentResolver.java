package com.bellini.recipecatalog.utils.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bellini.recipecatalog.model.common.pagination.PaginationInfo;

public class PaginationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PaginationInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String offStr = webRequest.getParameter("offset");
        String limStr = webRequest.getParameter("limit");
        
        if (isNotSet(offStr)) {
            offStr = "0"; // FIXME
        }
        if (isNotSet(limStr)) {
            limStr = "10"; // FIXME
        }
        
        return new PaginationInfo(Long.parseLong(offStr), Integer.parseInt(limStr));
    }
    
    private static boolean isNotSet(String value) {
        return value == null;
    }

}
