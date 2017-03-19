package net.itarray.driver;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class CapabilitiesFactory {

    protected static DesiredCapabilities capabilities;

    public static DesiredCapabilities getCapabilities() throws Exception {

        capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("app", new File("src/test/resources/MyReaction.apk").getAbsolutePath());

        return capabilities;
    }

}
