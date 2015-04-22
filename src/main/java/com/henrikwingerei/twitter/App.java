package com.henrikwingerei.twitter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

public class App {

    public static void main(String[] args) throws Exception {
        Server server = new Server(getPort());
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.addServlet(CustomWebSocketServlet.class, "/ws");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/webapp");
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {handler, resourceHandler});
        server.setHandler(handlers);

        server.start();

        new Twitter().fetchTweets();
        
        server.join();
    }

    private static int getPort() {
        String port = getenv("PORT");

        if (port == null) {
            return 8080;
        } else {
            return parseInt(port);
        }
    }
}
