package com.common.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.sql.DataSource;

public class SpringDbSource {
	
	private static Map<String, DataSource> clientMaps;
	
	public static Map<String, DataSource> getClientMaps() {
		return clientMaps;
	}
	public static void setClientMaps(Map<String, DataSource> clientMaps) {
		SpringDbSource.clientMaps = clientMaps;
	}


	public DataSource getShardingDataSource(){
		return clientMaps.get("datasourceMap");
	}

	//测试
	public static void main(String[] args) {
		DataSource dataSource =clientMaps.get("datasourceMap");
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
