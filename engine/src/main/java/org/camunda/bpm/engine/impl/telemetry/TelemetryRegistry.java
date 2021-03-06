/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.telemetry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.impl.telemetry.dto.ApplicationServer;
import org.camunda.bpm.engine.impl.util.ClockUtil;

public class TelemetryRegistry {

  public static final String UNIQUE_TASK_WORKERS = "unique-task-workers";

  protected Map<String, CommandCounter> commands = new HashMap<>();
  protected ApplicationServer applicationServer;
  protected Date startReportTime = ClockUtil.getCurrentTime();
  protected String camundaIntegration;

  public synchronized ApplicationServer getApplicationServer() {
    return applicationServer;
  }

  public synchronized void setApplicationServer(ApplicationServer applicationServer) {
    this.applicationServer = applicationServer;
  }

  public synchronized void setApplicationServer(String applicationServerVersion) {
    this.applicationServer = new ApplicationServer(applicationServerVersion);
  }

  public Map<String, CommandCounter> getCommands() {
    return commands;
  }

  public Date getStartReportTime() {
    return startReportTime;
  }

  public void setStartReportTime(Date lastReport) {
    this.startReportTime = lastReport;
  }

  public String getCamundaIntegration() {
    return camundaIntegration;
  }

  public void setCamundaIntegration(String camundaIntegration) {
    this.camundaIntegration = camundaIntegration;
  }

  public void markOccurrence(String name) {
    markOccurrence(name, 1);
  }

  public void markOccurrence(String name, long times) {
    CommandCounter counter = commands.get(name);
    if (counter == null) {
      synchronized (commands) {
        if (counter == null) {
          counter = new CommandCounter(name);
          commands.put(name, counter);
        }
      }
    }

    counter.mark(times);
  }

  public void clear() {
    commands.clear();
  }

}
