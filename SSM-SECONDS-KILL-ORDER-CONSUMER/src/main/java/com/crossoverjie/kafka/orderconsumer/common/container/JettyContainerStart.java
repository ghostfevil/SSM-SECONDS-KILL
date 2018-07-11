package com.crossoverjie.kafka.orderconsumer.common.container;

public class JettyContainerStart {
    public static void main(String[] args) {
         args = new String[] { "spring", "jetty"};  
        com.alibaba.dubbo.container.Main.main(args);
    }
}
