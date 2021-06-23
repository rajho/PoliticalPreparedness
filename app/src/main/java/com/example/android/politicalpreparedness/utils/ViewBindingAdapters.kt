package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Election


@BindingAdapter("electionSaved", "election")
fun setElectionSaved(textView: TextView, electionSaved: Boolean?, election: Election?) {
	if (electionSaved == null || election == null){
		textView.visibility = View.GONE
		textView.isEnabled = false

	} else {
		textView.isEnabled = true
		textView.text = if (electionSaved) {
			textView.context.getString(R.string.unfollow_election)
		} else {
			textView.context.getString(R.string.follow_election)
		}

		textView.visibility = View.VISIBLE
	}
}

//	@BindingAdapter("imageUrl", "error")
//	fun loadImage(view: ImageView, url: String, error: Drawable) {
//		Picasso.get().load(url).error(error).into(view)
//	}
