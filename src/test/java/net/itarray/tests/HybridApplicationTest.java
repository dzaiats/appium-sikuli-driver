package net.itarray.tests;

import net.itarray.driver.SikuppiumAndroidDriver;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class HybridApplicationTest extends BaseTest{

    private static final String MYREACTIONS_ID = "com.denyszaiats.myreactions:id/";
    private DesiredCapabilities capabilities;

    @Test
    public void testSampleScenarioOfClicking() throws InterruptedException, MalformedURLException {
        capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("app", new File("src/test/resources/MyReaction.apk").getAbsolutePath());

        driver = new SikuppiumAndroidDriver(
                new URL("http://0.0.0.0:4723/wd/hub"),
                capabilities
        );
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.setDriver(driver);
        driver.setSimilarityScore(0.5);
        driver.setWaitSecondsAfterClick(2);
        driver.setWaitSecondsForImage(10);

        driver.findElementById(MYREACTIONS_ID + "imageLangEn").click();
        driver.findElementById(MYREACTIONS_ID + "skip_login_button").click();
        driver.findElementById(MYREACTIONS_ID + "buttonGuideOk").click();
        driver.findElementById("android:id/home").click();
        driver.findElementById("android:id/home").click();
        driver.findElementByXPath("//*[@text='Crazy Fingers']").click();
        driver.findElementById(MYREACTIONS_ID + "buttonGuideOk").click();
        driver.findElementById(MYREACTIONS_ID + "handButton").click();
        waitForImageElement("left_hand", 60).tap();
        driver.findElementById(MYREACTIONS_ID + "fingerButton").click();
        waitForImageElement("index_finger", 60).tap();
        driver.findElementById(MYREACTIONS_ID + "startButton").click();
        for (int i = 0; i < 3; i ++) {
            driver.findElementById(MYREACTIONS_ID + "imageTapButton").click();
        }

        String result = driver.findElementById(MYREACTIONS_ID + "resultsFasterClicker").getText();
        assertEquals(result, "3");
    }
}
