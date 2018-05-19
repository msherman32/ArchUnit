/*
 * Copyright 2018 TNG Technology Consulting GmbH
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
package com.tngtech.archunit.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.google.common.annotations.VisibleForTesting;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.Description;

import static com.google.common.base.Preconditions.checkArgument;

class ArchRuleExecution extends ArchTestExecution {
    private final Field ruleField;
    @VisibleForTesting
    final ArchRule rule;

    ArchRuleExecution(Class<?> testClass, Field ruleField, boolean forceIgnore) {
        super(testClass, ruleField, forceIgnore);

        validatePublicStatic(ruleField);
        checkArgument(ArchRule.class.isAssignableFrom(ruleField.getType()),
                "Rule field %s.%s to check must be of type %s",
                testClass.getSimpleName(), ruleField.getName(), ArchRule.class.getSimpleName());

        this.ruleField = validatePublicStatic(ruleField);
        rule = getArchRule(testClass, ruleField);
    }

    private ArchRule getArchRule(Class<?> testClass, Field ruleField) {
        try {
            return (ArchRule) ruleField.get(testClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format(
                    "Cannot access field %s.%s", testClass.getName(), ruleField.getName()), e);
        }
    }

    @Override
    Result evaluateOn(JavaClasses classes) {
        try {
            rule.check(classes);
        } catch (Exception | AssertionError e) {
            return new NegativeResult(describeSelf(), e);
        }
        return new PositiveResult();
    }

    @Override
    Description describeSelf() {
        return Description.createTestDescription(testClass, ruleField.getName());
    }

    @Override
    String getName() {
        return ruleField.getName();
    }

    @Override
    <T extends Annotation> T getAnnotation(Class<T> type) {
        return ruleField.getAnnotation(type);
    }
}
