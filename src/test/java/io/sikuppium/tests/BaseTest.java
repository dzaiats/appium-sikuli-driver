package io.sikuppium.tests;


import io.sikuppium.driver.CapabilitiesFactory;
import io.sikuppium.driver.ImageElement;
import io.sikuppium.driver.SikuppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class BaseTest {

    protected SikuppiumDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new SikuppiumDriver(
                new URL("http://0.0.0.0:4723/wd/hub"),
                CapabilitiesFactory.getCapabilities()
        );
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.setDriver(driver);
        driver.setSimilarityScore(0.2);
        driver.setWaitSecondsAfterClick(2);
        driver.setWaitSecondsForImage(10);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected ImageElement waitForImageElement(String resourceName, int secondsToWait) throws InterruptedException {
        sleep(3000);
        java.net.URL resource = this.getClass().getClassLoader().getResource("fhd_" + resourceName + ".png");
        ImageElement image = driver.findImageElement(resource);
        int attempts = 0;
        while (image == null && attempts < secondsToWait / 10) {
            sleep(10000);
            image = driver.findImageElement(resource);
            attempts++;
            if (image != null) {
                break;
            }
        }

        return image;
    }
}
