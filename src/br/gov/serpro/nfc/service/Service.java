package br.gov.serpro.nfc.service;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.serpro.nfc.util.HttpUtil;

abstract public class Service {
	public static final String BASE_URL = "http://fislgames-exmo.rhcloud.com/";
	private Status status;

	public Status getStatus() {
		return status;
	}

	public JSONObject getResponse(String url) {
		String jsonResponse = HttpUtil.performGet(url);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonResponse);

			String status = "";
			if (jsonObject.has("status")) {
				status = jsonObject.getString("status");
			} else if (jsonObject.has("codReg")) {
				status = jsonObject.getString("codRet");
			}

			if ("OK".equals(status)) {
				this.status = Status.OK;
			} else {
				this.status = Status.ERRO;
			}
		} catch (JSONException e) {
			throw new RuntimeException("Ocorreu um erro no acesso ao serviço remoto.");
		}

		return jsonObject;
	}

}
