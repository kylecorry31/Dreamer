package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.kylecorry.andromeda.alerts.CoroutineAlerts
import com.kylecorry.andromeda.alerts.toast
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.andromeda.pickers.CoroutinePickers
import com.kylecorry.oneironaut.R
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding
import com.kylecorry.oneironaut.domain.Dream
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo
import com.kylecorry.oneironaut.ui.lists.DreamAction
import com.kylecorry.oneironaut.ui.lists.DreamListItemMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class JournalFragment : BoundFragment<FragmentJournalBinding>() {

    @Inject
    lateinit var repo: DreamRepo

    private val mapper by lazy {
        DreamListItemMapper(requireContext(), this::handleDreamAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dreamList.emptyView = binding.dreamsEmptyText
        repo.getAllLive().observe(viewLifecycleOwner) {
            binding.dreamList.setItems(it.sortedByDescending { it.time }, mapper)
        }
        binding.addBtn.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val text = withContext(Dispatchers.Main) {
                    CoroutinePickers.text(
                        requireContext(),
                        getString(R.string.dream)
                    )
                }

                if (text != null) {
                    repo.add(Dream(0, Instant.now(), description = text))
                }
            }
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }

    private fun handleDreamAction(dream: Dream, action: DreamAction) {
        when (action) {
            DreamAction.Edit -> lifecycleScope.launchWhenResumed {
                val text = withContext(Dispatchers.Main) {
                    CoroutinePickers.text(
                        requireContext(),
                        getString(R.string.dream),
                        default = dream.description
                    )
                }

                if (text != null) {
                    repo.add(dream.copy(description = text))
                }
            }
            DreamAction.Delete -> lifecycleScope.launchWhenResumed {
                val delete = withContext(Dispatchers.Main) {
                    !CoroutineAlerts.dialog(
                        requireContext(),
                        getString(R.string.delete_dream_prompt)
                    )
                }
                if (delete) {
                    repo.delete(dream)
                }
            }
        }
    }

}