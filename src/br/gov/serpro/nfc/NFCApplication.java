package br.gov.serpro.nfc;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class NFCApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

}
