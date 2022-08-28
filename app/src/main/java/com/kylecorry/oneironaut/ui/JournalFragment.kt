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
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }

    private fun saveDream() {
        dream = dream.copy(description = binding.dreamEdit.text?.toString() ?: "")
        lifecycleScope.launchWhenResumed {
            dream = runner.cancelPreviousThenRun {
                val id = repo.add(dream)
                dream.copy(id = id)
            }
        }
    }

    private fun loadDream(date: LocalDate) {
        lifecycleScope.launchWhenResumed {
            dream = repo.get(date) ?: Dream(0, date)
            withContext(Dispatchers.Main){
                binding.dreamEdit.setText(dream.description)
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