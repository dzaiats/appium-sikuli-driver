package io.sikuppium.driver;

import io.appium.java_client.AppiumDriver;

import static java.lang.Thread.sleep;

public class CustomImageElement implements ImageElement {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int waitAfterClick;
    private AppiumDriver driver;

    public CustomImageElement(AppiumDriver driver, int x, int y, int width, int height, int waitAfterClick) {
        this.driver = driver;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.waitAfterClick = waitAfterClick;
    }

    @Override
    public ImageElement tap() {
        driver.tap(1, x + width / 2, y + height / 2, 100);
        try {
            waitAfterClick();
        } catch (InterruptedException e) {
        }

        return this;
    }

    private void waitAfterClick() throws InterruptedException {
        sleep(1000 * waitAfterClick);
    }
}
