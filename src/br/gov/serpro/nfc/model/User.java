package br.gov.serpro.nfc.model;

import br.gov.serpro.nfc.service.ServiceFactory;

public class User {
	public String email;
	public String name;
	public String phone;

	public User() {
	}

	public User(String data) {
		String[] d = data.split("\\|");
		email = d[0];
		name = d[1];
		phone = d[2];
	}

	/**
	 * Obter um usuário pelo email.
	 * 
	 * @param email Email do usuário.
	 * @return Usuário encontrado.
	 */
	public static User findByEmail(final String email) {
		return ServiceFactory.getUserService().findByEmail(email);
	}

	/**
	 * Registrar o usuário.
	 */
	public void register() {
		ServiceFactory.getUserService().register(this);
	}

}
