package com.kylecorry.oneironaut.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import com.kylecorry.oneironaut.R
import com.kylecorry.oneironaut.ui.UiUtils.flatten

class ToolTitleView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    val leftQuickAction: ImageButton
    val rightQuickAction: ImageButton
    val title: TextView
    val subtitle: TextView


    init {
        inflate(context, R.layout.view_tool_title, this)
        leftQuickAction = findViewById(R.id.left_quick_action)
        rightQuickAction = findViewById(R.id.right_quick_action)
        title = findViewById(R.id.title)
        subtitle = findViewById(R.id.subtitle)

        // Update attributes
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ToolTitleView, 0, 0)
        title.text = a.getString(R.styleable.ToolTitleView_title) ?: ""
        subtitle.text = a.getString(R.styleable.ToolTitleView_subtitle) ?: ""

        subtitle.isVisible = a.getBoolean(R.styleable.ToolTitleView_showSubtitle, true)

        val leftIcon = a.getResourceId(R.styleable.ToolTitleView_leftQuickActionIcon, -1)
        val rightIcon = a.getResourceId(R.styleable.ToolTitleView_rightQuickActionIcon, -1)

        if (leftIcon != -1) {
            leftQuickAction.isVisible = true
            leftQuickAction.setImageResource(leftIcon)
        }

        if (rightIcon != -1) {
            rightQuickAction.isVisible = true
            rightQuickAction.setImageResource(rightIcon)
        }

        UiUtils.setButtonState(leftQuickAction, false)
        UiUtils.setButtonState(rightQuickAction, false)

        val flattenQuickActions = a.getBoolean(R.styleable.ToolTitleView_flattenQuickActions, false)
        if (flattenQuickActions) {
            rightQuickAction.flatten()
            leftQuickAction.flatten()
        }
    }

}