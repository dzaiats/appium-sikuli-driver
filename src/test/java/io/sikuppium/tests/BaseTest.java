package io.sikuppium.tests;


import io.sikuppium.driver.CapabilitiesFactory;
import io.sikuppium.driver.ImageElement;
import io.sikuppium.driver.SikuppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;

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
        driver.setSimilarityScore(0.95);
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
        sleep(2000);
        String prefix = String.valueOf(driver.getSize().getWidth()) + "x_";
        resourceName = prefix.replace(".0", "") + resourceName + ".png";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        System.out.println(resource);
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

        assertTrue(image != null, String.format("Cannot find image %s in %s seconds",
                resourceName, String.valueOf(secondsToWait)));

        return image;
    }
}
