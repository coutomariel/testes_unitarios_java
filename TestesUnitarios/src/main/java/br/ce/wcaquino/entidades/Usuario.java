package br.ce.wcaquino.entidades;

import java.util.Objects;

public class Usuario {

	private String nome;
	private String email;
	
	public Usuario() {}
	
	public Usuario(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return Objects.equals(nome, usuario.nome) && Objects.equals(email, usuario.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, email);
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"nome='" + nome + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}