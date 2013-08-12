package br.gov.serpro.nfc.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.gov.serpro.nfc.model.Keynote;
import br.gov.serpro.nfc.model.User;

/**
 * Acesso aos serviços de Keynotes.
 * 
 * @author Marlon Silva Carvalho
 */
public class KeynoteService extends Service {
	public static final String URL_KEYNOTES_LIST = BASE_URL + "palestras";
	public static final String URL_KEYNOTES_CHECKIN = BASE_URL + "palestra/:id/checkin/:email";
	public static final String URL_KEYNOTES_CHECKOUT = BASE_URL + "palestra/:id/checkin/:email";

	/**
	 * Obter do servidor todas as palestras disponíveis.
	 * 
	 * @param id
	 *            Identificador do Evento.
	 * @return Lista de Palestras.
	 */
	public List<Keynote> list(Long id) {
		List<Keynote> result = new ArrayList<Keynote>();

		JSONObject jsonResponse = getResponse(URL_KEYNOTES_LIST);
		if (getStatus().equals(Status.OK)) {
			try {
				JSONArray jsonKeynotes = jsonResponse.getJSONArray("palestras");
				for (int i = 0; i < jsonKeynotes.length(); i++) {
					JSONObject jsonKeynote = jsonKeynotes.getJSONObject(i);
					Keynote keynote = new Keynote();
					keynote.service_id = jsonKeynote.getLong("id");
					keynote.speaker = jsonKeynote.getString("palestrante");
					keynote.description = jsonKeynote.getString("descricao");
					keynote.date = jsonKeynote.getString("data");
					result.add(keynote);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Realizar o Checkin de um usuário em uma palestra.
	 * 
	 * @param user
	 *            Usuário que realizará o checkin.
	 */
	public void checkin(Keynote keynote, User user) {
		String url = URL_KEYNOTES_CHECKIN.replace(":id", keynote.getId().toString()).replace(":email", user.email);
		getResponse(url);
	}

	/**
	 * Realizar o Checkout de um usuário em uma palestra.
	 * 
	 * @param user
	 *            Usuário que realizará o checkout.
	 */
	public void checkout(Keynote keynote, User user) {
		String url = URL_KEYNOTES_CHECKIN.replace(":id", keynote.getId().toString()).replace(":email", user.email);
		getResponse(url);
	}

}