package com.common.db;

import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class CodeDbSource {

	public static  DataSource initDbSouce(){
		// 配置真实数据源
	    Map<String, DataSource> dataSourceMap = new HashMap<>();	    
	    // 配置第一个数据源
	    BasicDataSource dataSource1 = new BasicDataSource();
	    dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource1.setUrl("jdbc:mysql://localhost:3308/xw_user0");
	    dataSource1.setUsername("root");
	    dataSource1.setPassword("1234");
	    dataSourceMap.put("xw_user0", dataSource1);
	    
	    // 配置第二个数据源
	    BasicDataSource dataSource2 = new BasicDataSource();
	    dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource2.setUrl("jdbc:mysql://localhost:3308/xw_user1");
	    dataSource2.setUsername("root");
	    dataSource2.setPassword("1234");
	    dataSourceMap.put("xw_user1", dataSource2);
	    
	    // 配置Order表规则
	    TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
	    orderTableRuleConfig.setLogicTable("t_order");
	    orderTableRuleConfig.setActualDataNodes("xw_user${0..1}.t_order_${0..1}");
	    
	    // 配置分库 + 分表策略
	    orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "xw_user${user_id % 2}"));
	    orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "t_order_${user_id % 2}"));
	    
	    // 配置分片规则
	    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
	    shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
	    
	    // 省略配置order_item表规则...
	    // ...
	    
	    // 获取数据源对象
	    try {
			DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap<String, Object>(), new Properties());
			return dataSource;
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//测试
	public static void main(String[] args) {
		DataSource dataSource =initDbSouce();
		Connection s=null;
		Statement d =null;
		ResultSet rest=null;
		try {
			s = dataSource.getConnection();
			d = s.createStatement();
			//插入
			//d.executeUpdate("insert into t_order(order_code,name,user_id,price,create_date) values('201812123456','双12商品',4,200,'2018-12-12 11:11:11')");
			//查询
			rest = d.executeQuery("select * from t_order where user_id=4");
			 while(rest.next()){
			        int id = rest.getInt(1);
			        String code = rest.getString(2);
			        String name = rest.getString(3);
			        BigDecimal price = rest.getBigDecimal(4);
			        System.out.println("id:"+id+" code："+code+" name："+name+"价格:"+price);
			    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{		
			try {
				d.close();
				s.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
