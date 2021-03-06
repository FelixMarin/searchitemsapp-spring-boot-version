package com.searchitemsapp.business.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.searchitemsapp.business.Patterns;
import com.searchitemsapp.business.Products;
import com.searchitemsapp.business.SelectorsCss;
import com.searchitemsapp.company.factory.CompaniesGroup;
import com.searchitemsapp.dto.ProductDto;
import com.searchitemsapp.dto.UrlDto;
import com.searchitemsapp.resource.Constants;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor
public class ProductsImpl implements Products {
	
	private SelectorsCss cssSelectors;
	private CompaniesGroup companiesGroup;
	
	@Override
	public Optional<ProductDto> checkProduct(String requestProducName, Long companyId, 
			ProductDto productDto, Patterns elementPatterns) throws IOException {
			 
		Optional<String> productName = Optional.ofNullable(productDto.getNomProducto());
		String[] productCharArray = removeTildes(requestProducName).split(StringUtils.SPACE);
		var patternProduct = elementPatterns.createPatternProduct(productCharArray);
		var productResult = removeTildes(productName.orElseThrow());
		
		return patternProduct.matcher(productResult.toUpperCase()).find()?
				Optional.of(productDto):Optional.empty();
	}
	
	@Override
	public String removeTildes(final String accentedWord) {
		
		if(accentedWord.indexOf('\u00f1') == -1) {
			
			var wordWithoutTildes = accentedWord.replace(Constants.ENIE_MAY.getValue(), Constants.ENIE_U_HEX.getValue());
			wordWithoutTildes = java.text.Normalizer.normalize(wordWithoutTildes.toLowerCase(), Normalizer.Form.NFD);
			wordWithoutTildes = wordWithoutTildes.replaceAll("[\\p{InCombiningDiacriticalMarks}]", StringUtils.EMPTY);
			wordWithoutTildes = wordWithoutTildes.replace(Constants.ENIE_U_HEX.getValue(),  Constants.ENIE_MIN.getValue());
			return Normalizer.normalize(wordWithoutTildes, Normalizer.Form.NFC);
			
		}
		
		return accentedWord;
	}
	
	@Override
	public ProductDto addElementsToProducts(@NonNull Element elem,
			@NonNull UrlDto urlDto, 
			@NonNull String ordenacion) throws IOException {
		
		var image = StringUtils.isNotBlank(urlDto.getSelectores().getSelImage())?
				cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelImage(), urlDto):
				StringUtils.EMPTY;
		var image2 = StringUtils.isNotBlank(urlDto.getSelectores().getSelImage2())?
				cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelImage2(), urlDto):
					StringUtils.EMPTY;

		var productDto = ProductDto.builder()
				.imagen(StringUtils.isNotBlank(image)?image:image2)
				.nomProducto(cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelProducto(), urlDto))
				.desProducto(cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelProducto(), urlDto))
				.precio(cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelPrecio(), urlDto))
				.precioKilo(cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelPreKilo(), urlDto))
				.nomUrl(cssSelectors.elementByCssSelector(elem, urlDto.getSelectores().getSelLinkProd(), urlDto))
				.didEmpresa(urlDto.getDidEmpresa())
				.nomEmpresa(urlDto.getNomEmpresa())
				.ordenacion(Integer.parseInt(ordenacion))
				.build();
		
		productDto.setImagen(productDto.getImagen().replace("40x40", Constants.IMAGE_SIZE.getValue()));
		productDto.setImagen(productDto.getImagen().replace("325x325", Constants.IMAGE_SIZE.getValue()));
		
		var company = companiesGroup.getInstance(urlDto.getDidEmpresa());
		var namesOfUrlsOfAllProducts = company.getAllUrlsToSearch(productDto);
		productDto.setNomUrlAllProducts(namesOfUrlsOfAllProducts);
			
		return productDto;
	}
	
	@Override
	public String manageProductName(final String productName) throws IOException {
		var matcher = Pattern.compile(Constants.REGEX_DOLAR_PERCENT.getValue()).matcher(productName);
		return matcher.find()?productName:URLEncoder.encode(productName, StandardCharsets.UTF_8.toString());
	}
}
