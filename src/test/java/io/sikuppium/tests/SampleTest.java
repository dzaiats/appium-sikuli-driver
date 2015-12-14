package io.sikuppium.tests;

import io.sikuppium.driver.ImageElement;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SampleTest extends BaseTest{

    @Test
    public void testSampleScenarioOfClicking() throws InterruptedException {
        driver.findElementById("com.denyszaiats.myreactions:id/imageLangEn").click();
        driver.findElementById("com.denyszaiats.myreactions:id/skip_login_button").click();
        driver.findElementById("com.denyszaiats.myreactions:id/buttonGuideOk").click();
        driver.findElementById("android:id/home").click();
        driver.findElementById("android:id/home").click();
        driver.findElementByName("Crazy Fingers").click();
        driver.findElementById("com.denyszaiats.myreactions:id/buttonGuideOk").click();
        driver.findElementById("com.denyszaiats.myreactions:id/handButton").click();
        ImageElement hand = waitForImageElement("left_hand", 60);
        hand.tap();
        driver.findElementById("com.denyszaiats.myreactions:id/fingerButton").click();
        ImageElement finger = waitForImageElement("index_finger", 60);
        finger.tap();
        driver.findElementById("com.denyszaiats.myreactions:id/startButton").click();
        for (int i = 0; i < 3; i ++) {
            driver.findElementById("com.denyszaiats.myreactions:id/imageTapButton").click();
        }

        String result = driver.findElementById("com.denyszaiats.myreactions:id/resultsFasterClicker").getText();
        assertEquals(result, "3");
    }
}
