package com.squidink.xkcdviewer.views.search

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.GoogleSearchApiDataSource
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchItems
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.squidink.xkcdviewer.databinding.FragmentSearchBinding
import com.squidink.xkcdviewer.extensions.setTopInset
import com.squidink.xkcdviewer.extensions.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var picasso: Picasso
    @Inject lateinit var googleApiDataSource: GoogleSearchApiDataSource

    private val viewModel: SearchViewModel by activityViewModels()
    lateinit var adapter: SearchAdapter
    private lateinit var recycler: RecyclerView

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(false)
        setInsets()

        recycler = binding.searchRecycler
        recycler.layoutManager = layoutManager
        adapter = SearchAdapter(
            GoogleSearchResponse(null, null, null, null),
            picasso,
            object: SearchAdapter.SearchItemClickListener {
            override fun onItemClicked(data: GoogleSearchItems) {
                viewModel.selectSearchResult(data)
                navController.navigateUp()
            }
        })
        recycler.adapter = adapter
        configureButtons()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler.adapter = null
        recycler.layoutManager = null
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { searchUiModel ->
            showLoading(searchUiModel.isLoading)
            if(searchUiModel.isError) {
                showErrorDialog()
            } else {
                searchUiModel.searchResult?.let {
                    displaySearchResults(it)
                }
                showPrevButton(searchUiModel.shouldShowPreviousButton)
                showNextButton(searchUiModel.shouldShowNextButton)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.let { binding ->
            if(isLoading) {
                binding.loadingImage.visibility = View.VISIBLE
                ObjectAnimator
                    .ofFloat(binding.loadingImage, "rotation", 0f, 360f)
                    .apply {
                        duration = 1000
                        repeatCount = ObjectAnimator.INFINITE
                        repeatMode = ObjectAnimator.RESTART
                        interpolator = LinearInterpolator()
                        start()
                    }
            } else {
                binding.loadingImage.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    // Only 10 results returned at a time and they are paged so notifyDatasetChanged() is ok
    private fun displaySearchResults(results: GoogleSearchResponse) {
        adapter.data = results
        adapter.notifyDataSetChanged()

        if(results.items.isNullOrEmpty()) {
            binding.searchPromptText.visibility = View.VISIBLE
        } else {
            binding.searchPromptText.visibility = View.GONE
        }
    }

    private fun showPrevButton(visible: Boolean) {
        binding.buttonSearchPageBack.visibility = if(visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showNextButton(visible: Boolean) {
        binding.buttonSearchPageNext.visibility = if(visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showErrorDialog() {
        this.showErrorDialog(
            title = getString(R.string.error_title),
            message = getString(R.string.error_body)
        )
    }

    private fun configureButtons() {
        binding.buttonSearchPageNext.setOnClickListener {
            viewModel.nextPage()
            binding.searchScrollView.smoothScrollTo(0,0)
        }
        binding.buttonSearchPageBack.setOnClickListener {
            viewModel.prevPage()
            binding.searchScrollView.smoothScrollTo(0,0)
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.submitSearchQuery(binding.searchEditText.text.toString())
                // Hide softkeyboard
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
            true
        }
    }

    private fun setInsets() {
        setTopInset(binding.searchContainer)
    }
}