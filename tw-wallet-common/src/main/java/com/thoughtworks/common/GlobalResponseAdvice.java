package com.thoughtworks.common;

import com.thoughtworks.common.exception.AppException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@ControllerAdvice(annotations = RestController.class)
public class GlobalResponseAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
        MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        if (body instanceof ResponseBean) {
            return body;
        }
        if(Objects.requireNonNull(request.getMethod()).matches(HttpMethod.POST.toString())){
            return ResponseBean.createdResponse(body);
        }
        return ResponseBean.okResponse(body);
    }

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseBean applicationException(AppException appException,
        HttpServletResponse resp) {
        resp.setStatus(appException.getCode().getStatus().value());
        return new ResponseBean(appException.getCode().getErrCode(), appException.getMessage(),
            appException.getData());
    }
}
