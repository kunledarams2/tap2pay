/*
 * Copyright (C) 2013 MILLAU Julien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.e.bloctap2pay.nfc.utils;

import android.text.TextUtils;
import android.util.Log;

import com.e.bloctap2pay.nfc.iso7816emv.EmvTags;
import com.e.bloctap2pay.nfc.model.EmvCard;
import com.e.bloctap2pay.nfc.model.Service;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.devnied.bitlib.BytesUtils;

/**
 * Extract track data
 * 
 * @author MILLAU Julien
 * 
 */
public final class TrackUtils {

	/**
	 * Class logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackUtils.class);

	/**
	 * Track 2 pattern
	 */
	private static final Pattern TRACK2_PATTERN = Pattern.compile("([0-9]{1,19})D([0-9]{4})([0-9]{3})?(.*)");

	/**
	 * Extract track 2 data
	 * 
	 * @param pEmvCard
	 *            Object card representation
	 * @param pData
	 *            data to parse
	 * @return true if the extraction succeed false otherwise
	 */
	public static boolean extractTrack2Data(final EmvCard pEmvCard, final byte[] pData) {
		boolean ret = false;
		byte[] track2 = TlvUtil.getValue(pData, EmvTags.TRACK_2_EQV_DATA, EmvTags.TRACK2_DATA);


		byte[] iad = TlvUtil.getValue(pData, EmvTags.ISSUER_APPLICATION_DATA);
		byte[] unpredictableByte = TlvUtil.getValue(pData, EmvTags.UNPREDICTABLE_NUMBER );
		String dataIad = BytesUtils.bytesToStringNoSpace(iad);
		pEmvCard.setIad(dataIad);
//		pEmvCard.setUnpredictable(BytesUtils.bytesToStringNoSpace(unpredictableByte));
		pEmvCard.setAip(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.APPLICATION_INTERCHANGE_PROFILE )));
		pEmvCard.setAtc(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.APP_TRANSACTION_COUNTER )));
		pEmvCard.setTvr(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.TERMINAL_VERIFICATION_RESULTS )));
		pEmvCard.setCryptogram(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.APP_CRYPTOGRAM )));
		pEmvCard.setFilename(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.DEDICATED_FILE_NAME )));
		pEmvCard.setCvm(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.CVM_RESULTS )));
		pEmvCard.setPan(BytesUtils.bytesToStringNoSpace(TlvUtil.getValue(pData, EmvTags.PAN_SEQUENCE_NUMBER )));
		String  unpredictableString = BytesUtils.bytesToStringNoSpace(unpredictableByte);
		Log.d("TrackUtils","iad++++>>>>>"+dataIad + "\n=>>upred=>>"+unpredictableString );
		if (track2 != null) {
			String data = BytesUtils.bytesToStringNoSpace(track2);
			Log.d("TrackUtils",data);

			if (!TextUtils.isEmpty(data) && data.endsWith("F")) {
				String track2D= data.substring(0, data.length() - 1);

				if(track2D.length() != 32){
					pEmvCard.setTrack2(reduceToMaxLength(track2D, 32));
					Log.d("TrackUtils","Track2=>>>" + reduceToMaxLength(track2D, 32));
				}else
				{
					pEmvCard.setTrack2(track2D);
				}

				Log.d("TrackUtils","Track2=>>>" + track2D);

			}else {
				pEmvCard.setTrack2(data);
			}
			Matcher m = TRACK2_PATTERN.matcher(data);

			// Check pattern
			if (m.find()) {
				// read card number

				pEmvCard.setCardNumber(m.group(1));
				// Read expire date
				SimpleDateFormat sdf = new SimpleDateFormat("yyMM", Locale.getDefault());
				try {
					pEmvCard.setExpireDate(DateUtils.truncate(sdf.parse(m.group(2)), Calendar.MONTH));
				} catch (ParseException e) {
					LOGGER.error("Unparsable expire card date : {}", e.getMessage());
					return ret;
				}
				// Read service
				pEmvCard.setService(new Service(m.group(3)));
				ret = true;
			}
		}
		return ret;
	}

	public static String reduceToMaxLength(String input, int maxLength) {
		if (input.length() <= maxLength) {
			// No need to reduce, the string is already within the desired length
			return input;
		} else {
			// Reduce the string to the specified maxLength
			return input.substring(0, maxLength);
		}
	}

	/**
	 * Private constructor
	 */
	private TrackUtils() {
	}

}
