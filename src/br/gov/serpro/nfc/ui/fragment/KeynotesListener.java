package br.gov.serpro.nfc.ui.fragment;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public interface KeynotesListener {

	void keynoteSelected(Long id);
	PullToRefreshAttacher getPullToRefreshAttacher();
	
}
