package com.meriniguan.polyclinic.ui.screens.diagnosis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meriniguan.polyclinic.databinding.DiagnosisItemBinding
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisListItem
import com.meriniguan.polyclinic.utils.getDateString

class DiagnosesAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : ListAdapter<DiagnosisListItem, DiagnosesAdapter.DiagnosisViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        val binding = DiagnosisItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiagnosisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class DiagnosisViewHolder(private val binding: DiagnosisItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
            }
        }

        fun bind(diagnosis: DiagnosisListItem) {
            binding.apply {
                deceaseTextView.text = diagnosis.decease
                patientTextView.text = "${diagnosis.patName} ${diagnosis.patFatherName} ${diagnosis.patSurname}"
                doctorTextView.text = "${diagnosis.docName} ${diagnosis.docFatherName} ${diagnosis.docSurname}"
                ambulatoryTreatmentTextView.text = if (diagnosis.isAmbTreatmentNeeded) "нужно" else "не нужно"
                isOnRecordTextView.text = if (diagnosis.isOnRecord) "да" else "нет"
                disabilityPeriodTextView.text = diagnosis.disabilityPeriodMonths.toString()
                treatmentStartDateTextView.text = diagnosis.treatmentStartDate.toDateString()
            }
        }

        private fun Long.toDateString(): String {
            return getDateString(this)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(diagnosis: DiagnosisListItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<DiagnosisListItem>() {
        override fun areItemsTheSame(oldItem: DiagnosisListItem, newItem: DiagnosisListItem) =
            oldItem.diagnosisId == newItem.diagnosisId

        override fun areContentsTheSame(oldItem: DiagnosisListItem, newItem: DiagnosisListItem) =
            oldItem == newItem
    }
}