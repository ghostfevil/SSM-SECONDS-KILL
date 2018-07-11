package com.crossoverJie.seconds.kill.dao;

import com.crossoverJie.seconds.kill.pojo.StockOrder;
import com.crossoverJie.seconds.kill.pojo.StockOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockOrderMapper {

    int insert(StockOrder record);


}