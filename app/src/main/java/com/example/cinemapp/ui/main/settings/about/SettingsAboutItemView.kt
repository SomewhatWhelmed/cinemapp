package com.example.cinemapp.ui.main.settings.about

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cinemapp.R
import com.example.cinemapp.databinding.SettingsAboutItemViewBinding
import com.example.cinemapp.util.Direction
import com.example.cinemapp.util.setMargin

class SettingsAboutItemView : ConstraintLayout {

    private lateinit var tvLabel: TextView
    private lateinit var tvValue: TextView
    private lateinit var separator: View
    private var value = ""
    private var label = ""
    private var isLast = false
    private var _binding: SettingsAboutItemViewBinding? = null
    private val binding get() = _binding!!

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getDataFromXml(attrs, context)
        init(context)
    }

    private fun getDataFromXml(attrs: AttributeSet?, context: Context) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.SettingsAboutItemView)
        value = data.getString(R.styleable.SettingsAboutItemView_value).toString()
        label = data.getString(R.styleable.SettingsAboutItemView_label).toString()
        isLast = data.getBoolean(R.styleable.SettingsAboutItemView_isLast, false)
        data.recycle()
    }

    private fun init(context: Context) {
        _binding = SettingsAboutItemViewBinding.inflate(LayoutInflater.from(context), this, true)
        tvLabel = binding.tvLabel
        tvValue = binding.tvValue
        separator = binding.separator
        if(label.isNotEmpty()) {
            tvLabel.text = label
        }
        if(value.isNotEmpty()) {
            tvValue.text = value
        }
        if(isLast) {
            separator.layoutParams.setMargin(Direction.LEFT, 0f, context)
            separator.layoutParams.setMargin(Direction.RIGHT, 0f, context)
        }
        refreshDrawableState()
    }

    fun setValue(value: String) {
        this.value = value
    }
}