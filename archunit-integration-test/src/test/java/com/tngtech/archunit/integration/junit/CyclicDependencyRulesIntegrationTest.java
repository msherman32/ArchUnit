package com.tngtech.archunit.integration.junit;

import com.tngtech.archunit.example.cycle.complexcycles.slice1.ClassBeingCalledInSliceOne;
import com.tngtech.archunit.example.cycle.complexcycles.slice1.ClassOfMinimalCircleCallingSliceTwo;
import com.tngtech.archunit.example.cycle.complexcycles.slice1.SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree;
import com.tngtech.archunit.example.cycle.complexcycles.slice2.ClassOfMinimalCircleCallingSliceOne;
import com.tngtech.archunit.example.cycle.complexcycles.slice2.InstantiatedClassInSliceTwo;
import com.tngtech.archunit.example.cycle.complexcycles.slice2.SliceTwoInheritingFromSliceOne;
import com.tngtech.archunit.example.cycle.complexcycles.slice2.SliceTwoInheritingFromSliceThreeAndAccessingFieldInSliceFour;
import com.tngtech.archunit.example.cycle.complexcycles.slice3.ClassCallingConstructorInSliceFive;
import com.tngtech.archunit.example.cycle.complexcycles.slice3.InheritedClassInSliceThree;
import com.tngtech.archunit.example.cycle.complexcycles.slice4.ClassWithAccessedFieldCallingMethodInSliceOne;
import com.tngtech.archunit.example.cycle.complexcycles.slice5.InstantiatedClassInSliceFive;
import com.tngtech.archunit.example.cycle.constructorcycle.slice1.SliceOneCallingConstructorInSliceTwo;
import com.tngtech.archunit.example.cycle.constructorcycle.slice1.SomeClassWithCalledConstructor;
import com.tngtech.archunit.example.cycle.constructorcycle.slice2.SliceTwoCallingConstructorInSliceOne;
import com.tngtech.archunit.example.cycle.fieldaccesscycle.slice1.ClassInSliceOneWithAccessedField;
import com.tngtech.archunit.example.cycle.fieldaccesscycle.slice1.SliceOneAccessingFieldInSliceTwo;
import com.tngtech.archunit.example.cycle.fieldaccesscycle.slice2.SliceTwoAccessingFieldInSliceOne;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice1.ClassThatCallSliceThree;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice1.ClassThatInheritsFromSliceTwo;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice1.ClassThatIsInheritedFromSliceTwo;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice1.InterfaceInSliceOne;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice2.ClassThatInheritsFromSliceOne;
import com.tngtech.archunit.example.cycle.inheritancecycle.slice3.ClassThatImplementsInterfaceFromSliceOne;
import com.tngtech.archunit.example.cycle.membercycle.slice1.SliceOneWithFieldInSliceTwo;
import com.tngtech.archunit.example.cycle.membercycle.slice2.SliceTwoWithMethodParameterInSliceThree;
import com.tngtech.archunit.example.cycle.membercycle.slice3.SliceThreeWithMethodReturnTypeInSliceFour;
import com.tngtech.archunit.example.cycle.membercycle.slice4.SliceFourWithConstructorParameterInSliceOne;
import com.tngtech.archunit.example.cycle.simplecycle.slice1.SliceOneCallingMethodInSliceTwo;
import com.tngtech.archunit.example.cycle.simplecycle.slice1.SomeClassBeingCalledInSliceOne;
import com.tngtech.archunit.example.cycle.simplecycle.slice2.SliceTwoCallingMethodOfSliceThree;
import com.tngtech.archunit.example.cycle.simplecycle.slice3.SliceThreeCallingMethodOfSliceOne;
import com.tngtech.archunit.example.cycle.simplescenario.administration.AdministrationService;
import com.tngtech.archunit.example.cycle.simplescenario.administration.Invoice;
import com.tngtech.archunit.example.cycle.simplescenario.importer.ImportService;
import com.tngtech.archunit.example.cycle.simplescenario.report.Report;
import com.tngtech.archunit.example.cycle.simplescenario.report.ReportService;
import com.tngtech.archunit.exampletest.junit.CyclicDependencyRulesTest;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitIntegrationTestRunner;
import com.tngtech.archunit.junit.CalledByArchUnitIntegrationTestRunner;
import com.tngtech.archunit.junit.ExpectsViolations;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.core.domain.JavaConstructor.CONSTRUCTOR_NAME;
import static com.tngtech.archunit.integration.junit.CyclicErrorMatcher.cycle;
import static com.tngtech.archunit.junit.ExpectedAccess.accessFromMethod;
import static com.tngtech.archunit.junit.ExpectedDependency.constructor;
import static com.tngtech.archunit.junit.ExpectedDependency.field;
import static com.tngtech.archunit.junit.ExpectedDependency.inheritanceFrom;
import static com.tngtech.archunit.junit.ExpectedDependency.method;

