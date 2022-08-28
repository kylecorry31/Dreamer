package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kylecorry.andromeda.core.coroutines.ControlledRunner
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding
import com.kylecorry.oneironaut.domain.Dream
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class JournalFragment : BoundFragment<FragmentJournalBinding>() {

    @Inject
    lateinit var repo: DreamRepo

    private var dream = Dream(0, getToday())
    private val runner = ControlledRunner<Dream>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dreamDate.date = getToday()
        loadDream(binding.dreamDate.date)
        binding.dreamDate.setOnDateChangeListener {
            loadDream(it)
        }
        binding.dreamDate.setOnCalendarLongPressListener {
            binding.dreamDate.date = getToday()
        }
        binding.dreamEdit.addTextChangedListener {
            saveDream()
        }
        binding.chipLucid.setOnCheckedChangeListener { _, _ ->
            saveDream()
        }
        binding.chipRecurring.setOnCheckedChangeListener { _, _ ->
            saveDream()
        }
        binding.chipNightmare.setOnCheckedChangeListener { _, _ ->
            saveDream()
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }

    private fun saveDream() {
        val d = dream.copy(
            description = binding.dreamEdit.text?.toString() ?: "",
            isLucid = binding.chipLucid.isChecked,
            isRecurring = binding.chipRecurring.isChecked,
            isNightmare = binding.chipNightmare.isChecked
        )
        if (d.id == 0L && d.description.isBlank()) {
            return
        }
        lifecycleScope.launchWhenResumed {
            dream = runner.cancelPreviousThenRun {
                if (d.description.isBlank()) {
                    repo.delete(d)
                    return@cancelPreviousThenRun Dream(0, d.date)
                }
                val id = repo.add(d)
                d.copy(id = id)
            }
        }
    }

    private fun loadDream(date: LocalDate) {
        lifecycleScope.launchWhenResumed {
            dream = repo.get(date) ?: Dream(0, date)
            withContext(Dispatchers.Main) {
                binding.dreamEdit.setText(dream.description)
                binding.chipLucid.isChecked = dream.isLucid
                binding.chipRecurring.isChecked = dream.isRecurring
                binding.chipNightmare.isChecked = dream.isNightmare
            }
        }
    }

    private fun getToday(): LocalDate {
        val date = ZonedDateTime.now().toLocalDateTime()
        return if (date.toLocalTime().isBefore(LocalTime.NOON)) {
            date.toLocalDate().minusDays(1)
        } else {
            date.toLocalDate()
        }
    }

}