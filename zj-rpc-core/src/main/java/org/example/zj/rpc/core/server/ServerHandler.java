package org.example.zj.rpc.core.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.zj.rpc.core.common.RpcInvocation;
import org.example.zj.rpc.core.common.RpcProtocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.example.zj.rpc.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Classname ServerHandler
 * @Description TODO
 * @Author zjj
 * @Date 3/13/24 2:57 PM
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws InvocationTargetException, IllegalAccessException {
        //1. Message Deserialization
        //1.1 RpcProtocol
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        //1.2 rpc request -> JSON string
        String json = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());
        //1.3 content byte -> RpcInvocation
        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);

        //2. Service method Invocation
        //2.1 service
        Object object = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
        //2.2 methods
        Method[] methods = object.getClass().getDeclaredMethods();
        Object result = null;
        for (Method method : methods) {
            if (method.getName().equals(rpcInvocation.getTargetMethod())) {
                //void method, simply invoke without result
                if (method.getReturnType().equals(Void.TYPE)) {
                    method.invoke(object, rpcInvocation.getArgs());
                } else {
                    //invoke + result
                    result = method.invoke(object, rpcInvocation.getArgs());
                }
                break;
            }
        }

        //3. Response Prep and Sending
        rpcInvocation.setResponse(result);
        RpcProtocol responseRpcProtocol = new RpcProtocol(JSON.toJSONString(rpcInvocation).getBytes());
        channelHandlerContext.writeAndFlush(responseRpcProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable e) {
        e.printStackTrace();
        Channel channel = channelHandlerContext.channel();
        if (channel.isActive()) {
            channelHandlerContext.close();
        }
    }
}
