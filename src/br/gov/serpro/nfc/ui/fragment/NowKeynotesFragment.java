package br.gov.serpro.nfc.ui.fragment;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.gov.serpro.nfc.R;
import br.gov.serpro.nfc.model.Keynote;
import br.gov.serpro.nfc.ui.adapter.KeynotesAdapter;
import br.gov.serpro.nfc.ui.async.FindAllKeynotesTask;

public class NowKeynotesFragment extends Fragment {
	private ListView listKeynotes;
	private KeynotesListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof KeynotesListener) {
			listener = (KeynotesListener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_all_keynotes, null);

		listKeynotes = (ListView) view.findViewById(R.id.listKeynotes);
		listKeynotes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				listener.keynoteSelected(id);
			}

		});

		listener.getPullToRefreshAttacher().addRefreshableView(listKeynotes, new OnRefreshListener() {

			@Override
			public void onRefreshStarted(View view) {
				fillListView();
			}

		});

		fillListView();

		return view;
	}

	/**
	 * Preencher a lista de palestras.
	 */
	private void fillListView() {

		setKeynotes(Keynote.all());

		new FindAllKeynotesTask() {

			protected void onPreExecute() {
				listener.getPullToRefreshAttacher().setRefreshing(true);
			}

			protected void onPostExecute(java.util.List<Keynote> result) {
				setKeynotes(result);
				listener.getPullToRefreshAttacher().setRefreshing(false);
			}

		}.execute();

	}

	public void setKeynotes(List<Keynote> keynotes) {
		listKeynotes.setAdapter(new KeynotesAdapter(getActivity(), keynotes));
	}

}
