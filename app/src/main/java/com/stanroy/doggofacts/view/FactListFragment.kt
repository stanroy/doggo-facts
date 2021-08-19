package com.stanroy.doggofacts.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stanroy.doggofacts.R
import com.stanroy.doggofacts.databinding.FragmentFactListBinding
import com.stanroy.doggofacts.model.api.DogFactItem
import com.stanroy.doggofacts.model.utils.FactListStatus.*
import com.stanroy.doggofacts.view.utils.FactListAdapter
import com.stanroy.doggofacts.viewmodel.FactListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class FactListFragment : Fragment() {

    private lateinit var binding: FragmentFactListBinding
    private lateinit var optionsMenuRefresh: MenuItem
    private lateinit var factListAdapter: FactListAdapter

    private val viewModel by viewModel<FactListViewModel>()


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        optionsMenuRefresh = menu.findItem(R.id.options_menu_refresh)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.options_menu_refresh -> {
                viewModel.onRefreshClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFactListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        this.setHasOptionsMenu(true)
        initializeRecyclerViewAdapter()

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            when (status.first) {

                SUCCESS -> {
                    Log.d("MyTag", "NO ERROR")
                    hideProgressBar()
                    viewModel.storedFacts.value?.let { insertNewFacts(it) }
                }

                ERROR -> {
                    hideProgressBar()
                    showErrorHandler(status.second)
                }

                NO_INTERNET_CONNECTION -> {
                    hideProgressBar()
                    showErrorHandler(status.second)
                }

                LOADING -> {
                    showProgressBar()
                }
            }
        })

        binding.btnErrorReload.setOnClickListener {
            hideErrorHandler()
            viewModel.onReloadClicked()
        }


        return binding.root
    }


    private fun showProgressBar() {
        binding.pbListLoading.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbListLoading.visibility = View.GONE
    }

    private fun showErrorHandler(errorMessage: String) {
        binding.llErrorHandler.visibility = View.VISIBLE
        binding.tvErrorMessage.text = resources.getString(R.string.tv_error_message, errorMessage)
        optionsMenuRefresh.isEnabled = false

    }

    private fun hideErrorHandler() {
        binding.llErrorHandler.visibility = View.GONE
        optionsMenuRefresh.isEnabled = true
    }

    private fun insertNewFacts(newFacts: ArrayList<DogFactItem>) {
        Log.d("MyTag", "Insert new facts")
        factListAdapter.submitList(newFacts)
    }

    // setting up recyclerview
    private fun initializeRecyclerViewAdapter() {
        factListAdapter = FactListAdapter()
        binding.rvDogFacts.apply {
            adapter = factListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        onFactClick()
    }

    // recyclerview on-click event
    private fun onFactClick() {
        factListAdapter.setOnFactClick { dogFactItem ->
            // using navigation component directions to navigate between fragments
            val action =
                FactListFragmentDirections.actionFactListFragmentToFactDetailsFragment(
                    dogFactItem.fact,
                    dogFactItem.updateDate
                )
            findNavController().navigate(action)
        }
    }
}