package com.squidink.xkcdviewer.views.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.FragmentMainBinding
import com.squidink.xkcdviewer.extensions.showErrorDialog
import com.squidink.xkcdviewer.utils.ImageUtils
import com.squidink.xkcdviewer.utils.SearchParseUtils
import com.squidink.xkcdviewer.views.search.SearchResultViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment: Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    @Inject lateinit var picasso : Picasso

    private val searchViewModel : SearchResultViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
        configureMenu()

        // Configure main ViewModel to observe UI state changes
        viewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiModel ->
                if(uiModel.isError) {
                    showErrorDialog()
                    return@observe
                } else {
                    setTitle(uiModel.title)
                    setNumber(uiModel.number.toString())
                    setImage(uiModel.imageUrl)
                    setAltText(uiModel.altText)
                }
            }
        }

        // Configure SearchViewModel provides data when a search result is selected in SearchFragment.
        searchViewModel.searchResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            SearchParseUtils.getComicNumberFromUrl(it.link)?.let { number ->
                viewModel.loadSpecific(number)
            }
        })

        // Load latest comic when the fragment is created or load current if being recreated
        viewModel.uiState.value?.number?.let { viewModel.loadSpecific(it) } ?: viewModel.loadLatest()
    }

    private fun configureMenu() {
        binding.mainToolbar.apply {
            setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.menuoption_search -> {
                        navController.navigate(R.id.action_mainFragment_to_searchFragment)
                        true
                    }
                    R.id.menuoption_settings -> {
                        navController.navigate(R.id.action_mainFragment_to_settingsFragment)
                        true
                    }
                    R.id.menuoption_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setTitle(title: String) {
        binding.toolbarTitle.text = title
    }

    private fun setNumber(number: String) {
        binding.toolbarSubtitle.text = "#"+number
    }

    private fun setImage(image: String) {
        binding.let {
            it.xkcdImage.resetZoom()
            context?.let { ctx ->

                /**
                 * If night mode is enabled, apply a transformation to the image,
                 * converting it to greyscale and then inverting it. See [ImageUtils]
                 */
                if(ImageUtils.isNightModeEnabled(ctx)) {
                    picasso.load(image)
                        .transform(ImageUtils.Companion.InversionTransformation())
                        .into(binding.xkcdImage)
                } else {
                    picasso.load(image)
                        .into(binding.xkcdImage)
                }
            }
        }
    }

    private fun setAltText(altText: String) {
        binding.xkcdAltText.text = altText
    }

    fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDialog() {
        this.showErrorDialog(
            title = getString(R.string.error_title),
            message = getString(R.string.error_body),
        )
    }

    private fun bindButtons() {
        binding.buttonPrevious.setOnClickListener { viewModel.loadPrevious() }
        binding.buttonRandom.setOnClickListener { viewModel.loadRandom() }
        binding.buttonLatest.setOnClickListener { viewModel.loadLatest() }
        binding.buttonNext.setOnClickListener { viewModel.loadNext() }
    }

    private fun createShareIntent() {
        viewModel.uiState.value?.let {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, it.title)
                putExtra(Intent.EXTRA_TEXT, getString(R.string.xkcd_base_url)+it.number)
                type = "text/html"
            }

            val shareIntent = Intent.createChooser(sendIntent, binding.mainToolbar.title)
            startActivity(shareIntent)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}