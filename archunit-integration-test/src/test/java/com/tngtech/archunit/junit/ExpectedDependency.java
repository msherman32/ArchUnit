package com.tngtech.archunit.junit;

import com.tngtech.archunit.core.domain.Dependency;

import static java.util.regex.Pattern.quote;

public class ExpectedDependency implements ExpectedRelation {
    private final Class<?> origin;
    private final Class<?> target;
    private int lineNumber;
    private String completeDependencyPattern;

    private ExpectedDependency(Class<?> origin, Class<?> target, String completeDependencyPattern) {
        this(origin, target, 0, completeDependencyPattern);
    }

    private ExpectedDependency(Class<?> origin, Class<?> target, int lineNumber, String completeDependencyPattern) {
        this.origin = origin;
        this.target = target;
        this.lineNumber = lineNumber;
        this.completeDependencyPattern = completeDependencyPattern;
    }

    @Override
    public String toString() {
        return "Matches: " + completeDependencyPattern;
    }

    public static InheritanceCreator inheritanceFrom(Class<?> clazz) {
        return new InheritanceCreator(clazz);
    }

    public static AccessCreator accessFrom(Class<?> clazz) {
        return new AccessCreator(clazz);
    }

    public static FieldTypeCreator field(Class<?> owner, String fieldName) {
        return new FieldTypeCreator(owner, fieldName);
    }

    @Override
    public void associateLines(LineAssociation association) {
        association.associateIfPatternMatches(getInheritanceOrAccessPattern());
    }

    private String getInheritanceOrAccessPattern() {
        return completeDependencyPattern;
    }

    private static String getCompleteDescriptionPattern(Class<?> origin, String dependencyTypePattern, Class<?> target, int lineNumber) {
        return String.format(".*%s.*%s.*%s.*\\.java:%d.*",
                quote(origin.getName()), dependencyTypePattern, quote(target.getName()), lineNumber);
    }

    @Override
    public boolean correspondsTo(Object object) {
        if (!(object instanceof Dependency)) {
            return false;
        }

        Dependency dependency = (Dependency) object;
        boolean originMatches = dependency.getOriginClass().isEquivalentTo(origin);
        boolean targetMatches = dependency.getTargetClass().isEquivalentTo(target);
        boolean descriptionMatches = dependency.getDescription().matches(getInheritanceOrAccessPattern());
        return originMatches && targetMatches && descriptionMatches;
    }

    public static class InheritanceCreator {
        private final Class<?> clazz;

        private InheritanceCreator(Class<?> clazz) {
            this.clazz = clazz;
        }

        public ExpectedDependency extending(Class<?> superClass) {
            return new ExpectedDependency(clazz, superClass, getCompleteDescriptionPattern(clazz, "extends", superClass, 0));
        }

        public ExpectedDependency implementing(Class<?> anInterface) {
            return new ExpectedDependency(clazz, anInterface, getCompleteDescriptionPattern(clazz, "implements", anInterface, 0));
        }

    }

    public static class AccessCreator {
        private final Class<?> originClass;

        private AccessCreator(Class<?> originClass) {
            this.originClass = originClass;
        }

        public Step2 toFieldDeclaredIn(Class<?> clazz) {
            return new Step2(clazz, "(accesses|gets|sets)");
        }

        public Step2 toCodeUnitDeclaredIn(Class<?> clazz) {
            return new Step2(clazz, "calls");
        }

        public class Step2 {
            private final Class<?> targetClass;
            private final String description;

            Step2(Class<?> targetClass, String description) {
                this.targetClass = targetClass;
                this.description = description;
            }

            public ExpectedDependency inLineNumber(int lineNumber) {
                return new ExpectedDependency(originClass, targetClass, lineNumber, String.format(".*%s.*%s.*%s.*\\.java:%d.*",
                        quote(originClass.getName()), description, quote(targetClass.getName()), lineNumber));
            }
        }
    }

    public static class FieldTypeCreator {

        private final Class<?> owner;
        private final String fieldName;

        private FieldTypeCreator(Class<?> owner, String fieldName) {
            this.owner = owner;
            this.fieldName = fieldName;
        }

        public ExpectedDependency ofType(Class<?> type) {
            return new ExpectedDependency(owner, type,
                    getCompleteDescriptionFromFieldType(type));
        }

        private String getCompleteDescriptionFromFieldType(Class<?> type) {
            return String.format(".*%s.*%s\\.%s.*%s.*%s.*\\.java:%d.*",
                    "Field", owner.getName(), fieldName, "is of type", type.getName(), 0);
        }
    }
}
