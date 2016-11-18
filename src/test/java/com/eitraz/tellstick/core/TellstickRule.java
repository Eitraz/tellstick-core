package com.eitraz.tellstick.core;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TellstickRule implements TestRule {
    private Tellstick tellstick = new Tellstick();
    private TellstickCoreMockLibrary library = new TellstickCoreMockLibrary();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                tellstick.start(library);

                try {
                    base.evaluate();
                } finally {
                    tellstick.stop();
                }
            }
        };
    }

    public TellstickCoreMockLibrary getLibrary() {
        return library;
    }

    public Tellstick getTellstick() {
        return tellstick;
    }
}
