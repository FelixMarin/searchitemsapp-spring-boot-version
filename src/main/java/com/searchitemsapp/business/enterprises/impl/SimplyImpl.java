package com.searchitemsapp.business.enterprises.impl;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.searchitemsapp.business.enterprises.Company;
import com.searchitemsapp.dto.UrlDto;
import com.searchitemsapp.resources.Constants;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SimplyImpl implements Company {
	
	private static final String AMPERSAND_FIN_EQUALS_TEN = "&fin=10";
	private static final String EQUALS_ONE_AMPERSAND = "=1&";
	private static final String AMPERSAND_FIN_EQUALS = "&fin=";
	private Environment environment;

	@Override
	public List<String> getUrls(final Document document, final UrlDto urlDto) {
 
		String urlAux;
		int itemsToShow = 30;
		int maxNumberOfUrls = 10;
		int increment = 2;
		
		String urlBase = urlDto.getNomUrl();
		
		int numresultados = NumberUtils.toInt(environment.getProperty("flow.value.paginacion.url.simply"));
		
		List<String> listaUrls = Lists.newArrayList();
		listaUrls.add(urlBase);
		
		for (int i = 2; i <= maxNumberOfUrls; i++) {
			
			urlAux = urlBase.replace(EQUALS_ONE_AMPERSAND, Constants.EQUALS.getValue().concat(String.valueOf(i).concat(Constants.AMPERSAND.getValue())));
			
			if(i == 2) {
				urlAux = urlAux.replace(AMPERSAND_FIN_EQUALS_TEN, AMPERSAND_FIN_EQUALS + itemsToShow);
			} else {
				urlAux = urlAux.replace(AMPERSAND_FIN_EQUALS_TEN, AMPERSAND_FIN_EQUALS + itemsToShow*increment++);
			}
			
			listaUrls.add(urlAux);
		}
		
		if(numresultados > 0 && numresultados <= listaUrls.size()) {
			listaUrls = listaUrls.subList(0, numresultados);
		}
		
		return listaUrls;
	}

	@Override
	public String reemplazarCaracteres(final String producto) {
		return producto.replace(Constants.ENIE_MIN.getValue(), 
				Constants.ENIE_URL_F.getValue());
	}

	@Override
	public int get_DID() {
		return NumberUtils.toInt(environment.getProperty("flow.value.did.empresa.simply"));
	}
}
