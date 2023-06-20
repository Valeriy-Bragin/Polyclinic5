package com.meriniguan.polyclinic.ui.screens.doctors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meriniguan.polyclinic.databinding.DoctorItemBinding
import com.meriniguan.polyclinic.model.doctor.DoctorListItem

class DoctorsAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<DoctorListItem, DoctorsAdapter.DoctorViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class DoctorViewHolder(private val binding: DoctorItemBinding) :
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

        fun bind(doctor: DoctorListItem) {
            binding.apply {
                nameTextView.text = "${doctor.surname} ${doctor.name} ${doctor.fatherName}"
                nicheTextView.text = doctor.niche
                qualificationTextView.text = doctor.qualification
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(patient: DoctorListItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<DoctorListItem>() {
        override fun areItemsTheSame(oldItem: DoctorListItem, newItem: DoctorListItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DoctorListItem, newItem: DoctorListItem) =
            oldItem == newItem
    }
}