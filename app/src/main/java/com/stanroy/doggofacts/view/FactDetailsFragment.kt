package com.stanroy.doggofacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.stanroy.doggofacts.R
import com.stanroy.doggofacts.databinding.FragmentFactDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class FactDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFactDetailsBinding

    // retrieving passed argument from FactListFragment
    private val navArgs: FactDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fact_details, container, false)
        binding.lifecycleOwner = this

        setUpFactInfo()

        return binding.root
    }

    private fun setUpFactInfo() {
        val factBody = navArgs.dogFact
        binding.tvFactBody.text = factBody

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyy HH:mm", Locale.getDefault())
        val dateString = simpleDateFormat.format(navArgs.factUpdateDate)
        binding.tvFactDate.text = resources.getString(R.string.tool_fact_date, dateString)
    }

}