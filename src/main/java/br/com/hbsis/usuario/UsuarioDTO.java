package br.com.hbsis.usuario;

/**
 * Classe para tráfego das informações do usuário
 */
public class UsuarioDTO {
	private Long id;
	private String senha;
	private String login;
	private String uuid;

	public UsuarioDTO() {
	}


	public UsuarioDTO(String senha, String login) {
		this.senha = senha;
		this.login = login;
	}

	public UsuarioDTO(Long id, String senha, String login, String uuid) {
		this.id = id;
		this.senha = senha;
		this.login = login;
		this.uuid = uuid;
	}

	public static UsuarioDTO of(Usuario usuario) {
		return new UsuarioDTO(
 				usuario.getId(),
				usuario.getSenha(),
				usuario.getLogin(),
				usuario.getUuid()
		);
	}

	public Long getId() {
		return id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "UsuarioDTO{" +
				"id=" + id +
				", senha='" + senha + '\'' +
				", login='" + login + '\'' +
				", uuid='" + uuid + '\'' +
				'}';
	}
}
