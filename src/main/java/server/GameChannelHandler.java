package server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import protocol.GameResponce;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

/**
 * Created by Moorka on 02.06.2017.
 */

public class GameChannelHandler extends SimpleChannelHandler {


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        Session.Status status = SessionsManager.addPlayer(e.getChannel());
        ChannelBuffer channelBuffer = buffer(1);

        if (status.equals(Session.Status.WAIT_PLAYER_ZERO)) {
            channelBuffer.writeByte(GameResponce.YOU_CROSS);
            Channels.write(ctx, e.getFuture(), channelBuffer);
        } else if (status.equals(Session.Status.STEP_CROSS)) {
            channelBuffer.writeByte(GameResponce.YOU_ZERO);
            Channels.write(ctx, e.getFuture(), channelBuffer);
        }
    }



    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        if (buf.readable()) {
            byte x = buf.readByte();
            if (buf.readable()) {
                byte y = buf.readByte();
                Session.Status status = SessionsManager.step(e.getChannel().getId(), x, y);
            }
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

        Channel ch = e.getChannel();
        ch.close();
    }




    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        SessionsManager.disconnect(e.getChannel().getId());
    }

}

