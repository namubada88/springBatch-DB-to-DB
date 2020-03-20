package com.bootbatch.job;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import com.bootbatch.main.UserVO;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class UserJob {

	private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactoy;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    @Bean
    public Job jdbcCursorItemReaderJob() {
    	return jobBuilderFactory.get("jdbcCursorItemReaderJob")
    			.start(jdbcCursorItemReaderStep())
    			.build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep() {
    			return stepBuilderFactoy.get("jdbcCursorItemReaderStep")
    			.<UserVO, UserVO>chunk(10)
    			//reader에서 받아서
    			.reader(jdbcCursorItemReader())
    			//writer에 넣는다.
    			.writer(jdbcCursorItemWriter())
    			.build();
    }


    @Bean
    JdbcBatchItemWriter<UserVO> jdbcCursorItemWriter(){
    	JdbcBatchItemWriter<UserVO> itemWriter = new JdbcBatchItemWriter<UserVO>();
    	itemWriter.setDataSource(dataSource);
    	itemWriter.setSql("insert into insert_count values(:count)");
    	itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UserVO>());
    	return itemWriter;
    }
    
    @Bean
    public JdbcCursorItemReader<UserVO> jdbcCursorItemReader(){
    	System.out.println("===>jdbcCursorItemReader 접속!!");
    	return new JdbcCursorItemReaderBuilder<UserVO>()
    			.fetchSize(10)
    			.dataSource(dataSource)
    			.rowMapper(new BeanPropertyRowMapper<>(UserVO.class))
    			.sql("SELECT COUNT FROM TOTAL_COUNT")
    			.name("jdbcCursorItemWriter")
    			.build();
 
    }
 
}
