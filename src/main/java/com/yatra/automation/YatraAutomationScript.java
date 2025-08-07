package com.yatra.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YatraAutomationScript {

	public static void main(String[] args) throws InterruptedException {

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--disable-notifications");

		WebDriver wd = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20));
		wd.get("https://www.yatra.com/");
		wd.manage().window().maximize();

		closePopUp(wait);
		clickOnDepatureDate(wait);

		WebElement currentMonthWebElement = selectMonthFromCalendar(wait, 0);// Current Month
		WebElement nextMonthWebElement = selectMonthFromCalendar(wait, 1);// Next Month

		Thread.sleep(2000);
		String lowestPriceForCurrentMonth = getMeLowestPrice(currentMonthWebElement);
		String lowestPriceForNextMonth = getMeLowestPrice(nextMonthWebElement);

		System.out.println(lowestPriceForCurrentMonth);
		System.out.println(lowestPriceForNextMonth);
		compareTwoMonthPrices(lowestPriceForCurrentMonth, lowestPriceForNextMonth);
	}

	public static void clickOnDepatureDate(WebDriverWait wait) {
		By departureDateButtonLocator = By
				.xpath("//div[@aria-label=\"Departure Date inputbox\"  and @role =\"button\"]");
		WebElement departureDateButton = wait
				.until(ExpectedConditions.elementToBeClickable(departureDateButtonLocator));
		departureDateButton.click();
	}

	public static void closePopUp(WebDriverWait wait) {
		By popupLocator = By.xpath("//div[contains(@class,\"style_popup\")][1]");
		try {
			WebElement popUpElement = wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
			WebElement crossButton = popUpElement.findElement(By.xpath("//img[@alt=\"cross\"]"));
			crossButton.click();
		} catch (TimeoutException e) {
			System.out.println("Pop up not shown on the screen!!!!");

		}
	}

	public static String getMeLowestPrice(WebElement monthWebElement) {
		By PriceLocator = By.xpath(".//span[contains(@Class,\"custom-day-content\")]");
		List<WebElement> augPriceList = monthWebElement.findElements(PriceLocator);

		int lowserPrice = Integer.MAX_VALUE;
		WebElement priceElement = null;

		for (WebElement price : augPriceList) {

			String priceString = price.getText();
			if (priceString.length() > 0) {
				priceString = priceString.replace("₹", "").replace(",", "");
				int priceInt = Integer.parseInt(priceString);
				if (priceInt < lowserPrice) {
					lowserPrice = priceInt;
					priceElement = price;
				}
			}
		}

		WebElement dateElement = priceElement.findElement(By.xpath(".//../.."));
		String result = dateElement.getAttribute("aria-label") + "----Price is Rs" + lowserPrice;
		return result;
	}

	public static WebElement selectMonthFromCalendar(WebDriverWait wait, int index) {
		By calendarMonthLocators = By.xpath("//div[@class=\"react-datepicker__month-container\"]");
		List<WebElement> calendarMonthsList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(calendarMonthLocators));
		WebElement monthCalendarWebElement = calendarMonthsList.get(index);
		return monthCalendarWebElement;
	}

	public static void compareTwoMonthPrices(String currentMonthPrice, String nextMonthPrice) {
		int currentMonthRSIndex = currentMonthPrice.indexOf("Rs");
		int nextMonthRSIndex = nextMonthPrice.indexOf("Rs");

		String currentPrice = currentMonthPrice.substring(currentMonthRSIndex + 2);
		String nextPrice = nextMonthPrice.substring(nextMonthRSIndex + 2);

		int current = Integer.parseInt(currentPrice);
		int next = Integer.parseInt(nextPrice);

		if (current < next) {
			System.out.println("The lowest price for the two month is " + current);
		} else {
			System.out.println("\"The lowest price for the two month is " + next);
		}

	}

}
