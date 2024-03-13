package org.example.zj.rpc.core.common;

import java.util.Arrays;

/**
 * @Classname RpcInvocation
 * @Description TODO
 * @Author zjj
 * @Date 3/13/24 2:06 PM
 */
public class RpcInvocation {
    //request target method
    private String targetMethod;

    //request target service name, ex：com.user.UserService
    private String targetServiceName;

    //request arguments
    private Object[] args;

    //match request & response
    private String uuid;

    //response; null if async call or void
    private Object response;

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetServiceName() {
        return targetServiceName;
    }

    public void setTargetServiceName(String targetServiceName) {
        this.targetServiceName = targetServiceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RpcInvocation{" +
                "targetMethod='" + targetMethod + '\'' +
                ", targetServiceName='" + targetServiceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", uuid='" + uuid + '\'' +
                ", response=" + response +
                '}';
    }
}
