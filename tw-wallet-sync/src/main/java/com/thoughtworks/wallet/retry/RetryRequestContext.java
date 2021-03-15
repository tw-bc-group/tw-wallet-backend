package com.thoughtworks.wallet.retry;

import com.thoughtworks.common.util.JacksonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* @description: 业务重试请求上下文
**/
@Getter
@Setter
@ToString
public class RetryRequestContext {


    private Class targetClass;


    private String methodName;


    private Object[] args;


    private MethodArg[] methodArgs;

    @Setter
    @Getter
    static class MethodArg {
        private Class clazz;
        private String jsonVal;
    }

    public void setArgs(Object[] args){
        MethodArg[] methodArgs = new MethodArg[args.length];
        for (int i = 0; i < args.length; i++) {
            methodArgs[i] = convert2MethodArg(args[i]);
        }
        setMethodArgs(methodArgs);
    }

    private MethodArg convert2MethodArg(Object arg){
        MethodArg methodArg = new MethodArg();
        methodArg.setClazz(arg.getClass());
        methodArg.setJsonVal(JacksonUtil.beanToJSonStr(arg));
        return methodArg;
    }

}

