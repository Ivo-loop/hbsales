package br.com.hbsis.usuario;

import javax.persistence.*;

/**
 * Classe responsável pelo mapeamento da entidade do banco de dados
 */
@Entity
@Table(name = "seg_usuarios")
class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "login", unique = true, nullable = false, length = 100)
	private String login;
	@Column(name = "senha", nullable = false, length = 255)
	private String senha;
	@Column(name = "uuid", unique = true, updatable = false, length = 36)
	private String uuid;

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"id=" + id +
				", login='" + login + '\'' +
				", senha='" + senha + '\'' +
				", uuid='" + uuid + '\'' +
				'}';
	}
}
