package br.gov.serpro.nfc.ui.async;

import android.app.Activity;
import android.os.AsyncTask;
import br.gov.serpro.nfc.model.User;

/**
 * Registrar um usuário no servidor externo.
 * 
 * @author Marlon Silva Carvalho
 */
public class RegisterUserTask extends AsyncTask<User, Void, User> {
	private Activity parent;

	public RegisterUserTask(Activity parent) {
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
	protected User doInBackground(User... params) {
		User user = params[0];
		user.register();
		return user;
	}

}
