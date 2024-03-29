package com.e.bloctap2pay.nfc.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * Utils class used to manager NFC Adapter
 * 
 * @author MILLAU julien
 * 
 */
public class NFCUtils {

	/**
	 * Check if NFC is available on the device
	 * 
	 * @return true if the device has NFC available
	 */
	public static boolean isNfcAvailable(final Context pContext) {
		boolean nfcAvailable = true;
		try {
			NfcAdapter adapter = NfcAdapter.getDefaultAdapter(pContext);
			if (adapter == null) {
				nfcAvailable = false;
			}
		} catch (UnsupportedOperationException e) {
			nfcAvailable = false;
		}
		return nfcAvailable;
	}
	
	/**
	 * Check if NFC is enabled on the device
	 * 
	 * @return true if the device has NFC enabled
	 */
	public static boolean isNfcEnabled(final Context pContext) {
		boolean nfcEnabled = true;
		try {
			NfcAdapter adapter = NfcAdapter.getDefaultAdapter(pContext);
			nfcEnabled = adapter.isEnabled();
		} catch (UnsupportedOperationException e) {
			nfcEnabled = false;
		}
		return nfcEnabled;
	}

	public  void disAbleNfcReaderMode(final Context pContext){
		mNfcAdapter.disableReaderMode(mActivity);
	}

	/**
	 * NFC adapter
	 */
	private final NfcAdapter mNfcAdapter;
	/**
	 * Intent sent
	 */
	private final PendingIntent mPendingIntent;

	/**
	 * Parent Activity
	 */
	private final Activity mActivity;

	/**
	 * Inetnt filter
	 */
	private static final IntentFilter[] INTENT_FILTER = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };

	/**
	 * Tech List
	 */
	private static final String[][] TECH_LIST = new String[][] { { IsoDep.class.getName() } };

	/**
	 * Constructor of this class
	 * 
	 * @param pActivity
	 *            activity context
	 */


	public NFCUtils(final Activity pActivity) {
		mActivity = pActivity;
		mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);
	/*	mPendingIntent = PendingIntent.getActivity(mActivity, 0,
				new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


		PendingIntent pendingIntent = null;*/


		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
			mPendingIntent = PendingIntent.getActivity
					(mActivity, 0, new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
		}
		else
		{
			mPendingIntent = PendingIntent.getActivity
					(mActivity, 0, new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_ONE_SHOT);
		}

		// Set up foreground dispatch to handle NFC intents
//		mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, null);
	}


	Bundle options = new Bundle();
	public  void enAbleNfcReaderMode(){
		mNfcAdapter.enableReaderMode(
				mActivity,
				(NfcAdapter.ReaderCallback) tag -> {
					Log.d("Nfcend", tag.toString());
				},
			NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, options

		);
		}




	/**
	 * Disable dispacher Remove the most important priority for foreground application
	 */
	public void disableDispatch() {
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(mActivity);
		}
	}

	/**
	 * Activate NFC dispacher to read NFC Card Set the most important priority to the foreground application
	 */
	public void enableDispatch() {
		if (mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, INTENT_FILTER, TECH_LIST);
		}
	}

	/**
	 * Getter mNfcAdapter
	 * 
	 * @return the mNfcAdapter
	 */
	public NfcAdapter getmNfcAdapter() {
		return mNfcAdapter;
	}
}
