package com.example.spring.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomDecider implements JobExecutionDecider {

  private Random random = new Random();

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

    if (random.nextBoolean()) {
      return new FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName());
    }
    return new FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
  }
}
