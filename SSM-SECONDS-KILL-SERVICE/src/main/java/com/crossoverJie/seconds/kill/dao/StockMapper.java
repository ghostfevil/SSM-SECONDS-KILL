package com.crossoverJie.seconds.kill.dao;

import com.crossoverJie.seconds.kill.pojo.Stock;
import com.crossoverJie.seconds.kill.pojo.StockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockMapper {

	int insert(Stock record);

    Stock selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Stock stock);

    int updateByOptimistic(Stock stock) ; 
}