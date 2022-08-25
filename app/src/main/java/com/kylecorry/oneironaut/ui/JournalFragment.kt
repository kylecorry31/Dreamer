package com.kylecorry.oneironaut.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding

class JournalFragment : BoundFragment<FragmentJournalBinding>() {
    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }
}