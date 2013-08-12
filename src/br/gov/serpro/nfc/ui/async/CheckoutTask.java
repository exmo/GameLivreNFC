package br.gov.serpro.nfc.ui.async;

import android.os.AsyncTask;
import br.gov.serpro.nfc.model.Keynote;
import br.gov.serpro.nfc.model.User;

public class CheckoutTask extends AsyncTask<Void, Void, Void> {
	protected User user;
	protected Keynote keynote;

	public CheckoutTask(User user, Keynote keynote) {
		this.user = user;
		this.keynote = keynote;
	}

	@Override
	protected Void doInBackground(Void... params) {
		keynote.checkout(user);
		return null;
	}

}
