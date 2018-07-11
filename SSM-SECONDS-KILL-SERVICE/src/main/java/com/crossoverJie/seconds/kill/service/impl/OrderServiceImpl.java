package com.crossoverJie.seconds.kill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossoverJie.seconds.kill.api.constant.RedisKeysConstant;
import com.crossoverJie.seconds.kill.api.dto.JsonSerializer;
import com.crossoverJie.seconds.kill.dao.StockMapper;
import com.crossoverJie.seconds.kill.dao.StockOrderMapper;
import com.crossoverJie.seconds.kill.pojo.Stock;
import com.crossoverJie.seconds.kill.pojo.StockOrder;
import com.crossoverJie.seconds.kill.service.OrderService;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 01/05/2018 14:10
 * @since JDK 1.8
 */
@Transactional(rollbackFor = Exception.class)
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
    @Resource(name = "DBStockService")
    private com.crossoverJie.seconds.kill.service.StockService stockService;  **/
    
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockOrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate ;


    @Autowired
    private KafkaProducer kafkaProducer ;

    @Value("${kafka.topic}")
    private String kafkaTopic ;
    
    @Value("${kafka.brokerList}")
    private String brokerList ;

    @Value("${kafka.swicth}")
    private boolean check ;

    @Override
    public int createWrongOrder(int sid) throws Exception{

        //校验库存
        Stock stock = checkStock(sid);

        //扣库存
        saleStock(stock);

        //创建订单
        int id = createOrder(stock);

        return id;
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {

        //校验库存
        Stock stock = checkStock(sid);

        //乐观锁更新库存
        saleStockOptimistic(stock);

        //创建订单
        int id = createOrder(stock);

        return id;
    }

    @Override
    public int createOptimisticOrderUseRedis(int sid) throws Exception {
        //检验库存，从 Redis 获取
        Stock stock = checkStockByRedis(sid);

        //乐观锁更新库存 以及更新 Redis
        saleStockOptimisticByRedis(stock);

        //创建订单
        int id = createOrder(stock);
        return id ;
    }

    @Override
    public void createOptimisticOrderUseRedisAndKafka(int sid) throws Exception {

        //检验库存，从 Redis 获取
        Stock stock = checkStockByRedis(sid);

        //利用 Kafka 创建订单
        //初始化生产者
        Map<String, Object> props = new HashMap<String, Object>(16);
        /**
         * 0表示不等待结果返回<br/>
         * 1表示等待至少有一个服务器返回数据接收标识<br/>
         * -1表示必须接收到所有的服务器返回标识，及同步写入<br/>
         * */
        props.put("request.required.acks", "1");
        /**
         * 内部发送数据是异步还是同步
         * sync：同步, 默认
         * async：异步
         */
        props.put("producer.type", "async");
        
        props.put("queue.buffering.max.ms","50000"); 
        props.put("queue.buffering.max.messages","10000"); 
        props.put("queue.enqueue.timeout.ms","-1");
        // 异步提交的时候(async)，并发提交的记录数
        props.put("batch.num.messages", "400");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", brokerList);
        props.put("bootstrap.servers", brokerList);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", JsonSerializer.class);

        KafkaProducer<String, Stock> producer = new KafkaProducer(props);
       // kafkaProducer.send(new ProducerRecord(kafkaTopic,stock));
        producer.send(new ProducerRecord(kafkaTopic,stock));
        /**
        long startTime = System.currentTimeMillis();
        // 消息的value
        int messageKey = 1;
        String messageValue = "Message_" + messageKey;
        producer.send(new ProducerRecord(kafkaTopic,stock), new DemoCallBack<>(startTime, messageKey, stock)); **/
        logger.info("send Kafka success");

    }
    


    private Stock checkStockByRedis(Integer sid) throws Exception {
    	/**
    	System.out.println("redis.count.sid:"+sid+"$$$$"+redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + sid));
    	System.out.println("redis.sale.sid:"+sid+"$$$$"+redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_SALE + sid));
    	System.out.println("redis.version.sid:"+sid+"$$$$"+redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_VERSION + sid));
    	String redis_count = redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + sid);
    	String redis_sale =redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_SALE + sid);
    	String redis_version = redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_VERSION + sid);
    	redisTemplate.opsForValue().set("name", "手机");
    	//如果redis 没有就从数据库拿再更新redis
    	if(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + sid)==null) {
    		Stock stock = stockMapper.selectByPrimaryKey(sid);
    		redis_count = String.valueOf(stock.getCount());
    		redis_sale = String.valueOf(stock.getSale());
    		redis_version = String.valueOf(stock.getVersion());
    		redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_COUNT + sid, redis_count);
    		redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_SALE + sid, redis_sale);
    		redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_VERSION + sid, redis_version);
    		
    		
    	} **/
        Integer count = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + sid));
        Integer sale = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_SALE + sid));
        if (count.equals(sale)){
            throw new RuntimeException("库存不足 Redis currentCount=" + sale);
        }
        Integer version = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_VERSION + sid));
        Stock stock = new Stock() ;
        stock.setId(sid);
        stock.setCount(count);
        stock.setSale(sale);
        stock.setName(redisTemplate.opsForValue().get("name"));
        stock.setVersion(version);

        return stock;
    }

    /**
     * 乐观锁更新数据库 还要更新 Redis
     * @param stock
     */
    private void saleStockOptimisticByRedis(Stock stock) {

        int count = stockMapper.updateByOptimistic(stock);
        if (count == 0){
            throw new RuntimeException("并发更新库存失败") ;
        }
        //自增
       redisTemplate.opsForValue().increment(RedisKeysConstant.STOCK_SALE + stock.getId(),1) ;
       redisTemplate.opsForValue().increment(RedisKeysConstant.STOCK_VERSION + stock.getId(),1) ;
/*        redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_SALE + stock.getId(), String.valueOf(stock.getSale()+1));
        redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_VERSION + stock.getId(), String.valueOf(stock.getVersion()+1));*/

    }

    private Stock checkStock(int sid) {
        Stock stock = stockMapper.selectByPrimaryKey(sid);
        System.out.println("get stock ");
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    private void saleStockOptimistic(Stock stock) {
        int count = stockMapper.updateByOptimistic(stock);
        if (count == 0){
            throw new RuntimeException("并发更新库存失败") ;
        }
    }


    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        order.setCreateTime(new Date());
        int id = orderMapper.insert(order);
        return id;
    }

    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        return stockMapper.updateByPrimaryKey(stock);
    }
    
    class DemoCallBack<K, V> implements Callback {
        private final long startTime;
        private final K key;
        private final V value;

        public DemoCallBack(long startTime, K key, V value) {
            this.startTime = startTime;
            this.key = key;
            this.value = value;
        }

        /**
         * 生产者成功发送消息，收到Kafka服务端发来的ACK确认消息后，会调用此回调函数
         *
         * @param recordMetadata 生产者发送的消息的元数据，如果发送过程中出现异常，此参数为null
         * @param exception      发送过程中出现的异常，如果发送成功，则此参数为null
         */
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
            if (recordMetadata != null) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("message(" + key + "," + value + ") send to partition("
                        + recordMetadata.partition() + ")," + "offset(" + recordMetadata.offset() + ") in" + elapsedTime);
            } else {
                exception.printStackTrace();
            }
        }
    }    
}
