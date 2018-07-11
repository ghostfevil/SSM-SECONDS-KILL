package com.crossoverjie.kafka.orderconsumer.common.container;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.page.PageServlet;
import com.alibaba.dubbo.container.page.ResourceFilter;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * JettyContainer. (SPI, Singleton, ThreadSafe)
 */
public class JettyContainer implements Container {

    public static final String JETTY_PORT = "dubbo.jetty.port";
    public static final String JETTY_DIRECTORY = "dubbo.jetty.directory";
    public static final String JETTY_PAGES = "dubbo.jetty.page";
    public static final int DEFAULT_JETTY_PORT = 8091; //consumer jetty容器端口
    private static final Logger logger = LoggerFactory.getLogger(JettyContainer.class);
    SelectChannelConnector connector;

    public void start() {
        String serverPort = ConfigUtils.getProperty(JETTY_PORT);
        int port;
        if (serverPort == null || serverPort.length() == 0) {
            port = DEFAULT_JETTY_PORT;
        } else {
            port = Integer.parseInt(serverPort);
        }
        connector = new SelectChannelConnector();
        connector.setPort(port);
        ServletHandler handler = new ServletHandler();

        String resources = ConfigUtils.getProperty(JETTY_DIRECTORY);
        if (resources != null && resources.length() > 0) {
            FilterHolder resourceHolder = handler.addFilterWithMapping(ResourceFilter.class, "/*", Handler.DEFAULT);
            resourceHolder.setInitParameter("resources", resources);
        }

        ServletHolder pageHolder = handler.addServletWithMapping(PageServlet.class, "/*");
        pageHolder.setInitParameter("pages", ConfigUtils.getProperty(JETTY_PAGES));
        pageHolder.setInitOrder(2);
        
        // 日志监控系统的
        handler.addServletWithMapping(StatViewServlet.class, "/druid/*");

        Server server = new Server();
        server.addConnector(connector);
        server.addHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start jetty server on " + NetUtils.getLocalHost() + ":" + port + ", cause: " + e.getMessage(), e);
        }
    }

    public void stop() {
        try {
            if (connector != null) {
                connector.close();
                connector = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

}