package com.kylecorry.oneironaut.ui.lists

import android.content.Context
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
            value.title,
            value.description,
            singleLineSubtitle = true,
            icon = ResourceListIcon(
                R.drawable.ic_journal,
                Resources.androidTextColorSecondary(context)
            ),
            trailingText = formatter.formatDate(
                value.time.toZonedDateTime(),
                includeWeekDay = false
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