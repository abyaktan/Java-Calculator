package de.commsult.sgudemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    Calculator calc;

    @BeforeEach
    void setupBeforeEachTest() {
        calc = new Calculator();
    }

    @Test
    void testAdd() {
        int actual = calc.add(5, 3);
        Assertions.assertEquals(8, actual);
    }

    @Test
    void testAdd2() {
        int actual = calc.add(7, 12);
        Assertions.assertEquals(19, actual);
    }

    @Test
    void testDivide() {
        double actual = calc.divide(10, 7);
        Assertions.assertEquals(1.4285, actual, 0.001);
    }

    @Test
    void testDivide2() {
        double actual = calc.divide(10, 2);
        Assertions.assertEquals(5, actual);
    }

    @Test
    void testDivideByZero() {
        IllegalArgumentException myException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calc.divide(10, 0);
        });

        Assertions.assertEquals("you can not divide by zero!", myException.getMessage());
    }

}
