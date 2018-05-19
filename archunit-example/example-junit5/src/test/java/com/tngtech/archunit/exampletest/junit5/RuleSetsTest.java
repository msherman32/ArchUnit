package com.tngtech.archunit.exampletest.junit5;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.Tag;

@Tag("example")
@AnalyzeClasses(packages = "com.tngtech.archunit.example")
class RuleSetsTest {
    @ArchTest
    static final ArchRules CODING_RULES = ArchRules.in(CodingRulesTest.class);

    @ArchTest
    static final ArchRules CYCLIC_DEPENDENCY_RULES = ArchRules.in(CyclicDependencyRulesTest.class);
}
