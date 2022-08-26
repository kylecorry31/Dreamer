package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo

class JournalFragment : BoundFragment<FragmentJournalBinding>() {

    private val repo by lazy { DreamRepo.getInstance(requireContext()) }

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