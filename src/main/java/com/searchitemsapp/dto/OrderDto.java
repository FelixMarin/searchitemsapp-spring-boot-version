package com.searchitemsapp.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@Builder
@AllArgsConstructor
@Component
public class OrderDto {
	
	private String categoria;
	private String producto;

}
