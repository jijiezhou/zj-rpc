package org.example.zj.rpc.core.common;

import java.io.Serializable;
import java.util.Arrays;

import static org.example.zj.rpc.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Classname RpcProtocol
 * @Description TODO
 * @Author zjj
 * @Date 3/13/24 1:59 PM
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5359096060555795690L;

    private short magicNumber = MAGIC_NUMBER;

    private int contentLength;

    //RpcInvocation
    private byte[] content;

    public RpcProtocol(byte[] content){
        this.contentLength = content.length;
        this.content = content;
    }

    public int getMagicNumber(){
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength(){
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
