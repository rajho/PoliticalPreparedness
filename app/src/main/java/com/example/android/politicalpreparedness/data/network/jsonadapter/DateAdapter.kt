package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DateAdapter {

	@FromJson fun dateFromJson(dateJson: String): Date? {
		return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateJson)
	}

	@ToJson fun dateToJson(date: Date): String? {
		return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
	}
}