package com.serenitydojo.playwright.cucumber.stepdefination;

import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Paths;
import java.time.LocalDateTime;

public class ScenarioTracingFixtures {

    LocalDateTime now = LocalDateTime.now();

    @Before
    public void setupTracing() {
        PlaywrightCucumberFixtures.getBrowserContext().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @After
    public void recordTraces(Scenario scenario) {
        String traceName = scenario.getName().replace(" ","-").toLowerCase();
        PlaywrightCucumberFixtures.getBrowserContext().tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/Trace/trace-" + traceName +now+ ".zip"))

        );

    }
}
