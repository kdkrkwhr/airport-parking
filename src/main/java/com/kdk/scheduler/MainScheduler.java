package com.kdk.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.kdk.service.RealTimeService;

@Component
public class MainScheduler {

  static final Logger logger = LoggerFactory.getLogger(MainScheduler.class);

  @Autowired
  private RealTimeService service;

  @Scheduled(fixedDelay = 1000 * 60)
  public void minuteInsertDataScheduled() throws Exception {
    logger.debug("=== START :: minuteInsertDataScheduled ===");
    logger.debug("Result :: {}", service.minuteScheduleF(System.currentTimeMillis()));
    logger.debug("=== END :: minuteInsertDataScheduled ===");
  }
}
