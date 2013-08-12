package br.gov.serpro.nfc.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.gov.serpro.nfc.R;
import br.gov.serpro.nfc.model.Keynote;

public class KeynotesAdapter extends BaseAdapter {
	private List<Keynote> keynotes;
	private Context context;

	public KeynotesAdapter(Context context, List<Keynote> keynotes) {
		this.context = context;
		this.keynotes = keynotes;
		if (this.keynotes == null) {
			this.keynotes = new ArrayList<Keynote>();
		}
	}

	@Override
	public int getCount() {
		return keynotes.size();
	}

	@Override
	public Object getItem(int pos) {
		return keynotes.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		Keynote keynote = (Keynote) getItem(pos);
		return keynote.service_id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.adapter_keynotes, null);
		}

		Keynote keynote = (Keynote) getItem(pos);
		TextView txtDesc = (TextView) view.findViewById(R.id.adapter_keynotes_text_name);
		TextView txtQuantity = (TextView) view.findViewById(R.id.adapter_keynotes_text_quantity);
		TextView txtSpeaker = (TextView) view.findViewById(R.id.adapter_keynotes_text_speaker);
		TextView txtDate = (TextView) view.findViewById(R.id.adapter_keynotes_text_date);

		txtDesc.setText(keynote.description);
		txtQuantity.setText("Checkins: " + pos);
		txtSpeaker.setText("Com " + keynote.speaker);
		txtDate.setText(keynote.date.toString());
		
		return view;
	}

}