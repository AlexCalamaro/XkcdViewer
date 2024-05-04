package com.acalamaro.xkcdviewer.views.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchItems
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.acalamaro.xkcdviewer.databinding.FragmentSearchBinding
import com.acalamaro.xkcdviewer.extensions.showErrorDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(), SearchContract.View {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var picasso: Picasso
    @Inject lateinit var googleApiDataSource: GoogleSearchApiDataSource

    lateinit var presenter: SearchPresenter
    lateinit var adapter: SearchAdapter
    private lateinit var recycler: RecyclerView

    private val searchViewModel : SearchResultViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        presenter = SearchPresenter(this, SearchModel(
            googleApiDataSource,
            requireContext().getString(R.string.google_api_key), requireContext().getString(R.string.search_instance_identifier)))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = binding.searchRecycler
        recycler.layoutManager = layoutManager
        adapter = SearchAdapter(
            GoogleSearchResponse(null, null, null, null),
            picasso,
            object: SearchAdapter.SearchItemClickListener {
            override fun onItemClicked(data: GoogleSearchItems) {
                searchViewModel.searchResult.value = data
                navController.navigateUp()
            }
        })
        recycler.adapter = adapter

        configureButtons()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler.adapter = null
        recycler.layoutManager = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    @SuppressLint("NotifyDataSetChanged")
    // Only 10 results returned at a time and they are paged so notifyDatasetChanged() is ok
    override fun displaySearchResults(results: GoogleSearchResponse) {
        adapter.data = results
        adapter.notifyDataSetChanged()
    }

    override fun showPrevButton(visible: Boolean) {
        if(visible) {
            binding.buttonSearchPageBack.visibility = View.VISIBLE
        } else {
            binding.buttonSearchPageBack.visibility = View.GONE
        }
    }

    override fun showNextButton(visible: Boolean) {
        if(visible) {
            binding.buttonSearchPageNext.visibility = View.VISIBLE
        } else {
            binding.buttonSearchPageNext.visibility = View.GONE
        }
    }

    override fun showErrorDialog(message: String) {
        this.showErrorDialog(
            title = getString(R.string.error_title),
            message = getString(R.string.error_body)
        )
    }

    private fun configureButtons() {
        binding.buttonSearchPageNext.setOnClickListener {
            presenter.nextPage()
            binding.searchScrollView.smoothScrollTo(0,0)
        }
        binding.buttonSearchPageBack.setOnClickListener {
            presenter.prevPage()
            binding.searchScrollView.smoothScrollTo(0,0)
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.submitSearchQuery(binding.searchEditText.text.toString())
                binding.searchPromptText.visibility = View.GONE

                // Hide softkeyboard
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
            true
        }
    }
}