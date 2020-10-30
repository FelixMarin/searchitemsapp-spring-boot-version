package com.searchitemsapp.company;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.searchitemsapp.dto.UrlDto;
import com.searchitemsapp.resource.Constants;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Dia implements Company {
	
	private Environment environment;

	@Override
	public List<String> getUrls(final Document document, final UrlDto urlDto) 
					throws MalformedURLException {
		
		String urlBase = urlDto.getNomUrl();
		List<String> liSelectorAtr = Lists.newArrayList();
		int numresultados = NumberUtils.toInt(environment.getProperty("flow.value.paginacion.url.dia"));
		String selectorPaginacion = urlDto.getSelectores().getSelPaginacion();	

		StringTokenizer st = new StringTokenizer(selectorPaginacion, Constants.PIPE.getValue());

		while (st.hasMoreTokens()) {
			liSelectorAtr.add(st.nextToken());
		}

		Elements elements = document.select(liSelectorAtr.get(0)); 
		List<String> listaUrls = Lists.newArrayList();

		listaUrls.add(urlBase);

		URL url = new URL(urlBase);
		String strUrlEmpresa = url.getProtocol().concat(Constants.PROTOCOL_ACCESSOR.getValue()).concat(url.getHost());

		for (Element element : elements) {
			listaUrls.add(strUrlEmpresa.concat(element.attr(liSelectorAtr.get(1))));
		}

		if(numresultados > 0 && numresultados <= listaUrls.size()) {
			listaUrls = listaUrls.subList(0, numresultados);
		}
		
		return listaUrls;
	}

	@Override
	public Long getId() {
		return NumberUtils.toLong(environment.getProperty("flow.value.did.empresa.dia"));
	}
}