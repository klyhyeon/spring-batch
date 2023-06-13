package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class BatchApplication {

  //jobRepository, platfromTrasactionManager는 SpringBatchConfiguration에서 자동으로 주입
  //    @Bean
  //    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
  //        return new StepBuilder("step1", jobRepository)
  //                .tasklet(helloWorldTasklet(), platformTransactionManager).build();
  //    }
  //
  //    @Bean
  //    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
  //        return new JobBuilder("basicJob", jobRepository)
  //                .start(step1(jobRepository, platformTransactionManager))
  //                .listener(new JobLoggerListener())
  //                .build();
  //    }
  //
  //    @Bean
  //    public Tasklet helloWorldTasklet() {
  //        return ((contribution, chunkContext) -> {
  //            String name = (String) chunkContext.getStepContext()
  //                    .getJobParameters()
  //                    .get("name");
  //            System.out.println(String.format("Hello, %s!", name));
  //            return RepeatStatus.FINISHED;
  //        });
  //    }

  @Bean
  public Tasklet loadStockFile() {
    return ((contribution, chunkContext) -> {
      System.out.println("The stock file has been loaded");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Tasklet loadCustomerFile() {
    return ((contribution, chunkContext) -> {
      System.out.println("The customer file has been loaded");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Tasklet updateStart() {
    return ((contribution, chunkContext) -> {
      System.out.println("The start has been updated");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Tasklet runBatchTasklet() {
    return ((contribution, chunkContext) -> {
      System.out.println("The batch has been run");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Job preProcessingJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new JobBuilder("preProcessingJob", jobRepository)
        .start(loadFileStep())
        .next(updateStartStep())
        .build();
  }




  public static void main(String[] args) {
    SpringApplication.run(BatchApplication.class, args);
  }

}
