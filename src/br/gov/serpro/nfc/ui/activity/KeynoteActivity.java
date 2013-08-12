package br.gov.serpro.nfc.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.gov.serpro.nfc.R;
import br.gov.serpro.nfc.model.Keynote;
import br.gov.serpro.nfc.model.User;
import br.gov.serpro.nfc.ui.async.CheckinTask;
import br.gov.serpro.nfc.ui.async.CheckoutTask;
import br.gov.serpro.nfc.util.NfcUtils;

/**
 * Tela de apresentação de uma palestra.
 * 
 * @author Marlon Silva Carvalho
 */
public class KeynoteActivity extends Activity {
	public static final int STATE_CHECKIN = 1;
	public static final int STATE_CHECKOUT = 2;
	public static final String KEYNOTE_ID = "keynote_id";

	private int state = STATE_CHECKIN;

	private Keynote keynote;

	private TextView txtKeynoteDescription;
	private TextView txtKeynoteDate;
	private TextView txtKeynoteSpeaker;
	private Switch switchState;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private IntentFilter[] mNdefExchangeFilters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_keynote);
		getFields();
		prepareNFC();
		getKeynoteFromExtras();
		fillFields();
		configureActionBar();
	}

	private void configureActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return true;
	}

	/**
	 * Preencher os campos com os dados do keynote.
	 */
	private void fillFields() {
		if (keynote != null) {
			txtKeynoteDescription.setText(keynote.description);
			txtKeynoteDate.setText(keynote.date.toString());
			txtKeynoteSpeaker.setText(keynote.speaker);
		}
	}

	/**
	 * Obter o Keynote a partir do ID passado como parâmetro nos extras.
	 */
	private void getKeynoteFromExtras() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String id = (String) extras.get(KEYNOTE_ID);
			if (id != null) {
				keynote = Keynote.byId(Long.valueOf(id));
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = NfcUtils.getNdefMessages(intent);
			notifyState(msgs[0]);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		enableNdefExchangeMode();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableNdefExchangeMode();
	}

	private void notifyState(NdefMessage ndefMessage) {
		byte[] payload = ndefMessage.getRecords()[0].getPayload();
		String data = new String(payload);
		User user = new User(data);

		if (state == STATE_CHECKIN) {
			new CheckinTask(user, keynote) {

				protected void onPreExecute() {
					setProgressBarIndeterminateVisibility(true);
				}

				protected void onPostExecute(Void result) {
					setProgressBarIndeterminateVisibility(false);
					Toast.makeText(KeynoteActivity.this, "Checkin realizado para o participante " + user.name, Toast.LENGTH_LONG).show();
				}

			}.execute();

		} else if (state == STATE_CHECKOUT) {
			new CheckoutTask(user, keynote) {

				protected void onPreExecute() {
					setProgressBarIndeterminateVisibility(true);
				}

				protected void onPostExecute(Void result) {
					setProgressBarIndeterminateVisibility(false);
					Toast.makeText(KeynoteActivity.this, "Checkout realizado para o participante " + user.name, Toast.LENGTH_LONG).show();
				}

			}.execute();

		}
	}

	private void prepareNFC() {
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			ndefDetected.addDataType("application/vnd.serpro.nfcevents");
		} catch (MalformedMimeTypeException e) {
		}
		mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	private void disableNdefExchangeMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}

	private void enableNdefExchangeMode() {
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
	}

	private void getFields() {
		switchState = (Switch) findViewById(R.id.switchState);
		txtKeynoteDescription = (TextView) findViewById(R.id.keynote_description);
		txtKeynoteDate = (TextView) findViewById(R.id.keynote_date);
		txtKeynoteSpeaker = (TextView) findViewById(R.id.keynote_speaker);

		switchState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				state = switchState.isChecked() ? STATE_CHECKIN : STATE_CHECKOUT;
			}

		});

	}

}
