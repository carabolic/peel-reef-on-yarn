/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.peelframework.carabolic.yarn.hello;

import org.apache.reef.driver.evaluator.AllocatedEvaluator;
import org.apache.reef.driver.evaluator.EvaluatorRequest;
import org.apache.reef.driver.evaluator.EvaluatorRequestor;
import org.apache.reef.driver.evaluator.JVMProcess;
import org.apache.reef.driver.evaluator.JVMProcessFactory;
import org.apache.reef.driver.task.TaskConfiguration;
import org.apache.reef.tang.Configuration;
import org.apache.reef.tang.annotations.Unit;
import org.apache.reef.wake.EventHandler;
import org.apache.reef.wake.time.event.StartTime;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Driver code for the Hello JVM Options Application.
 * The additional JVM Options print out diagnostics at the Evaluator.
 */
@Unit
public final class HelloJVMOptionsDriver {

  private static final Logger LOG = Logger.getLogger(HelloJVMOptionsDriver.class.getName());

  private final EvaluatorRequestor requestor;
  private final JVMProcessFactory jvmProcessFactory;

  /**
   * Job driver constructor - instantiated via TANG.
   *
   * @param requestor evaluator requestor object used to create new evaluator containers.
   */
  @Inject
  private HelloJVMOptionsDriver(final EvaluatorRequestor requestor,
                                final JVMProcessFactory jvmProcessFactory) {
    this.requestor = requestor;
    this.jvmProcessFactory = jvmProcessFactory;
    LOG.log(Level.FINE, "Instantiated 'HelloDriver'");
  }

  /**
   * Handles the StartTime event: Request as single Evaluator.
   */
  public final class StartHandler implements EventHandler<StartTime> {
    @Override
    public void onNext(final StartTime startTime) {
      HelloJVMOptionsDriver.this.requestor.submit(EvaluatorRequest.newBuilder()
          .setNumber(1)
          .setMemory(64)
          .setNumberOfCores(1)
          .build());
      LOG.log(Level.INFO, "Requested Evaluator.");
    }
  }

  /**
   * Handles AllocatedEvaluator: Submit the HelloTask with additional JVM Options.
   * The additional JVM Options print out diagnostics at the Evaluator.
   */
  public final class EvaluatorAllocatedHandler implements EventHandler<AllocatedEvaluator> {
    @Override
    public void onNext(final AllocatedEvaluator allocatedEvaluator) {
      LOG.log(Level.INFO, "Submitting HelloREEF task to AllocatedEvaluator: {0}", allocatedEvaluator);
      final Configuration taskConfiguration = TaskConfiguration.CONF
          .set(TaskConfiguration.IDENTIFIER, "HelloREEFTask")
          .set(TaskConfiguration.TASK, org.peelframework.carabolic.yarn.hello.HelloTask.class)
          .build();
      final JVMProcess jvmProcess = jvmProcessFactory.newEvaluatorProcess()
          .setMemory(56)
          .addOption("-Xdiag")
          .addOption("-XX:+PrintCommandLineFlags")
          .addOption("-XX:+PrintGCDetails");
      allocatedEvaluator.setProcess(jvmProcess);
      allocatedEvaluator.submitTask(taskConfiguration);
    }
  }
}
