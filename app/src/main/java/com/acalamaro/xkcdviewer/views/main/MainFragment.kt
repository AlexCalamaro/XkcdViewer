package com.acalamaro.xkcdviewer.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchBaseObject
import com.acalamaro.xkcdviewer.databinding.FragmentMainBinding
import com.acalamaro.xkcdviewer.utils.ImageUtils
import com.acalamaro.xkcdviewer.utils.SearchParseUtils
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import com.acalamaro.xkcdviewer.views.search.SearchResultViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainFragment : Fragment(), MainContract.View {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var xkcdApiDataSource: XkcdApiDataSource
    @Inject lateinit var picasso : Picasso
    private lateinit var presenter: MainPresenter

    private val searchViewModel : SearchResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        presenter = MainPresenter(this, MainModel(xkcdApiDataSource))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButtons()
        presenter.onCreate(savedInstanceState)

        // searchViewModel provides data when a search result is selected in SearchFragment.
        // This is a lifecycle-safe way to communicate between fragments.
        searchViewModel.searchResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            SearchParseUtils.getComicNumberFromUrl(it.link)?.let { number ->
                presenter.loadSpecific(number)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.requestInstanceBundle(outState)
        super.onSaveInstanceState(outState)
    }

    override fun setTitle(title: String) {
        binding.xkcdTitle.text = title
    }

    override fun setNumber(number: String) {
        binding.xkcdNumber.text = number
    }

    override fun setImage(image: String) {
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

    override fun setAltText(altText: String) {
        binding.xkcdAltText.text = altText
    }

    override fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun bindButtons() {
        binding.buttonPrevious.setOnClickListener { presenter.loadPrevious() }
        binding.buttonRandom.setOnClickListener { presenter.loadRandom() }
        binding.buttonLatest.setOnClickListener { presenter.loadLatest() }
        binding.buttonNext.setOnClickListener { presenter.loadNext() }

        binding.buttonSearch.setOnClickListener { (activity as MainActivity).openSearch() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}