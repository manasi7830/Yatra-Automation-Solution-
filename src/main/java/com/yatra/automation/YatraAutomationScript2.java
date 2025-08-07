package com.yatra.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class YatraAutomationScript2 {

	public static void main(String[] args) {
		
		ChromeOptions chromeOptions= new ChromeOptions();
		chromeOptions.addArguments("--disable-notifications");
		
		WebDriver wd= new ChromeDriver(chromeOptions);
		wd.get("https://www.yatra.com/");
		wd.manage().window().maximize();
		
		

	}

}
