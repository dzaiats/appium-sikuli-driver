package io.sikuppium.driver;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.sikuli.api.Screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DriverScreen implements Screen {

    public AppiumDriver driver;
    final private Dimension size;

    public DriverScreen(AppiumDriver driver) throws IOException {
        this.driver = driver;
        File screenshotFile = this.driver.getScreenshotAs(OutputType.FILE);
        BufferedImage b = ImageIO.read(screenshotFile);
        size = new Dimension(b.getWidth(), b.getHeight());
    }

    @Override
    public BufferedImage getScreenshot(int x, int y, int width, int height) {
        File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage full = ImageIO.read(screenshotFile);
            BufferedImage cropped = crop(full, x, y, width, height);
            return cropped;
        } catch (IOException e) {
        }
        return null;
    }

    private BufferedImage crop(BufferedImage src, int x, int y, int width, int height) {
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, width, height, x, y, x + width, y + height, null);
        g.dispose();
        return dest;
    }

    @Override
    public Dimension getSize() {
        return size;
    }
}