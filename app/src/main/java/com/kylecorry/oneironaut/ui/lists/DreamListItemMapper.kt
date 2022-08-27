package com.kylecorry.oneironaut.ui.lists

import android.content.Context
import androidx.core.text.backgroundColor
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.oneironaut.R
import com.kylecorry.oneironaut.domain.Dream
import com.kylecorry.oneironaut.ui.FormatService
import com.kylecorry.sol.time.Time.toZonedDateTime

class DreamListItemMapper(
    private val context: Context,
    private val actionHandler: (dream: Dream, action: DreamAction) -> Unit
) : ListItemMapper<Dream> {

    private val formatter by lazy { FormatService(context) }

    override fun map(value: Dream): ListItem {
        return ListItem(
            value.id,
            buildSpannedString {
                if (value.title.isNotBlank()) {
                    bold { append(value.title + " ") }
                }
                append(value.description)
            },
            formatter.formatDate(
                value.time.toZonedDateTime(),
                includeWeekDay = false
            ),
            singleLineTitle = true,
            icon = ResourceListIcon(
                R.drawable.ic_journal,
                Resources.androidTextColorSecondary(context)
            ),
            data = listOfNotNull(
                if (value.isLucid) ListItemData(
                    buildSpannedString {
                        color(Resources.color(context, R.color.blue)) {
                            append(context.getString(R.string.lucid))
                        }
                    },
                    null
                ) else null,
                if (value.isRecurring) ListItemData(
                    buildSpannedString {
                        color(Resources.color(context, R.color.green)) {
                            append(context.getString(R.string.recurring))
                        }
                    },
                    null
                ) else null,
                if (value.isNightmare) ListItemData(
                    buildSpannedString {
                        color(Resources.color(context, R.color.red)) {
                            append(context.getString(R.string.nightmare))
                        }
                    },
                    null
                ) else null,
            ),
            menu = listOf(
                ListMenuItem(context.getString(R.string.delete)) {
                    actionHandler(
                        value,
                        DreamAction.Delete
                    )
                }
            )
        ) {
            actionHandler(value, DreamAction.Edit)
        }
    }

}