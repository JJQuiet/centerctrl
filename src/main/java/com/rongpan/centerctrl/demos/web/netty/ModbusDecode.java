package com.rongpan.centerctrl.demos.web.netty;
import com.rongpan.centerctrl.demos.web.config.GlobalConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ModbusDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 传入的字节的长度
        int length = in.readableBytes();
        // 临时存储解码的消息
        StringBuilder sb = new StringBuilder();
        // 循环，直到没有足够的数据来解码为止
        while (in.isReadable()) {
            // 标记当前读索引
            in.markReaderIndex();
            // 读取一行数据
            byte readByte;
            boolean endOfLineFound = false;
            while(in.isReadable()) {
                readByte = in.readByte();
                if (readByte == '\n') { // 换行符表示一行的结束
                    endOfLineFound = true;
                    break;
                }
                sb.append((char) readByte);
            }
            // 如果这一行还没结束就没有更多数据了，则回到标记的读索引
            if (!endOfLineFound) {
                in.resetReaderIndex();
                return;
            }
            // 一条完整消息的处理逻辑
            String message = sb.toString().trim();
            if (!message.isEmpty()) {
                out.add(message); // 将解码的消息添加到输出列表中
                sb.setLength(0); // 清空StringBuilder以解码下一条消息
            }
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("TCP连接异常！，信息：{}",cause.getMessage(),cause);
//        ctx.close();
    }
}