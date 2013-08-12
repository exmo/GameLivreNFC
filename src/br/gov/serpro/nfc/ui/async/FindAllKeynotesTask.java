package br.gov.serpro.nfc.ui.async;

import java.util.List;

import android.os.AsyncTask;
import br.gov.serpro.nfc.model.Keynote;

/**
 * Obter a lista de todas as palestras.
 * 
 * @author Marlon Silva Carvalho
 */
public class FindAllKeynotesTask extends AsyncTask<Void, Void, List<Keynote>> {

	@Override
	protected List<Keynote> doInBackground(Void... params) {
		return Keynote.refresh();
	}

}
