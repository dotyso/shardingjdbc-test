package com.divino.shardingjdbc.repository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.keygen.KeyGenerator;
import io.shardingjdbc.example.jdbc.yaml.RawJdbcYamlShardingTableMain;

public class JdbcRepository {

	private KeyGenerator keyGenerator = new io.shardingjdbc.core.keygen.DefaultKeyGenerator();
	
	public void run() throws SQLException, IOException {
		DataSource dataSource = ShardingDataSourceFactory.createDataSource(new File(RawJdbcYamlShardingTableMain.class.getResource("/META-INF/MasterSlaveShardingByUserId.yaml").getFile()));
		
		//数据插入测试
		//new Repository().insert(dataSource);
		
		//数据查询测试
		for (int i=0; i<100; i++) {
			printInSelect(dataSource);
		}
	}
	
    public void printInSelect(DataSource dataSource) throws SQLException {
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id in (10, 11) order by o.order_id";
    	//String sql = "SELECT 0, order_id, user_id FROM t_order o order by o.order_id limit 5, 5";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            //preparedStatement.setInt(1, 10);
            //preparedStatement.setInt(2, 11);
            printSimpleSelect(preparedStatement);
        }
    }

    private void printSimpleSelect(final PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                System.out.print("item_id:" + rs.getLong(1) + ", ");
                System.out.print("order_id:" + rs.getLong(2) + ", ");
                System.out.print("user_id:" + rs.getInt(3));
                System.out.println();
            }
        }
    }
    
	public void insert(DataSource dataSource) throws SQLException {
		long orderId = 0;
		
		for (int i = 1; i < 10; i++) {
			orderId = (long) keyGenerator.generateKey();
			System.out.println(orderId);			
			execute(dataSource, String.format("INSERT INTO t_order (order_id, user_id, status, create_date) VALUES (%d, 10, 'INIT', now())", orderId));
		    execute(dataSource, String.format("INSERT INTO t_order_item (order_id, user_id, create_date) VALUES (%d, 10, now())", orderId));
		    execute(dataSource, String.format("INSERT INTO t_order_item (order_id, user_id, create_date) VALUES (%d, 10, now())", orderId));
		    
		    orderId = (long) keyGenerator.generateKey();
			System.out.println(orderId);
		    execute(dataSource, String.format("INSERT INTO t_order (order_id, user_id, status, create_date) VALUES (%d, 11, 'INIT', now())", orderId));
		    execute(dataSource, String.format("INSERT INTO t_order_item (order_id, user_id, create_date) VALUES (%d, 11, now())", orderId));
		    execute(dataSource, String.format("INSERT INTO t_order_item (order_id, user_id, create_date) VALUES (%d, 11, now())", orderId));
		    execute(dataSource, String.format("INSERT INTO t_order_item (order_id, user_id, create_date) VALUES (%d, 11, now())", orderId));
		}
	}

    private void execute(final DataSource dataSource, final String sql) throws SQLException {
        try (
                Connection conn = dataSource.getConnection();
                Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }
	
	private long executeAndGetGeneratedKey(final DataSource dataSource, final String sql) throws SQLException {
		long result = -1;
		try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				result = resultSet.getLong(1);
			}
		}
		return result;
	}
}
