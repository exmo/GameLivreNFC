package br.gov.serpro.nfc.ui.activity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import br.gov.serpro.nfc.R;
import br.gov.serpro.nfc.ui.fragment.AllKeynotesFragment;
import br.gov.serpro.nfc.ui.fragment.KeynotesListener;
import br.gov.serpro.nfc.ui.fragment.NowKeynotesFragment;

public class MainActivity extends Activity implements KeynotesListener {
	private PullToRefreshAttacher pullToRefreshAttacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		createTabs();
		pullToRefreshAttacher = PullToRefreshAttacher.get(this);
	}

	private void createTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = actionBar.newTab().setText("Agora").setTabListener(new TabListener<NowKeynotesFragment>(this, "now", NowKeynotesFragment.class));
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText("Todas").setTabListener(new TabListener<AllKeynotesFragment>(this, "all", AllKeynotesFragment.class));
		actionBar.addTab(tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			startActivity(new Intent(this, UserActivity.class));
		}

		return true;
	}

	@Override
	public void keynoteSelected(Long id) {
		Intent intent = new Intent(this, KeynoteActivity.class);
		intent.putExtra(KeynoteActivity.KEYNOTE_ID, id.toString());
		startActivity(intent);
	}

	@Override
	public PullToRefreshAttacher getPullToRefreshAttacher() {
		return pullToRefreshAttacher;
	}

}
