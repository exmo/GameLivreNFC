package br.gov.serpro.nfc.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.gov.serpro.nfc.model.User;
import br.gov.serpro.nfc.util.HttpUtil;

/**
 * Serviço de Registro de Usuários.
 * 
 * @author Marlon Silva Carvalho
 */
public class UserService extends Service {
	private static final String URL_REGISTER = BASE_URL + "usuario/criar/";
	private static final String URL_GET = BASE_URL + "usuario/consultar/";

	/**
	 * Cadastrar um usuário no aplicativo.
	 * 
	 * @param user
	 *            Usuário a ser cadastrado.
	 */
	public void register(final User user) {
		getResponse(URL_REGISTER + user.email + "/" + user.name + "/" + user.phone + "?callback");
		if (!getStatus().equals(Status.OK)) {
			throw new RuntimeException("Não foi possível cadastrar o usuário.");
		}
	}

	/**
	 * Obter um usuário pelo email.
	 * 
	 * @param email
	 *            Email do usuário.
	 * @return Usuário encontrado.
	 */
	public User findByEmail(final String email) {
		User result = null;

		String jsonResponse = HttpUtil.performGet(URL_GET + email + "?callback");
		jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 2);

		JSONObject jsonResult = null;
		try {
			jsonResult = new JSONObject(jsonResponse);
			result = new User();
			result.name = jsonResult.getString("nome");
			result.phone = jsonResult.getString("telefone");
			result.email = email;
		} catch (JSONException e1) {
			Log.e(getClass().getName(), e1.getMessage(), e1);
		}

		return result;
	}

}