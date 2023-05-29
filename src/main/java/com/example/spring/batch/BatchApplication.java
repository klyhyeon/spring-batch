package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class BatchApplication {

    //jobRepository, platfromTrasactionManager는 SpringBatchConfiguration에서 자동으로 주입
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloWorldTasklet(), platformTransactionManager).build();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("basicJob", jobRepository)
                .start(step1(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Tasklet helloWorldTasklet() {
        return ((contribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        });
    }


    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}
