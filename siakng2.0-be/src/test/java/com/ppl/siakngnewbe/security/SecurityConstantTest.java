package com.ppl.siakngnewbe.security;

import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConstantTest {
    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<SecurityConstant> securityConstant = SecurityConstant.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(securityConstant.getModifiers()));
        securityConstant.setAccessible(true);
        securityConstant.newInstance();
    }
}
