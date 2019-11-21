package br.com.hbsis.usuario;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioServiceTest {

	@Mock
	private IUsuarioRepository usuarioRepository;

	@Captor
	private ArgumentCaptor<Usuario> argumentCaptor;

	@InjectMocks
	private UsuarioService usuarioService;

	@Test
	public void save() {
		UsuarioDTO usuarioDTO = new UsuarioDTO("minha.senha", "meu.login");

		Usuario usuarioMock = Mockito.mock(Usuario.class);

		when(usuarioMock.getSenha()).thenReturn(usuarioDTO.getSenha());
		when(usuarioMock.getLogin()).thenReturn(usuarioDTO.getLogin());

		when(this.usuarioRepository.save(any())).thenReturn(usuarioMock);

		this.usuarioService.save(usuarioDTO);

		verify(this.usuarioRepository, times(1)).save(this.argumentCaptor.capture());
		Usuario createdUsuario = argumentCaptor.getValue();

		assertTrue(StringUtils.isNoneEmpty(createdUsuario.getLogin()), "Login não deve ser nulo");
		assertTrue(StringUtils.isNoneEmpty(createdUsuario.getSenha()), "Login não deve ser nulo");
		assertTrue(StringUtils.isNoneEmpty(createdUsuario.getUuid()), "Login não deve ser nulo");

		assertEquals(36, createdUsuario.getUuid().length(), "UUID deve ter 36 posições");
	}

	@Test
	public void usuarioDTONull() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.usuarioService.save(null);
		});
	}

	@Test
	public void usuarioComSenhaNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			UsuarioDTO usuarioDTO = new UsuarioDTO(null, "meu_querido_login");
			this.usuarioService.save(usuarioDTO);
		});
	}

	@Test void usuarioComLoginVazio() {
		assertThrows(IllegalArgumentException.class, () -> {
			UsuarioDTO usuarioDTO = new UsuarioDTO("minha_senha_ponto_com_arroba", "");
			this.usuarioService.save(usuarioDTO);
		});
	}

}
