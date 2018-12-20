package com.common.db;

import io.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class YamlDbSource {

	public static  DataSource initDbSouce() throws SQLException, IOException{
		File yamlFile = new File("src/main/resources/config-user.yaml");
		DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(yamlFile);
		return dataSource;
	}
	
	//测试
	public static void main(String[] args) {
		Connection s=null;
		Statement d =null;
		ResultSet rest=null;
		try {
			DataSource dataSource =initDbSouce();					
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{		
			try {
				if(null!=d){
					d.close();
				}
				if(null!=s){
					s.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
