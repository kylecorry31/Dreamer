package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JournalFragment : BoundFragment<FragmentJournalBinding>() {

    @Inject lateinit var repo: DreamRepo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo.getAllLive().observe(viewLifecycleOwner) {
            println(it.size)
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }
}