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

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

abstract class AbstractArchUnitTestDescriptor extends AbstractTestDescriptor implements Node<ArchUnitEngineExecutionContext> {
    private final Set<TestTag> tags;

    AbstractArchUnitTestDescriptor(UniqueId uniqueId, String displayName, TestSource source, AnnotatedElement testElement) {
        super(uniqueId, displayName, source);
        this.tags = findTagsOn(testElement);
    }

    private Set<TestTag> findTagsOn(AnnotatedElement testClass) {
        return Arrays.stream(testClass.getAnnotationsByType(Tag.class))
                .map(annotation -> TestTag.create(annotation.value()))
                .collect(toSet());
    }

    @Override
    public Set<TestTag> getTags() {
        Set<TestTag> result = new HashSet<>(tags);
        result.addAll(getParent().map(TestDescriptor::getTags).orElse(emptySet()));
        return result;
    }
}
