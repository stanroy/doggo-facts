package com.stanroy.doggofacts.view.utils

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stanroy.doggofacts.R
import com.stanroy.doggofacts.databinding.FactListItemBinding
import com.stanroy.doggofacts.model.api.DogFactItem

class FactListAdapter :
    ListAdapter<DogFactItem, FactListAdapter.FactsViewHolder>(diffUtilCallback) {

    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<DogFactItem>() {
            override fun areItemsTheSame(oldItem: DogFactItem, newItem: DogFactItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DogFactItem, newItem: DogFactItem): Boolean {
                return oldItem.fact == newItem.fact
            }

        }
    }

    // setting up on-click event for recyclerview items
    private lateinit var onFactClick: (dogFact: DogFactItem) -> Unit

    fun setOnFactClick(onClick: (dogFactItem: DogFactItem) -> Unit) {
        onFactClick = onClick
    }

    inner class FactsViewHolder(private val binding: FactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val resources: Resources = binding.root.resources

        fun bind(dogFactItem: DogFactItem, position: Int) {
            binding.tvFactTitle.text = resources.getString(R.string.fact_template, position)
            binding.llFactContainer.setOnClickListener {
                onFactClick(dogFactItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        val binding = DataBindingUtil.inflate<FactListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.fact_list_item,
            parent, false
        )

        return FactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        // setting up fact count so it doesn't start with 0 for app user
        val normalizedPosition = position + 1
        getItem(position)?.let { holder.bind(it, normalizedPosition) }
    }
}