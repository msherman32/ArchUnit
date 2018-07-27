package com.tngtech.archunit.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaFieldAccess;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.domain.properties.HasOwner;
import com.tngtech.archunit.core.domain.properties.HasParameterTypes;

import static com.tngtech.archunit.core.domain.JavaConstructor.CONSTRUCTOR_NAME;
import static java.util.Collections.singleton;

abstract class ExpectedMember {
    private final Class<?> clazz;
    private final String memberName;
    private final List<String> params = new ArrayList<>();

    ExpectedMember(Class<?> clazz, String memberName, Class<?>[] paramTypes) {
        this.clazz = clazz;
        this.memberName = memberName;
        for (Class<?> paramType : paramTypes) {
            params.add(paramType.getName());
        }
    }

    String lineMessage(int number) {
        return String.format("(%s.java:%d)", clazz.getSimpleName(), number);
    }

    List<String> getParams() {
        return params;
    }

    String getMemberName() {
        return memberName;
    }

    Class<?> getDeclaringClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return String.format("%s.%s", clazz.getName(), memberName);
    }

    <T extends HasOwner<JavaClass> & HasName> boolean matches(T member) {
        return member.getOwner().isEquivalentTo(clazz) &&
                member.getName().equals(memberName) &&
                getParameters(member).equals(params);
    }

    private List<String> getParameters(Object member) {
        return member instanceof HasParameterTypes ?
                ((HasParameterTypes) member).getParameters().getNames() :
                Collections.<String>emptyList();
    }

    static class ExpectedOrigin extends ExpectedMember {
        private final String memberDescription;

        ExpectedOrigin(String memberDescription, Class<?> clazz, String methodName, Class<?>[] paramTypes) {
            super(clazz, methodName, paramTypes);
            this.memberDescription = memberDescription;
        }

        String getMemberDescription() {
            return memberDescription;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", super.toString(), Joiner.on(", ").join(getParams()));
        }
    }

    abstract static class ExpectedAccess extends ExpectedMember {
        private final ExpectedOrigin expectedOrigin;

        private ExpectedAccess(ExpectedOrigin expectedOrigin, Class<?> clazz, String memberName, Class<?>[] paramTypes) {
            super(clazz, memberName, paramTypes);
            this.expectedOrigin = expectedOrigin;
        }

        String messageFor(com.tngtech.archunit.junit.ExpectedAccess access) {
            return String.format(template(),
                    expectedOrigin.getMemberDescription(), access.getOrigin(), access.getTarget(), access.getOrigin().lineMessage(access.getLineNumber()));
        }

        abstract String template();
    }

    static class ExpectedFieldAccess extends ExpectedAccess {
        private final Map<Set<JavaFieldAccess.AccessType>, String> accessDescription = ImmutableMap.of(
                singleton(JavaFieldAccess.AccessType.GET), "gets",
                singleton(JavaFieldAccess.AccessType.SET), "sets",
                EnumSet.of(JavaFieldAccess.AccessType.GET, JavaFieldAccess.AccessType.SET), "accesses"
        );

        private final String accesses;

        ExpectedFieldAccess(ExpectedOrigin expectedOrigin, Class<?> clazz, String memberName, ImmutableSet<JavaFieldAccess.AccessType> accessTypes) {
            super(expectedOrigin, clazz, memberName, new Class<?>[0]);
            this.accesses = accessDescription.get(accessTypes);
        }

        @Override
        String template() {
            return "%s <%s> " + accesses + " field <%s> in %s";
        }
    }

    static class ExpectedMethodAccess extends ExpectedAccess {
        ExpectedMethodAccess(ExpectedOrigin expectedOrigin, Class<?> clazz, String memberName, Class<?>[] paramTypes) {
            super(expectedOrigin, clazz, memberName, paramTypes);
        }

        @Override
        String template() {
            return "%s <%s> calls method <%s> in %s";
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", super.toString(), Joiner.on(", ").join(getParams()));
        }
    }

    static class ExpectedConstructorAccess extends ExpectedMethodAccess {
        ExpectedConstructorAccess(ExpectedOrigin expectedOrigin, Class<?> clazz, Class<?>[] paramTypes) {
            super(expectedOrigin, clazz, CONSTRUCTOR_NAME, paramTypes);
        }

        @Override
        String template() {
            return "%s <%s> calls constructor <%s> in %s";
        }
    }
}
