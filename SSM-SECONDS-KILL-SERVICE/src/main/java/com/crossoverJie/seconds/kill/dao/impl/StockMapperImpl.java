package com.crossoverJie.seconds.kill.dao.impl;

import java.util.List;

import com.crossoverJie.seconds.kill.dao.StockMapper;
import com.crossoverJie.seconds.kill.pojo.Stock;
import com.crossoverJie.seconds.kill.base.BaseDaoImpl;

public class StockMapperImpl extends BaseDaoImpl<Stock> implements StockMapper {

	@Override
	public int insert(Stock stock) {
		return super.insert(stock);
	}

	@Override
	public Stock selectByPrimaryKey(Integer id) {
		return super.getById(id);
	}


	@Override
	public int updateByPrimaryKey(Stock stock) {
		return super.update(stock);
	}

	@Override
	public int updateByOptimistic(Stock stock) {
		return super.update(stock);
	}  

}
