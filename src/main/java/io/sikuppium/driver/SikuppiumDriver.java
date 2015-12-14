package io.sikuppium.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.Capabilities;
import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class SikuppiumDriver extends AppiumDriver {

    private final static Logger LOG = LoggerFactory.getLogger(SikuppiumDriver.class);

    private int waitSecondsAfterClick = 2;
    private double similarityScore = 0.7;
    private int waitSecondsForImage = 10;
    private AppiumDriver driver;
    private DriverScreen driverScreen;

    public SikuppiumDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    public ImageElement findImageElement(URL imageUrl) {
        try {
            driverScreen = new DriverScreen(driver);
        } catch (IOException e1) {
            throw new RuntimeException("Unable to init SikkupiumDriver");
        }

        ScreenRegion webdriverRegion = new DefaultScreenRegion(driverScreen);
        webdriverRegion.setScore(similarityScore);

        ImageTarget target = new ImageTarget(imageUrl);
        ScreenRegion imageRegion = webdriverRegion.wait(target, waitSecondsForImage);

        Rectangle rectangle;

        if (imageRegion != null) {
            rectangle = imageRegion.getBounds();
            LOG.debug("Image is found at {} {} {} {}", rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            LOG.debug("Image is not found");
            return null;
        }

        return new CustomImageElement(
                this.driver,
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height,
                this.waitSecondsAfterClick
        );
    }

    public Dimension getSize() {
        try {
            driverScreen = new DriverScreen(driver);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return driverScreen.getSize();
    }

    public void setWaitSecondsAfterClick(int waitSecondsAfterClick) {
        this.waitSecondsAfterClick = waitSecondsAfterClick;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public void setWaitSecondsForImage(int waitSecondsForImage) {
        this.waitSecondsForImage = waitSecondsForImage;
    }

    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
    }

    @Override
    public MobileElement scrollTo(String s) {
        return null;
    }

    @Override
    public MobileElement scrollToExact(String s) {
        return null;
    }
}
