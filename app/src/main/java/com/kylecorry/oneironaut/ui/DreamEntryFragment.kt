package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.databinding.FragmentDreamEntryBinding
import com.kylecorry.oneironaut.domain.Dream
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DreamEntryFragment : BoundFragment<FragmentDreamEntryBinding>() {

    @Inject
    lateinit var repo: DreamRepo

    private var dream: Dream = Dream()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getLong("dream_id")?.let {
            lifecycleScope.launchWhenResumed {
                dream = loadDream(it) ?: Dream()
                withContext(Dispatchers.Main) {
                    updateDream()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleEntry.addTextChangedListener {
            dream = dream.copy(title = it?.toString() ?: "")
        }

        binding.description.addTextChangedListener {
            dream = dream.copy(description = it?.toString() ?: "")
        }

        binding.lucid.setOnCheckedChangeListener { buttonView, isChecked ->
            dream = dream.copy(isLucid = isChecked)
        }

        binding.recurring.setOnCheckedChangeListener { buttonView, isChecked ->
            dream = dream.copy(isRecurring = isChecked)
        }

        binding.nightmare.setOnCheckedChangeListener { buttonView, isChecked ->
            dream = dream.copy(isNightmare = isChecked)
        }

        binding.done.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                if (dream.title.isNotBlank() || dream.description.isNotBlank()) {
                    repo.add(dream)
                }
                findNavController().navigateUp()
            }
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDreamEntryBinding {
        return FragmentDreamEntryBinding.inflate(layoutInflater, container, false)
    }

    private fun updateDream() {
        if (!isBound) return
        binding.titleEntry.setText(dream.title)
        binding.description.setText(dream.description)
        binding.lucid.isChecked = dream.isLucid
        binding.recurring.isChecked = dream.isRecurring
        binding.nightmare.isChecked = dream.isNightmare
    }

    private suspend fun loadDream(id: Long): Dream? {
        return repo.get(id)
    }


}