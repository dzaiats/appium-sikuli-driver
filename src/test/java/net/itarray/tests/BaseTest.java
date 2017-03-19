package net.itarray.tests;


import net.itarray.driver.CapabilitiesFactory;
import net.itarray.driver.ImageElement;
import net.itarray.driver.SikuppiumDriver;
import org.junit.After;
import org.junit.Before;

import java.net.URL;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertTrue;

public class BaseTest {

    protected SikuppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        driver = new SikuppiumDriver(
                new URL("http://0.0.0.0:4723/wd/hub"),
                CapabilitiesFactory.getCapabilities()
        );
        //driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.setDriver(driver);
        driver.setSimilarityScore(0.1);
        driver.setWaitSecondsAfterClick(2);
        driver.setWaitSecondsForImage(10);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected ImageElement waitForImageElement(String resourceName, int secondsToWait) throws InterruptedException {
        sleep(2000);
        resourceName = resourceName + ".png";
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

        assertTrue(String.format("Cannot find image %s in %s seconds",
                resourceName, String.valueOf(secondsToWait)), image != null);

        return image;
    }
}
