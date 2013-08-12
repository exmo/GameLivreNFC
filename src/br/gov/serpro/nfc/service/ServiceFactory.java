package br.gov.serpro.nfc.service;

/**
 * Fábrica que dá acesso aos serviços.
 * 
 * @author Marlon Silva Carvalho
 */
public class ServiceFactory {
	private static UserService userService = null;
	private static KeynoteService keynoteService = null;

	public static final UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
		}

		return userService;
	}

	public static final KeynoteService getKeynoteService() {
		if (keynoteService == null) {
			keynoteService = new KeynoteService();
		}

		return keynoteService;
	}

}
