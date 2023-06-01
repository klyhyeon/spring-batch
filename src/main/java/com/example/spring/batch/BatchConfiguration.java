package com.example.spring.batch;

import com.example.spring.batch.service.CustomService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

  @Autowired
  private CustomService customService;

  @Autowired
  private RandomDecider randomDecider;

  // @Bean
  // public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
  //     return new StepBuilder("step1", jobRepository)
  //             .tasklet(helloWorldTasklet(), platformTransactionManager).build();
  // }

  @Bean
  public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new JobBuilder("conditionalJob", jobRepository)
        .start(firstStep(jobRepository, platformTransactionManager))
        .on("FAILED").stopAndRestart(successStep(jobRepository, platformTransactionManager))
        .from(firstStep(jobRepository, platformTransactionManager)).on("*").to(successStep(jobRepository, platformTransactionManager))
        .end().build();
  }

  @Bean
  public Tasklet passTasklet() {
    return ((contribution, chunkContext) -> {
      throw new RuntimeException("Causing a failure");
    });
  }

  @Bean
  public Tasklet successTasklet() {
    return ((contribution, context) -> {
      System.out.println("Success!");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Tasklet failTasklet() {
    return ((contribution, context) -> {
      System.out.println("Failure!");
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  public Step firstStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("firstStep", jobRepository)
        .tasklet(passTasklet(), platformTransactionManager).build();
  }

  @Bean
  public Step successStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("successStep", jobRepository)
        .tasklet(successTasklet(), platformTransactionManager).build();
  }

  @Bean
  public Step failureStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("failureStep", jobRepository)
        .tasklet(failTasklet(), platformTransactionManager).build();
  }

  // public class HelloWorld implements Tasklet {
  //
  //     private static final String HELLO_WORLD = "Hello, %s";
  //
  //     @Override
  //     public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
  //
  //         customService.serviceMethod();
  //
  //         String name = (String) chunkContext
  //                 .getStepContext()
  //                 .getJobParameters()
  //                 .get("name");
  //
  //         ExecutionContext jobContext = chunkContext
  //                 .getStepContext()
  //                 .getStepExecution()
  //                 .getExecutionContext();
  //
  //         jobContext.put("name", name);
  //
  //         System.out.println(String.format(HELLO_WORLD, name));
  //
  //         return RepeatStatus.FINISHED;
  //     }
  // }
}
