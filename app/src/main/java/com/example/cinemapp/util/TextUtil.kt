package com.example.cinemapp.util

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun setExpandableTextView(
    text: String,
    phrase: String = "More",
    phraseColor: Int,
    maxChars: Int,
    textView: TextView,
    onClickEvent: () -> Unit
) {
    if (text.length > maxChars) {
        val spannableString = makeExpandableText(
            text = text,
            phrase = phrase,
            phraseColor = phraseColor,
            maxChars = maxChars,
            onClickEvent = onClickEvent
        )
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    } else textView.text = text
}

fun makeExpandableText(
    text: String,
    phrase: String = "More",
    phraseColor: Int,
    maxChars: Int,
    onClickEvent: () -> Unit
): SpannableString {

    val shortText = "${text.substring(0, maxChars - 1)}... $phrase"
    val spannableString = SpannableString(shortText)

    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = phraseColor
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            ds.isUnderlineText = false
        }

        override fun onClick(widget: View) {
            onClickEvent()
        }
    }
    val start = shortText.length - phrase.length
    val end = shortText.length
    spannableString.setSpan(
        clickableSpan,
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}