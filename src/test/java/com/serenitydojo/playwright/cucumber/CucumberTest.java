package com.serenitydojo.playwright.cucumber;


import org.junit.platform.suite.api.*;
// This called as runner
public class CucumberTest {
    /**
     * *************************  QUICK SUMMARY  ********************************
     *
     * @Suite :
     * Marks this class as a test suite for JUnit 5.
     * Think of it as: “Hey JUnit, this is not a normal test class, it’s a collection of tests (or engines) to run.”
     *
     * @IncludeEngines("cucumber") :
     * Tells JUnit Platform: “Use the Cucumber engine to discover and run tests.”
     * This replaces @RunWith(Cucumber.class) from JUnit 4.
     * Only works with cucumber-junit-platform-engine dependency.
     *
     * @SelectClasspathResource("/feature") :
     * Specifies where your .feature files live in the classpath.
     * JUnit Platform will scan this folder for feature files.
     *
     * @ConfigurationParameter(...) :
     * Configures Cucumber plugins via JUnit 5.
     * io.qameta.allure.allure-cucumber-jvm → generates Allure reports
     * html:target/cucumber-reports/cucumber.html → generates HTML reports
     * You can add multiple plugins separated by commas.
     */
    @Suite
    @IncludeEngines("cucumber")
    @SelectClasspathResource("/feature")
    @ConfigurationParameter(
            key="cucumber.plugin",
            value =
                "pretty," +
                        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
                    "html:target/cucumber-reports/cucumber.html"
    )
    public class CucumberTests {
        /**
         * If you not declare parallel execution in junit-platform.properties, it will run one by one
         * otherwise it will parallel according to your max-pool-size.
         */
    }
}
