package net.itarray.driver;

import io.appium.java_client.MobileDriver;

import java.awt.*;
import java.net.URL;

/**
 * Created by ZayCo on 19/03/17.
 */
public interface SikuppiumDriver extends MobileDriver {

    ImageElement findImageElement(URL imageUrl);

    Dimension getSize();

    void setWaitSecondsAfterClick(int waitSecondsAfterClick);

    void setSimilarityScore(double similarityScore);

    void setWaitSecondsForImage(int waitSecondsForImage);

    void setDriver(SikuppiumDriver driver);
}
