/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package grails.plugin.spock.build.test.adapter

import spock.lang.Sputnik

class SuiteAdapter {
  final specks = []

  def leftShift(Class speck) {
    specks << speck
  }

  int countTestCases() {
    specks.sum(0) {
      new Sputnik(it).description.testCount()
    }
  }

  int testCount() {
    specks.size()
  }
}