package com.kylecorry.oneironaut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kylecorry.andromeda.alerts.CoroutineAlerts
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.oneironaut.R
import com.kylecorry.oneironaut.databinding.FragmentJournalBinding
import com.kylecorry.oneironaut.domain.Dream
import com.kylecorry.oneironaut.infrastructure.persistence.DreamRepo
import com.kylecorry.oneironaut.ui.lists.DreamAction
import com.kylecorry.oneironaut.ui.lists.DreamListItemMapper
import com.kylecorry.sol.math.Range
import com.kylecorry.sol.time.Time.toZonedDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.*
import javax.inject.Inject

@AndroidEntryPoint
class JournalFragment : BoundFragment<FragmentJournalBinding>() {

    @Inject
    lateinit var repo: DreamRepo

    private var dreams: LiveData<List<Dream>>? = null

    private val mapper by lazy {
        DreamListItemMapper(requireContext(), this::handleDreamAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dreamList.emptyView = binding.dreamsEmptyText
        binding.dreamDate.date = getToday()
        loadDreams(binding.dreamDate.date)
        binding.dreamDate.setOnDateChangeListener {
            loadDreams(it)
        }
        binding.dreamDate.setOnCalendarLongPressListener {
            binding.dreamDate.date = getToday()
        }
        binding.addBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_journal_to_dream_entry
            )
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentJournalBinding {
        return FragmentJournalBinding.inflate(layoutInflater, container, false)
    }

    private fun loadDreams(date: LocalDate) {
        binding.addBtn.isVisible = date == getToday()
        dreams?.removeObserver(this::onDreamsChanged)
        val range = getTimeRange(date)
        dreams = repo.getLive(range.start, range.end)
        dreams?.observe(viewLifecycleOwner, this::onDreamsChanged)
    }

    private fun onDreamsChanged(dreams: List<Dream>) {
        binding.dreamList.setItems(dreams.sortedByDescending { it.time }, mapper)
    }

    private fun getTimeRange(date: LocalDate): Range<Instant> {
        val start = date.atTime(LocalTime.NOON).toZonedDateTime()
        val end = date.plusDays(1).atTime(LocalTime.NOON).toZonedDateTime()
        return Range(start.toInstant(), end.toInstant())
    }

    private fun getToday(): LocalDate {
        val date = ZonedDateTime.now().toLocalDateTime()
        return if (date.toLocalTime().isBefore(LocalTime.NOON)) {
            date.toLocalDate().minusDays(1)
        } else {
            date.toLocalDate()
        }
    }

    private fun handleDreamAction(dream: Dream, action: DreamAction) {
        when (action) {
            DreamAction.Edit -> lifecycleScope.launchWhenResumed {
                findNavController().navigate(
                    R.id.action_journal_to_dream_entry,
                    bundleOf("dream_id" to dream.id)
                )
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