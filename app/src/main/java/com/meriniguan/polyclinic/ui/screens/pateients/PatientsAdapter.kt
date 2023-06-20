package com.meriniguan.polyclinic.ui.screens.pateients

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.PatientItemBinding
import com.meriniguan.polyclinic.model.patient.PatientListItem

class PatientsAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : ListAdapter<PatientListItem, PatientsAdapter.PatientViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = PatientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PatientViewHolder(private val binding: PatientItemBinding) :
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

        fun bind(patient: PatientListItem) {
            binding.apply {
                nameTextView.text = "${patient.surname} ${patient.name} ${patient.fatherName}"
                birthDateTextView.text = patient.birthDate.toDateString()
                conditionTextView.text = patient.condition
                if (patient.condition == "healed") {
                    conditionTextView.setTextColor(context.resources.getColor(R.color.green))
                } else if (patient.condition == "ill") {
                    conditionTextView.setTextColor(context.resources.getColor(R.color.orange))
                } else if (patient.condition == "died") {
                    conditionTextView.setTextColor(context.resources.getColor(R.color.red))
                } else {
                    conditionTextView.setTextColor(context.resources.getColor(R.color.defaultTextColor))
                }
                socialStatusTextView.text = patient.socialStatus
            }
        }

        private fun Long.toDateString(): String {
            return "18.06.2023"
        }
    }

    interface OnItemClickListener {
        fun onItemClick(patient: PatientListItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<PatientListItem>() {
        override fun areItemsTheSame(oldItem: PatientListItem, newItem: PatientListItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PatientListItem, newItem: PatientListItem) =
            oldItem == newItem
    }
}