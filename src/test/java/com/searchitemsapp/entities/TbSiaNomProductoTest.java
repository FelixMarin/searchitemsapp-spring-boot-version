package com.searchitemsapp.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TbSiaNomProductoTest {
	
	private static TbSiaNomProducto tbSiaNomProducto;
	
	@BeforeAll
	static void inicializar() {
		tbSiaNomProducto = new TbSiaNomProducto();
	}

	@Test
	void testGetDid() {
		tbSiaNomProducto.setDid(101l);
		assertEquals(101l,tbSiaNomProducto.getDid());
	}

	@Test
	void testGetNomProducto() {
		tbSiaNomProducto.setNomProducto("test");
		assertEquals("test",tbSiaNomProducto.getNomProducto());
	}

	@Test
	void testGetTbSiaPais() {
		var tbSiaPais = new TbSiaPais();
		tbSiaPais.setDid(101l);
		tbSiaNomProducto.setTbSiaPais(tbSiaPais);
		assertEquals(101l,tbSiaNomProducto.getTbSiaPais().getDid());
		
	}
	
	@Test 
	void tesToString() {
		assertNotNull(tbSiaNomProducto.toString());
		assertNotEquals("", tbSiaNomProducto.toString());
	}
	
	@Test
	void testHashCode() {
		assertNotEquals("", tbSiaNomProducto.hashCode());
	}
	
	@Test
	void testCanEquals() {
		var isEquals = tbSiaNomProducto.equals(new TbSiaNomProducto());
		assertFalse(isEquals);
	}

}
