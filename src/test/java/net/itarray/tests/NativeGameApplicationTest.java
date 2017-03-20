package net.itarray.tests;

import net.itarray.driver.SikuppiumAndroidDriver;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class NativeGameApplicationTest extends BaseTest {

    private DesiredCapabilities capabilities;

    @Test
    public void testSampleScenarioOfClicking() throws InterruptedException, MalformedURLException {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("app", new File("src/test/resources/HillClimb.apk").getAbsolutePath());
        driver = new SikuppiumAndroidDriver(
                new URL("http://0.0.0.0:4723/wd/hub"),
                capabilities
        );
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.setDriver(driver);
        driver.setSimilarityScore(0.1);
        driver.setWaitSecondsAfterClick(2);
        driver.setWaitSecondsForImage(10);
        Thread.sleep(10000);

        waitForImageElement("shop", 20).tap();
        waitForImageElement("back", 20).tap();
        waitForImageElement("next", 20).tap();
        waitForImageElement("next", 20).tap();
        waitForImageElement("start", 20).tap();
        waitForImageElement("gas", 20).pressAndHold(5);
        waitForImageElement("break", 20).pressAndHold(2);
    }
}
