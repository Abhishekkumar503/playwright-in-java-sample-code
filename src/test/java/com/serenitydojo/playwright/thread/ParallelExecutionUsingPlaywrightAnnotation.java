package com.serenitydojo.playwright.thread;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.browserSetup.Base;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@UsePlaywright(Base.class)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelExecutionUsingPlaywrightAnnotation {

    @RepeatedTest(4)
    void shouldShowThePageTitle(Page page) {

        System.out.println("Run "  + " | Thread: " + Thread.currentThread().getName());

        page.navigate("https://www.practicesoftwaretesting.com/");
        Assertions.assertEquals("Practice Software Testing - Toolshop - v5.0", page.title());
    }
}
