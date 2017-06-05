package client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Created by Moorka on 02.06.2017.
 */
public class Client {

    private static final int port = 11111;

    private Client(int port) throws  Exception{

        ChannelFactory factory =
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new PlayerChannelHandler());
            }
        });

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

       bootstrap.connect(new InetSocketAddress("localhost", port));;

    }

    public static void main(String[] args) throws Exception {
        Client player = new Client(port);

    }
}
