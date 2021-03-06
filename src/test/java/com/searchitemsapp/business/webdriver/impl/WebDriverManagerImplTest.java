package com.searchitemsapp.business.webdriver.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.searchitemsapp.business.webdriver.WebDriverManager;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class WebDriverManagerImplTest {
	
	@Autowired
	WebDriverManager webDriverManager;
	
	@BeforeEach
	void setUpInicial() {
		webDriverManager.setUp();
	}
	
	@AfterClass
	void closeDriver() {
		webDriverManager.closeDriver();
	}
	
	@Test
	void testGetDynamicHtmlContentSync() throws InterruptedException {
		final var urlBase = "https://tienda.consum.es/consum/es/search?q=miel#!Grid";
						
		var result = webDriverManager.getDynamicHtmlContentSync(
				webDriverManager.getWebDriver().get(),
				urlBase, Long.valueOf(116));
		
		assertNotNull(result);
	}
	
	@Test
	void testGetDynamicHtmlContentAsync() throws InterruptedException {
		final var urlBase = "https://www.hipercor.es/supermercado/buscar/1/?term=miel&type_ahead_tab=panel_all&sort=mostSell";
						
		var result = webDriverManager.getDynamicHtmlContentAsync(
				webDriverManager.getWebDriver().get(),
				urlBase, Long.valueOf(103));
		
		assertNotNull(result);
	}
	
	@Test
	void testIsPresent() {
		assertTrue(webDriverManager.isPresent());
	}
	
	@Test
	void testShutdownWebDriver() {		
		webDriverManager.shutdownWebDriver();
		
		assertThrows(NoSuchSessionException.class, () -> {
			webDriverManager.closeDriver();
		});
		
	}
	
	@Test
	void testCloseDriver() {
		webDriverManager.closeDriver();
		assertNotNull(webDriverManager.getWebDriver().get().getClass());
	}
	
	@Test
	 void testOpenWindow() {
		webDriverManager.openWindow(webDriverManager.getWebDriver().get());
		List<String> multipleTabs = Lists.newArrayList(webDriverManager.getWebDriver().get().getWindowHandles());
		assertEquals(2, multipleTabs.size());		
	}
}
