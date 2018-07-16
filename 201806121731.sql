DELETE from stock_order;

update stock set sale =0,version=0  where id =1;


select * from stock_order;
select * from stock;


show full PROCESSLIST;

mysql.log:/home/jie/mysql/logs
重启mysql:service mysql.service restart
查看内存：free -m
查看进程：ps -ef|grep mysqld

#start kafka
nohup ./kafka-server-start.sh /usr/local/kafka/kafka_2.11-1.1.0/config/server.properties >/dev/null 2>&1 & 

#start kafka manager
nohup ./kafka-manager &

#创建TOPIC
 ./kafka-topics.sh --create --zookeeper 192.168.116.168:2181 --replication-factor 1 --partitions 1 --topic ORDER-TOPIC

#往ORDER-TOPIC 发送消息
 ./kafka-console-producer.sh --broker-list 192.168.116.168:9092 --topic ORDER-TOPIC
 
#消费ORDER-TOPIC 消息
 ./kafka-console-consumer.sh --zookeeper 192.168.116.168:2181 --topic ORDER-TOPIC --from-beginning
 
 ./kafka-console-consumer.sh --topic ORDER-TOPIC --zookeeper 192.168.116.168:2181



KafkaProducer<String, Stock> producer = new KafkaProducer(props);


tail -f 500

 Git:
 1. git init
 git config --global user.name "lwqenter"
 git config --global user.email "lwqenter@163.com"
 2. git pull git@github.com:lwqenter/SSM-SECONDS-KILL.git
 3. git add .
 4. git commit -m "201807111205_V1.0.1"
 5. git push --set-upstream git@github.com:lwqenter/kill.git master


https://github.com/lwqenter/SSM-SECONDS-KILL.git

    <bean id="druid-stat-interceptor"
          class="com.alibaba.druid.support.spring.stat.DruidStatInterceptor">
    </bean>

    <bean id="druid-stat-pointcut" class="org.springframework.aop.support.JdkRegexpMethodPointcut"
          scope="prototype">
        <property name="patterns">
            <list>
                <value>com.crossoverJie.seconds.kill.service.*</value>
                <value>com.crossoverJie.seconds.kill.dao.*</value>
            </list>
        </property>
    </bean>
   <!-- 20180614 add end -->    

    <aop:config>
        <aop:advisor advice-ref="druid-stat-interceptor" pointcut-ref="druid-stat-pointcut"/>
    </aop:config> 