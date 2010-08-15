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

package org.spockframework.runtime.extension.builtin;

import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.model.*;
import groovy.lang.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Luke Daley
 */
// NOTE: this implementation is not thread safe in that it expects beforeFeature and afterFeature to
//       be called in matching pairs. This needs to be addressed.
// 
//       Also, this implementation does not restore meta classes after each iteration of a parameterised
//       feature. It will only do so at the end. It's not clear if this is the correct behaviour here yet.
public class RestoreMetaClassRunListener extends AbstractRunListener {
  
  private final Map<Class, MetaClass> specLevelSavedMetaClasses = new HashMap<Class, MetaClass>();
  private final Map<Class, MetaClass> methodLevelSavedMetaClasses = new HashMap<Class, MetaClass>();
  
  private final Set<Class> specRestorations;
  private final Map<String, Set<Class>> methodRestorations;
  
  public RestoreMetaClassRunListener(Set<Class> specRestorations, Map<String, Set<Class>> methodRestorations) {
    this.specRestorations = specRestorations;
    this.methodRestorations = methodRestorations;
  }
  
  public void beforeSpec(SpecInfo spec) {
    if (specRestorations.isEmpty()) return;
    saveMetaClassesInto(specRestorations, specLevelSavedMetaClasses);
  }
  
  public void beforeFeature(FeatureInfo feature) {
    if (methodRestorations.isEmpty()) return;
    
    String methodName = feature.getFeatureMethod().getReflection().getName();
    if (!methodRestorations.containsKey(methodName)) return;
    
    saveMetaClassesInto(methodRestorations.get(methodName), methodLevelSavedMetaClasses);
  }
  
  public void afterFeature(FeatureInfo feature) {
    if (methodLevelSavedMetaClasses.isEmpty()) return;
    
    String methodName = feature.getFeatureMethod().getReflection().getName();
    restoreMetaClassesFromAndClear(methodLevelSavedMetaClasses);
  }
  
  public void afterSpec(SpecInfo spec) {
    if (specRestorations.isEmpty()) return;
    restoreMetaClassesFromAndClear(specLevelSavedMetaClasses);
  }
  
  private void saveMetaClassesInto(Set<Class> toSave, Map<Class, MetaClass> into) {
    MetaClassRegistry registry = GroovySystem.getMetaClassRegistry();

    for (Class clazz : toSave) {
      into.put(clazz, registry.getMetaClass(clazz));
      MetaClass newMetaClass = new ExpandoMetaClass(clazz, true, true);
      newMetaClass.initialize();
      registry.setMetaClass(clazz, newMetaClass);
    }
  }
  
  private void restoreMetaClassesFromAndClear(Map<Class, MetaClass> savedMetaClasses) {
    MetaClassRegistry registry = GroovySystem.getMetaClassRegistry();

    for (Entry<Class, MetaClass> entry : savedMetaClasses.entrySet()) {
      Class clazz = entry.getKey();
      MetaClass originalMetaClass = entry.getValue();
      
      registry.removeMetaClass(clazz);
      registry.setMetaClass(clazz, originalMetaClass);
    }
    savedMetaClasses.clear();
  }

}
