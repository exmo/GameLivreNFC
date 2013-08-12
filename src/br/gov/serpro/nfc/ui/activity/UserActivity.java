package br.gov.serpro.nfc.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.gov.serpro.nfc.R;
import br.gov.serpro.nfc.model.User;
import br.gov.serpro.nfc.ui.async.FindUserByEmailTask;
import br.gov.serpro.nfc.ui.async.RegisterUserTask;
import br.gov.serpro.nfc.util.NfcUtils;
import br.gov.serpro.nfc.util.Strings;

/**
 * Atividade responsável em apresentar o formulário de registr de um usuário.
 * 
 * @author Marlon Silva Carvalho
 */
public class UserActivity extends Activity {
	private IntentFilter[] mWriteTagFilters;
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;

	private TextView info;
	private EditText name;
	private EditText email;
	private EditText phone;

	private MenuItem menuItemRegister;
	private MenuItem menuItemCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_user);
		prepareNFC();
		getFields();
		configureActionBar();
	}

	private void configureActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user, menu);

		menuItemCancel = menu.findItem(R.id.action_cancel);
		menuItemRegister = menu.findItem(R.id.action_register);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		if (item.getItemId() == R.id.action_register) {
			registerUser();
		} else if (item.getItemId() == R.id.action_cancel) {
			cancelRegister();
		} else if (item.getItemId() == android.R.id.home) {
			goBack();
		}

		return result;
	}

	/**
	 * Sair desta tela.
	 */
	private void goBack() {
		disableTagWriteMode();
		finish();
	}

	/**
	 * Cancelar o registro do usuário.
	 */
	private void cancelRegister() {
		menuItemCancel.setVisible(false);
		menuItemRegister.setVisible(true);
		info.setText(R.string.text_info_add_user);
		enableFields();
	}

	/**
	 * Registrar um usuário no serviço e gravar a tag NFC.
	 */
	private void registerUser() {

		new FindUserByEmailTask(this) {

			protected void onPostExecute(User user) {
				super.onPostExecute(user);

				if (state == FAIL) {
					showToast(R.string.add_user_server_fail);
				} else {
					if (user == null) {
						if (!Strings.isEmpty(getName()) && !Strings.isEmpty(getPhone())) {
							user = getUserFromFields();

							new RegisterUserTask(UserActivity.this) {

								protected void onPostExecute(User user) {
									super.onPostExecute(user);
									enableNFC(user);
								}

							}.execute(user);

						} else {
							showToast(R.string.message_user_not_registered);
						}
					} else {
						enableNFC(user);
					}
				}
			}

		}.execute(getEmail());

	}

	/**
	 * Criar um usuário a partir dos dados dos campos.
	 * 
	 * @return Usuário criado.
	 */
	private User getUserFromFields() {
		User user = new User();

		user.email = getEmail();
		user.name = getName();
		user.phone = getPhone();

		return user;
	}

	/**
	 * Habilitar a gravação de tags e apresentar na tela os dados do usuário.
	 * 
	 * @param user
	 *            Usuário.
	 */
	private void enableNFC(User user) {
		setName(user.name);
		setPhone(user.phone);

		disableFields();

		info.setText(R.string.text_info_add_user_nfc);

		menuItemCancel.setVisible(true);
		menuItemRegister.setVisible(false);

		enableTagWriteMode();
	}

	/**
	 * Desabilitar todos os campos da tela.
	 */
	private void disableFields() {
		email.setEnabled(false);
		name.setEnabled(false);
		phone.setEnabled(false);
	}

	/**
	 * Habilitar todos os campos da tela.
	 */
	private void enableFields() {
		email.setEnabled(true);
		name.setEnabled(true);
		phone.setEnabled(true);
	}

	/**
	 * Exibir uma mensagem em Toast.
	 * 
	 * @param id
	 *            Identificador do recurso que contém a string.
	 */
	private void showToast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
	}

	/**
	 * Obter os campos da tela.
	 */
	private void getFields() {
		info = (TextView) findViewById(R.id.txtview_info_add_user);
		name = (EditText) findViewById(R.id.edit_user_name);
		email = (EditText) findViewById(R.id.edit_user_email);
		phone = (EditText) findViewById(R.id.edit_user_phone);
	}

	/**
	 * Preparar o NFC para ser usado nesta tela.
	 */
	private void prepareNFC() {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	/**
	 * Desabilitar a opção de escrita de tags. Quando uma tag se aproximar, não
	 * acontecerá algo.
	 */
	private void disableTagWriteMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			writeTag(intent);
		}
	}

	private void writeTag(Intent intent) {
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		if (NfcUtils.writeTag(NfcUtils.getAsNdef(getEmail() + "|" + getName() + "|" + getPhone()), detectedTag)) {
			Toast.makeText(this, R.string.nfc_write_success, Toast.LENGTH_LONG).show();
			cancelRegister();
			clearFields();
		} else {
			Toast.makeText(this, R.string.nfc_write_fail, Toast.LENGTH_LONG).show();
		}

	}

	private void clearFields() {
		email.setText("");
		name.setText("");
		phone.setText("");
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableTagWriteMode();
	}

	/**
	 * Habilitar a escrita de tags para detectar tags próximas.
	 */
	private void enableTagWriteMode() {
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	}

	public String getEmail() {
		return email.getText().toString();
	}

	public String getName() {
		return name.getText().toString();
	}

	public String getPhone() {
		return phone.getText().toString();
	}

	public void setEmail(String email) {
		this.email.setText(email);
	}

	public void setPhone(String phone) {
		this.phone.setText(phone);
	}

	public void setName(String name) {
		this.name.setText(name);
	}

}
