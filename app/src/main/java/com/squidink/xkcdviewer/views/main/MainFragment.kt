package com.squidink.xkcdviewer.views.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
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
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.FragmentMainBinding
import com.squidink.xkcdviewer.extensions.centerImageInRect
import com.squidink.xkcdviewer.extensions.setBottomInset
import com.squidink.xkcdviewer.extensions.setTopInset
import com.squidink.xkcdviewer.extensions.showContentDialog
import com.squidink.xkcdviewer.extensions.showErrorDialog
import com.squidink.xkcdviewer.extensions.spinForever
import com.squidink.xkcdviewer.utils.ImageUtils
import com.squidink.xkcdviewer.utils.ImageUtils.Companion.BitmapPaddingTransformation
import com.squidink.xkcdviewer.utils.ImageUtils.Companion.InversionTransformation
import com.squidink.xkcdviewer.utils.SearchParseUtils
import com.squidink.xkcdviewer.views.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment: Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var picasso : Picasso

    private val viewModel: MainViewModel by viewModels()
    private var prevUiState: MainUiModel? = null
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var navController: NavController

    private val picassoTarget = object: Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if(_binding != null) {
                val targetRect = Rect()
                binding.freeSpaceReference.getGlobalVisibleRect(targetRect)
                binding.xkcdImage.setImageBitmap(bitmap)
                binding.xkcdImage.centerImageInRect(targetRect)
                showLoading(false)
            }
        }

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
            showErrorDialog()
            showLoading(false)
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            showLoading(true)
        }
    }

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
        setInsets()
        configureMenu()
        binding.xkcdImage.minZoom = 0.7f

        // Configure main ViewModel to observe UI state changes
        viewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiModel ->
                showLoading(uiModel.isLoading)

                if(uiModel.isError) {
                    showErrorDialog()
                    showLoading(false)
                    return@observe
                } else {
                    if(prevUiState?.number != uiModel.number ||
                        binding.xkcdImage.drawable == null) {

                        setTitle(uiModel.title)
                        setNumber(uiModel.number.toString())
                        setImage(uiModel.imageUrl)
                    }
                }
                prevUiState = uiModel
            }
        }

        // Configure SearchViewModel provides data when a search result is selected in SearchFragment.
        searchViewModel.uiState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.selectedResult?.let {selectedResult ->
                SearchParseUtils.getComicNumberFromUrl(selectedResult.link)?.let { number ->
                    viewModel.loadSpecific(number)
                }
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
                    R.id.menuoption_whatif -> {
                        navController.navigate(R.id.action_mainFragment_to_whatIfFragment)
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
            showLoading(true)
            context?.let { ctx ->
                /**
                 * If night mode is enabled, apply a transformation to the image,
                 * converting it to greyscale and then inverting it. See [ImageUtils]
                 */
                if(ImageUtils.isNightModeEnabled(ctx)) {
                    picasso.load(image)
                        .transform(BitmapPaddingTransformation(ImageUtils.PADDING_PIXELS))
                        .transform(InversionTransformation())
                        .into(picassoTarget)
                } else {
                    picasso.load(image)
                        .transform(BitmapPaddingTransformation(ImageUtils.PADDING_PIXELS))
                        .into(picassoTarget)
                }
            }
        }
    }

    private fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDialog() {
        this.showErrorDialog(
            title = getString(R.string.error_title),
            message = getString(R.string.error_body),
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindButtons() {
        binding.buttonPrevious.setOnClickListener { viewModel.loadPrevious() }
        binding.buttonRandom.setOnClickListener { viewModel.loadRandom() }
        binding.buttonLatest.setOnClickListener { viewModel.loadLatest() }
        binding.buttonNext.setOnClickListener { viewModel.loadNext() }

        binding.xkcdImage.setOnClickListener {
            showToast(getString(R.string.show_alt_text))
        }
        binding.xkcdImage.setOnLongClickListener {
            this.showContentDialog(
                title = viewModel.uiState.value?.title ?: getString(R.string.alt_text),
                message = viewModel.uiState.value?.altText ?: getString(R.string.alt_text_error)
            )
            true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.let {binding ->
            if(isLoading) {
                binding.loadingImage.visibility = View.VISIBLE
                binding.loadingImage.spinForever()
            } else {
                binding.loadingImage.visibility = View.GONE
            }
        }
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

    private fun setInsets() {
        setTopInset(binding.mainToolbar)
        setBottomInset(binding.buttonContainer)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}