@RunWith(ArchUnitIntegrationTestRunner.class)
@AnalyzeClasses(packages = "com.tngtech.archunit.example.cycle")
public class CyclicDependencyRulesIntegrationTest {

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromSimpleCycle")
    public static final ArchRule no_cycles_by_method_calls_between_slices =
            CyclicDependencyRulesTest.no_cycles_by_method_calls_between_slices;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromConstructorCycle")
    public static final ArchRule no_cycles_by_constructor_calls_between_slices =
            CyclicDependencyRulesTest.no_cycles_by_constructor_calls_between_slices;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromInheritanceCycle")
    public static final ArchRule no_cycles_by_inheritance_between_slices =
            CyclicDependencyRulesTest.no_cycles_by_inheritance_between_slices;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromFieldAccessCycle")
    public static final ArchRule no_cycles_by_field_access_between_slices =
            CyclicDependencyRulesTest.no_cycles_by_field_access_between_slices;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromMemberDependencyCycle")
    public static final ArchRule no_cycles_by_member_dependencies_between_slices =
            CyclicDependencyRulesTest.no_cycles_by_member_dependencies_between_slices;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromSimpleCyclicScenario")
    public static final ArchRule no_cycles_in_simple_scenario =
            CyclicDependencyRulesTest.no_cycles_in_simple_scenario;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromComplexCyclicScenario")
    public static final ArchRule no_cycles_in_complex_scenario =
            CyclicDependencyRulesTest.no_cycles_in_complex_scenario;

