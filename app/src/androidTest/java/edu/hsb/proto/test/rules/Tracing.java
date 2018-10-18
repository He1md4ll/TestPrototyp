package edu.hsb.proto.test.rules;

import android.os.Trace;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class Tracing extends ExternalResource {

    private String nameOfTest;

    @Override
    public Statement apply(Statement base, Description description) {
        nameOfTest = description.getMethodName();
        return super.apply(base, description);
    }

    @Override
    public void before() {
        Trace.beginSection(nameOfTest);
    }

    @Override
    public void after() {
        Trace.endSection();
    }
}
