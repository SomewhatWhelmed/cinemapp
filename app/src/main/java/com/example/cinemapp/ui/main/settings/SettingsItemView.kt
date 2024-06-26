package com.example.cinemapp.ui.main.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cinemapp.R
import com.example.cinemapp.databinding.SettingsItemViewBinding
import com.example.cinemapp.util.Direction
import com.example.cinemapp.util.setMargin

class SettingsItemView : LinearLayout {

    private lateinit var tvLabel: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var separator: View
    private var icon = 0
    private var label = ""
    private var isLast = false
    private var _binding: SettingsItemViewBinding? = null
    private val binding get() = _binding!!

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getDataFromXml(attrs, context)
        init(context)
    }

    private fun getDataFromXml(attrs: AttributeSet?, context: Context) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemView)
        icon = data.getResourceId(R.styleable.SettingsItemView_icon, R.drawable.vic_settings)
        label = data.getString(R.styleable.SettingsItemView_label).toString()
        isLast = data.getBoolean(R.styleable.SettingsItemView_isLast, false)
        data.recycle()
    }

    private fun init(context: Context) {
        _binding = SettingsItemViewBinding.inflate(LayoutInflater.from(context), this, true)
        tvLabel = binding.tvLabel
        ivIcon = binding.ivIcon
        separator = binding.separator
        if(label.isNotEmpty()) {
            tvLabel.text = label
        }
        if(isLast) {
            separator.layoutParams.setMargin(Direction.LEFT, 0f, context)
            separator.layoutParams.setMargin(Direction.RIGHT, 0f, context)
        }
        ivIcon.setImageDrawable(AppCompatResources.getDrawable(context, icon))
        ivIcon.contentDescription = label

        refreshDrawableState()
    }
}