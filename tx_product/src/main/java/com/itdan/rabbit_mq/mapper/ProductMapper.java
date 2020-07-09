package com.itdan.rabbit_mq.mapper;

import com.itdan.rabbit_mq.entry.Order;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Component;



@Mapper
@Component
public interface ProductMapper {
	public int update(Order order);
}
