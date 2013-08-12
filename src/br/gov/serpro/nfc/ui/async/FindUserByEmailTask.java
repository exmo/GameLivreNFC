package br.gov.serpro.nfc.ui.async;

import android.app.Activity;
import android.os.AsyncTask;
import br.gov.serpro.nfc.model.User;

/**
 * Tarefa assíncrona que busca o usuário na base de dados, que pode causar uma
 * ida para o servidor externo.
 * 
 * @author Marlon Silva Carvalho
 */
public class FindUserByEmailTask extends AsyncTask<String, Void, User> {
	public static final int FAIL = -1;
	public static final int SUCCESS = 1;

	private Activity parent;

	protected int state = FAIL;

	public FindUserByEmailTask(Activity parent) {
		this.parent = parent;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		parent.setProgressBarIndeterminateVisibility(true);
	}

	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		parent.setProgressBarIndeterminateVisibility(false);
	}

	@Override
	protected User doInBackground(String... params) {
		User user = null;

		try {
			user = User.findByEmail(params[0]);
			state = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

}
