/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spockframework.runtime.model;

public class ErrorInfo {
  private final MethodInfo method;
  private final Throwable error;
  private final int runStatus;

  public ErrorInfo(MethodInfo method, Throwable error, int runStatus) {
    this.method = method;
    this.error = error;
    this.runStatus = runStatus;
  }

  public MethodInfo getMethod() {
    return method;
  }

  public Throwable getException() {
    return error;
  }

  public int getRunStatus() {
    return runStatus;
  }
}
