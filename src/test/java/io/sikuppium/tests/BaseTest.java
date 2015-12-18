package io.sikuppium.tests;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.sikuppium.driver.CapabilitiesFactory;
import io.sikuppium.driver.ImageElement;
import io.sikuppium.driver.SikuppiumDriver;

public class BaseTest {

	protected SikuppiumDriver driver;
	AppiumDriverLocalService appiumDriverLocalService;

	@BeforeMethod
	public void setUp() throws Exception {
		AppiumServiceBuilder builder = new AppiumServiceBuilder()
				.withAppiumJS(new File(System.getenv("APPIUM_JS")))
				.withArgument(GeneralServerFlag.LOG_LEVEL, "info").usingAnyFreePort();
		appiumDriverLocalService = builder.build();
		appiumDriverLocalService.start();
		driver = new SikuppiumDriver(appiumDriverLocalService.getUrl(), CapabilitiesFactory.getCapabilities());
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

		assertTrue(image != null,
				String.format("Cannot find image %s in %s seconds", resourceName, String.valueOf(secondsToWait)));

		return image;
	}
}
