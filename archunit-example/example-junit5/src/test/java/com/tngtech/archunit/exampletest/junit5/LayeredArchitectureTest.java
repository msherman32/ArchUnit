package com.tngtech.archunit.exampletest.junit5;

import com.tngtech.archunit.example.SomeMediator;
import com.tngtech.archunit.example.service.ServiceViolatingLayerRules;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Tag;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@Tag("example")
@AnalyzeClasses(packages = "com.tngtech.archunit.example")
class LayeredArchitectureTest {
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()

            .layer("Controllers").definedBy("com.tngtech.archunit.example.controller..")
            .layer("Services").definedBy("com.tngtech.archunit.example.service..")
            .layer("Persistence").definedBy("com.tngtech.archunit.example.persistence..")

            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");

    @ArchTest
    static final ArchRule layer_dependencies_are_respected_with_exception = layeredArchitecture()

            .layer("Controllers").definedBy("com.tngtech.archunit.example.controller..")
            .layer("Services").definedBy("com.tngtech.archunit.example.service..")
            .layer("Persistence").definedBy("com.tngtech.archunit.example.persistence..")

            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services")

            .ignoreDependency(SomeMediator.class, ServiceViolatingLayerRules.class);
}
