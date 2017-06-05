package server;

/**
 * Created by Moorka on 02.06.2017.
 */


import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.*;
import org.jboss.netty.bootstrap.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    private static  int port;// = 11111;


    private Server(int port) {
        Server.port = port;

    }

    private void run () throws  Exception{

            ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
            ServerBootstrap bootstrap = new ServerBootstrap(factory);
            bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                public ChannelPipeline getPipeline() {
                    return Channels.pipeline(new GameChannelHandler());
                }
            });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(new InetSocketAddress(port));

    }

    public static void main(String[] args) {
        try {
            new Server(11111).run();
        } catch (Exception ignored) {}
    }
}
