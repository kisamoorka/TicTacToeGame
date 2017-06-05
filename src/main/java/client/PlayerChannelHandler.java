package client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import protocol.GameResponce;

/**
 * Created by Moorka on 02.06.2017.
 */
public class PlayerChannelHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Player.setChannel(e.getChannel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
         Channel ch = e.getChannel();
         ch.close();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();

        if (channelBuffer.readable()) {
            byte status = channelBuffer.readByte();

            if ( (status == GameResponce.YOU_ZERO )|| (status == GameResponce.YOU_CROSS)) {
                byte temp = -1;
                Player.ReceiveMessage(status, temp, temp);
            } else {
                if (channelBuffer.readable()){

                    byte x = channelBuffer.readByte();
                    if (channelBuffer.readable()){
                        byte y = channelBuffer.readByte();
                        Player.ReceiveMessage(status, x, y);
                    }
                }
            }
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("--client disconnected--");
        e.getChannel().close();
    }
}
