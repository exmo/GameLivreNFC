package br.gov.serpro.nfc.ui.async;

import android.os.AsyncTask;
import br.gov.serpro.nfc.model.Keynote;
import br.gov.serpro.nfc.model.User;

public class CheckinTask extends AsyncTask<Void, Void, Void> {
	protected User user;
	protected Keynote keynote;

	public CheckinTask(User user, Keynote keynote) {
		this.user = user;
		this.keynote = keynote;
	}

	@Override
	protected Void doInBackground(Void... params) {
		keynote.checkin(user);
		return null;
	}

}
