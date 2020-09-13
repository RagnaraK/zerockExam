package org.zerock.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = {"org.zerock.service", "org.zerock.aop","org.zerock.task"})
@EnableAspectJAutoProxy	// AOP 어노테이션
@EnableScheduling		// 스케줄링 라이브러리 어노테이션(Quartz)
@EnableTransactionManagement	// 트랜잭션 처리 어노테이션
@MapperScan(basePackages = {"org.zerock.mapper"})
public class RootConfig {
	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		//hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		//hikariConfig.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:rockdb");
		hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:rockdb");
		hikariConfig.setUsername("rock");
		hikariConfig.setPassword("4444");
			    	    
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception{
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		return sqlSessionFactory.getObject();
	}
	
	@Bean
	public DataSourceTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
