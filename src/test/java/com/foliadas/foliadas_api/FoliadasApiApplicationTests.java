package com.foliadas.foliadas_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.foliadas.foliadas_api.DTO.UsuarioDTO;
import com.foliadas.foliadas_api.Model.Usuario;
import com.foliadas.foliadas_api.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;

	@Test
	void loginCorrecto() {
		try {
			Usuario u = usuarioService.login("test@gmail.com", "1234");

			assertNotNull(u);
			assertEquals("test@gmail.com", u.getEmail());

			System.out.println("✔ PASSED: loginCorrecto");

		} catch (AssertionError e) {
			System.out.println("✖ FAILED: loginCorrecto");
			throw e;
		}
	}

	@Test
	void loginIncorrecto() {
		try {
			Usuario u = usuarioService.login("noexiste@gmail.com", "1234");

			assertNull(u);

			System.out.println("✔ PASSED: loginIncorrecto");

		} catch (AssertionError e) {
			System.out.println("✖ FAILED: loginIncorrecto");
			throw e;
		}
	}

	@Test
	void crearUsuario() {
		try {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setNombre("test");
			dto.setEmail("nuevo@test.com");
			dto.setPassword("1234");

			UsuarioDTO creado = usuarioService.create(dto);

			assertNotNull(creado);
			assertEquals("nuevo@test.com", creado.getEmail());

			System.out.println("✔ PASSED: crearUsuario");

		} catch (AssertionError e) {
			System.out.println("✖ FAILED: crearUsuario");
			throw e;
		}
	}
}