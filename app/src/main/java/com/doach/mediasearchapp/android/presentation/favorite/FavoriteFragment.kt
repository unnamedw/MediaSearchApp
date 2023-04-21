package com.doach.mediasearchapp.android.presentation.favorite

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.doach.mediasearchapp.android.BuildConfig
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.databinding.FragmentFavoriteBinding
import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.domain.model.Video
import com.doach.mediasearchapp.android.presentation.FavoriteMediaAdapter
import com.doach.mediasearchapp.android.presentation.getContainer
import com.doach.mediasearchapp.android.presentation.openCustomTab
import com.doach.mediasearchapp.android.presentation.showToast
import kotlinx.coroutines.flow.collectLatest

class FavoriteFragment: Fragment(), MenuProvider {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private val favoriteAdapter = FavoriteMediaAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, FavoriteViewModel.Factory(
            getContainer().mediaRepository
        ))[FavoriteViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        bindViewModel()
        initView()
        return binding.root
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favoriteListFlow.collectLatest {
                favoriteAdapter.submitList(it)
            }
        }

        viewModel.eventShowMediaDetail.observe(viewLifecycleOwner) { media ->
            when (media) {
                is Video -> showVideo(media)
                is Image -> showImage(media)
            }
        }
    }

    private fun showImage(image: Image) {
        // TODO: replace below code with right way
        if (BuildConfig.DEBUG) {
            showToast(image.title)
        }
        requireContext().openCustomTab(image.url)
    }

    private fun showVideo(video: Video) {
        // TODO: replace below code with right way
        if (BuildConfig.DEBUG) {
            showToast(video.title)
        }
        requireContext().openCustomTab(video.url)
    }

    private fun initView() {
        setUpActionBar()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvFavoriteList.adapter = favoriteAdapter
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.favorite_toolbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_sort_by_default -> {
                viewModel.changeSortType(SortType.DEFAULT)
                true
            }
            R.id.menu_sort_by_recent -> {
                viewModel.changeSortType(SortType.RECENT)
                true
            }
            else -> true
        }
    }

}

