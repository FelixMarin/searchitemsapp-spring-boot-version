package com.searchitemsapp.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.util.List;

import org.assertj.core.util.Lists;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.searchitemsapp.dto.CssSelectorsDto;
import com.searchitemsapp.dto.UrlDto;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CondisTest {
	
	@Autowired
	private Condis condis;

	@Test
	void testGetUrls() throws MalformedURLException {
		final var baseUri = "https://www.condisline.com/searching?term=miel&source=directSearch&sort=price&position=10&page=11&_=1557933255375";
		var document = Document.createShell(baseUri);
		var element = document.getAllElements().first();
		var cssSelectorsDto = CssSelectorsDto.builder().didEmpresa(110l).selPaginacion("a|href").build();
		var urlDto = UrlDto.builder().didEmpresa(110l)
			.selectores(cssSelectorsDto).nomUrl(baseUri).build();
		element.setBaseUri(baseUri);
		element.getElementsByTag("body")
			.append("<div><a class='link' href='test'>1 de 6</a></div>");
		List<String> res = condis.getUrls(document, urlDto);
		assertNotNull(res);
		assertEquals(1, res.size());
	}

	@Test
	void testGetId() {
		assertEquals(110l, condis.getId());
	}

	@Test
	void testReplaceCharacters() {
		assertEquals("ca%D1on", condis.replaceCharacters("cañón"));
		assertEquals("", condis.replaceCharacters(""));
	}

	@Test
	void testSelectorTextExtractor() {
		final var baseUri = "https://www.carrefour.es";
		var cssSelector = "a|href";

		var document = Document.createShell(baseUri);
		var element = document.getAllElements().first();
		element.setBaseUri(baseUri);
		element.getElementsByTag("body")
			.append("<div><a href='test'></a></div>");
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("href");
		var res = condis.selectorTextExtractor(document, list, cssSelector);
		assertNotNull(res);
		assertEquals("test", res);
		
		list = Lists.newArrayList();
		list.add("script");
		cssSelector = "div";
		element.getElementsByTag("body")
		.append("<div id='test'>" + "formatNumber('1,09', 'list_price_780231');\\n  formatNumber('1', 'sale_price_780231');" + "</div>");
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("1,09", res);
		
		list = Lists.newArrayList();
		list.add("script");
		cssSelector = "div";
		element.children().select("#test").remove();
		element.getElementsByTag("body")
		.append("<div id='test'>" + "formatNumber('1', 'sale_price_780231');" + "</div>");
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("1", res);
		
		list = Lists.newArrayList();
		list.add("script");
		cssSelector = "div";
		element.children().select("#test").remove();
		element.getElementsByTag("body")
		.append("<div id='test'>" + "formatNumber('1,', 'sale_price_780231');" + "</div>");
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("1,00", res);
		
		document = Document.createShell(baseUri);
		element = document.getAllElements().first();
		list = Lists.newArrayList();
		list.add("script");
		cssSelector = "div";
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("0,00", res);
		
		list = Lists.newArrayList();
		list.add("script");
		element.getElementsByTag("body").append("<div>10,00</div>");
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("10,00,00", res);
		
		document = Document.createShell(baseUri);
		element = document.getAllElements().first();
		list = Lists.newArrayList();
		list.add("a");
		cssSelector = "";
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("", res); 
		
		list = Lists.newArrayList();
		res = condis.selectorTextExtractor(element, list, cssSelector);
		assertEquals("", res);
	}

}
