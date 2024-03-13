package org.example.zj.rpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static org.example.zj.rpc.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Classname RpcDecoder
 * @Description TODO
 * @Author zjj
 * @Date 3/13/24 2:23 PM
 */
public class RpcDecoder extends ByteToMessageDecoder {
    /**
     * protocol header base length(magic nuber 2 bytes + contentLength 4 bytes)
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        //1. check if larger then the base length
        if (byteBuf.readableBytes() >= BASE_LENGTH){
            //2. check the limit: manually set 1000 right now
            if (byteBuf.readableBytes() > 1000){
                //if greater than limit, skip all bytes: ignore the whole packet
                byteBuf.skipBytes(byteBuf.readableBytes());
            }

            //3. Check if magic number match
            int beginReader;
            while (true){
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                if (byteBuf.readShort() == MAGIC_NUMBER){
                    break;
                } else {
                    //if not magic number, invalid
                    channelHandlerContext.close();
                    return;
                }
            }

            //4. Check if content complete
            //contentLength of 4 byte
            int length = byteBuf.readInt();
            //left data is not full, reset the readerIndex
            if (byteBuf.readableBytes() < length){
                byteBuf.readerIndex(beginReader);
                return;
            }

            //5. Get the content and construct rpc protocol
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);

            //6. Decoded message to output list
            out.add(rpcProtocol);
        }
    }
}