    @ArchTest
    @ExpectedViolationFrom(location = CyclicDependencyRulesIntegrationTest.class, method = "expectViolationFromComplexCyclicScenarioWithCustomIgnore")
    public static final ArchRule no_cycles_in_complex_scenario_with_custom_ignore =
            CyclicDependencyRulesTest.no_cycles_in_complex_scenario_with_custom_ignore;

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromSimpleCycle(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(simplecycle).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of simplecycle")
                        .by(accessFromMethod(SliceOneCallingMethodInSliceTwo.class, "callSliceTwo")
                                .toMethod(SliceTwoCallingMethodOfSliceThree.class, "doSomethingInSliceTwo")
                                .inLine(9))
                        .by(field(SliceOneCallingMethodInSliceTwo.class, "classInSliceTwo")
                                .ofType(SliceTwoCallingMethodOfSliceThree.class))
                        .from("slice2 of simplecycle")
                        .by(accessFromMethod(SliceTwoCallingMethodOfSliceThree.class, "callSliceThree")
                                .toMethod(SliceThreeCallingMethodOfSliceOne.class, "doSomethingInSliceThree")
                                .inLine(9))
                        .by(field(SliceTwoCallingMethodOfSliceThree.class, "classInSliceThree")
                                .ofType(SliceThreeCallingMethodOfSliceOne.class))
                        .from("slice3 of simplecycle")
                        .by(accessFromMethod(SliceThreeCallingMethodOfSliceOne.class, "callSliceOne")
                                .toMethod(SomeClassBeingCalledInSliceOne.class, "doSomethingInSliceOne")
                                .inLine(9))
                        .by(field(SliceThreeCallingMethodOfSliceOne.class, "someClassInSliceOne")
                                .ofType(SomeClassBeingCalledInSliceOne.class)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromConstructorCycle(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(constructorcycle).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of constructorcycle")
                        .by(accessFromMethod(SliceOneCallingConstructorInSliceTwo.class, "callSliceTwo")
                                .toConstructor(SliceTwoCallingConstructorInSliceOne.class)
                                .inLine(7))
                        .from("slice2 of constructorcycle")
                        .by(accessFromMethod(SliceTwoCallingConstructorInSliceOne.class, "callSliceOne")
                                .toConstructor(SomeClassWithCalledConstructor.class)
                                .inLine(7)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromInheritanceCycle(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(inheritancecycle).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of inheritancecycle")
                        .by(inheritanceFrom(ClassThatInheritsFromSliceTwo.class)
                                .extending(ClassThatInheritsFromSliceOne.class))
                        .by(accessFromMethod(ClassThatInheritsFromSliceTwo.class, CONSTRUCTOR_NAME)
                                .toConstructor(ClassThatInheritsFromSliceOne.class)
                                .inLine(5))
                        .from("slice2 of inheritancecycle")
                        .by(inheritanceFrom(ClassThatInheritsFromSliceOne.class)
                                .extending(ClassThatIsInheritedFromSliceTwo.class))
                        .by(accessFromMethod(ClassThatInheritsFromSliceOne.class, CONSTRUCTOR_NAME)
                                .toConstructor(ClassThatIsInheritedFromSliceTwo.class)
                                .inLine(5)))

                .by(cycle()
                        .from("slice1 of inheritancecycle")
                        .by(accessFromMethod(ClassThatCallSliceThree.class, CONSTRUCTOR_NAME)
                                .toConstructor(ClassThatImplementsInterfaceFromSliceOne.class)
                                .inLine(7))
                        .from("slice3 of inheritancecycle")
                        .by(inheritanceFrom(ClassThatImplementsInterfaceFromSliceOne.class)
                                .implementing(InterfaceInSliceOne.class)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromFieldAccessCycle(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(fieldaccesscycle).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of fieldaccesscycle")
                        .by(field(SliceOneAccessingFieldInSliceTwo.class, "classInSliceTwo")
                                .ofType(SliceTwoAccessingFieldInSliceOne.class))
                        .by(accessFromMethod(SliceOneAccessingFieldInSliceTwo.class, "accessSliceTwo")
                                .setting().field(SliceTwoAccessingFieldInSliceOne.class, "accessedField")
                                .inLine(9))
                        .from("slice2 of fieldaccesscycle")
                        .by(field(SliceTwoAccessingFieldInSliceOne.class, "classInSliceOne")
                                .ofType(ClassInSliceOneWithAccessedField.class))
                        .by(accessFromMethod(SliceTwoAccessingFieldInSliceOne.class, "accessSliceOne")
                                .setting().field(ClassInSliceOneWithAccessedField.class, "accessedField")
                                .inLine(10)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromMemberDependencyCycle(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(membercycle).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of membercycle")
                        .by(field(SliceOneWithFieldInSliceTwo.class, "classInSliceTwo")
                                .ofType(SliceTwoWithMethodParameterInSliceThree.class))
                        .from("slice2 of membercycle")
                        .by(method(SliceTwoWithMethodParameterInSliceThree.class, "methodWithParameterInSliceThree")
                                .hasParameter(SliceThreeWithMethodReturnTypeInSliceFour.class))
                        .from("slice3 of membercycle")
                        .by(method(SliceThreeWithMethodReturnTypeInSliceFour.class, "methodWithReturnTypeInSliceFour")
                                .withReturnType(SliceFourWithConstructorParameterInSliceOne.class))
                        .from("slice4 of membercycle")
                        .by(constructor(SliceFourWithConstructorParameterInSliceOne.class, CONSTRUCTOR_NAME)
                                .hasParameter(SliceOneWithFieldInSliceTwo.class)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromSimpleCyclicScenario(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..simplescenario.(*)..' should be free of cycles")
                .by(cycle().from("administration")
                        .by(accessFromMethod(AdministrationService.class, "saveNewInvoice", Invoice.class)
                                .toMethod(ReportService.class, "getReport", String.class)
                                .inLine(12))
                        .by(field(AdministrationService.class, "reportService")
                                .ofType(ReportService.class))
                        .by(accessFromMethod(AdministrationService.class, "saveNewInvoice", Invoice.class)
                                .toMethod(Report.class, "isEmpty")
                                .inLine(13))
                        .from("report")
                        .by(accessFromMethod(ReportService.class, "getReport", String.class)
                                .toMethod(ImportService.class, "process", String.class)
                                .inLine(10))
                        .by(field(ReportService.class, "importService")
                                .ofType(ImportService.class))
                        .from("importer")
                        .by(accessFromMethod(ImportService.class, "process", String.class)
                                .toMethod(AdministrationService.class, "createCustomerId", String.class)
                                .inLine(11))
                        .by(field(ImportService.class, "administrationService")
                                .ofType(AdministrationService.class)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromComplexCyclicScenario(ExpectsViolations expectsViolations) {
        expectViolationFromComplexCyclicScenarioWithCustomIgnore(expectsViolations);

        expectsViolations
                .by(cycle().from("slice1 of complexcycles")
                        .by(accessFromMethod(ClassOfMinimalCircleCallingSliceTwo.class, "callSliceTwo")
                                .toMethod(ClassOfMinimalCircleCallingSliceOne.class, "callSliceOne")
                                .inLine(9))
                        //                        .by(field(ClassOfMinimalCircleCallingSliceOne.class, "classInSliceOne")
                        //                                .ofType(ClassOfMinimalCircleCallingSliceTwo.class)) FIXME: should this be included?
                        //                        .by(field(ClassOfMinimalCircleCallingSliceTwo.class, "classInSliceTwo")
                        //                                .ofType(ClassOfMinimalCircleCallingSliceOne.class))
                        .by(accessFromMethod(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class, "callSliceTwo")
                                .toConstructor(InstantiatedClassInSliceTwo.class)
                                .inLine(10))
                        .from("slice2 of complexcycles")
                        .by(accessFromMethod(SliceTwoInheritingFromSliceThreeAndAccessingFieldInSliceFour.class, "accessSliceFour")
                                .toConstructor(ClassWithAccessedFieldCallingMethodInSliceOne.class)
                                .inLine(8))
                        .by(accessFromMethod(SliceTwoInheritingFromSliceThreeAndAccessingFieldInSliceFour.class, "accessSliceFour")
                                .setting().field(ClassWithAccessedFieldCallingMethodInSliceOne.class, "accessedField")
                                .inLine(8))
                        .from("slice4 of complexcycles")
                        .by(accessFromMethod(ClassWithAccessedFieldCallingMethodInSliceOne.class, "callSliceOne")
                                .toMethod(ClassBeingCalledInSliceOne.class, "doSomethingInSliceOne")
                                .inLine(10))
                        .by(field(ClassWithAccessedFieldCallingMethodInSliceOne.class, "classInSliceOne")
                                .ofType(ClassBeingCalledInSliceOne.class)))

                .by(cycle().from("slice1 of complexcycles")
                        .by(accessFromMethod(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class, "callSliceThree")
                                .toMethod(ClassCallingConstructorInSliceFive.class, "callSliceFive")
                                .inLine(14))
                        .by(field(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class, "classInSliceThree")
                                .ofType(ClassCallingConstructorInSliceFive.class))
                        .from("slice3 of complexcycles")
                        .by(accessFromMethod(ClassCallingConstructorInSliceFive.class, "callSliceFive")
                                .toConstructor(InstantiatedClassInSliceFive.class)
                                .inLine(7))
                        .from("slice5 of complexcycles")
                        .by(accessFromMethod(InstantiatedClassInSliceFive.class, "callSliceOne")
                                .toConstructor(ClassBeingCalledInSliceOne.class)
                                .inLine(7))
                        .by(accessFromMethod(InstantiatedClassInSliceFive.class, "callSliceOne")
                                .toMethod(ClassBeingCalledInSliceOne.class, "doSomethingInSliceOne")
                                .inLine(7)));
    }

    @CalledByArchUnitIntegrationTestRunner
    static void expectViolationFromComplexCyclicScenarioWithCustomIgnore(ExpectsViolations expectsViolations) {
        expectsViolations.ofRule("slices matching '..(complexcycles).(*)..' should be free of cycles")
                .by(cycle()
                        .from("slice1 of complexcycles")
                        .by(accessFromMethod(ClassOfMinimalCircleCallingSliceTwo.class, "callSliceTwo")
                                .toMethod(ClassOfMinimalCircleCallingSliceOne.class, "callSliceOne")
                                .inLine(9))
                        .by(field(ClassOfMinimalCircleCallingSliceTwo.class, "classInSliceTwo")
                                .ofType(ClassOfMinimalCircleCallingSliceOne.class))
                        .by(accessFromMethod(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class, "callSliceTwo")
                                .toConstructor(InstantiatedClassInSliceTwo.class)
                                .inLine(10))
                        .from("slice2 of complexcycles")
                        .by(inheritanceFrom(SliceTwoInheritingFromSliceOne.class)
                                .extending(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class))
                        .by(accessFromMethod(SliceTwoInheritingFromSliceOne.class, CONSTRUCTOR_NAME)
                                .toConstructor(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class)
                                .inLine(5))
                        .by(accessFromMethod(ClassOfMinimalCircleCallingSliceOne.class, "callSliceOne")
                                .toMethod(ClassOfMinimalCircleCallingSliceTwo.class, "callSliceTwo")
                                .inLine(9))
                        .by(field(ClassOfMinimalCircleCallingSliceOne.class, "classInSliceOne")
                                .ofType(ClassOfMinimalCircleCallingSliceTwo.class)))

                .by(cycle().from("slice1 of complexcycles")
                        .by(accessFromMethod(ClassOfMinimalCircleCallingSliceTwo.class, "callSliceTwo")
                                .toMethod(ClassOfMinimalCircleCallingSliceOne.class, "callSliceOne")
                                .inLine(9))
                        .by(accessFromMethod(SliceOneCallingConstructorInSliceTwoAndMethodInSliceThree.class, "callSliceTwo")
                                .toConstructor(InstantiatedClassInSliceTwo.class)
                                .inLine(10))
                        .from("slice2 of complexcycles")
                        .by(inheritanceFrom(SliceTwoInheritingFromSliceThreeAndAccessingFieldInSliceFour.class)
                                .extending(InheritedClassInSliceThree.class))
                        .by(accessFromMethod(SliceTwoInheritingFromSliceThreeAndAccessingFieldInSliceFour.class, CONSTRUCTOR_NAME)
                                .toConstructor(InheritedClassInSliceThree.class)
                                .inLine(6))
                        .from("slice3 of complexcycles")
                        .by(accessFromMethod(ClassCallingConstructorInSliceFive.class, "callSliceFive")
                                .toConstructor(InstantiatedClassInSliceFive.class)
                                .inLine(7))
                        .from("slice5 of complexcycles")
                        .by(accessFromMethod(InstantiatedClassInSliceFive.class, "callSliceOne")
                                .toConstructor(ClassBeingCalledInSliceOne.class)
                                .inLine(7))
                        .by(accessFromMethod(InstantiatedClassInSliceFive.class, "callSliceOne")
                                .toMethod(ClassBeingCalledInSliceOne.class, "doSomethingInSliceOne")
                                .inLine(7)));
    }
}
