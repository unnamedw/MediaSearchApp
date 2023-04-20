package com.doach.mediasearchapp.android.presentation.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.doach.mediasearchapp.android.BuildConfig
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.databinding.FragmentHomeBinding
import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.domain.model.Video
import com.doach.mediasearchapp.android.presentation.MediaAdapter
import com.doach.mediasearchapp.android.presentation.getContainer
import com.doach.mediasearchapp.android.presentation.openCustomTab
import com.doach.mediasearchapp.android.presentation.showToast
import kotlinx.coroutines.flow.catch

class HomeFragment: Fragment(), MenuProvider {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.Factory(
            getContainer().provideGetMediaFlowByQueryUseCase(),
            getContainer().mediaRepository
        )
    }
    private val mediaAdapter: MediaAdapter = MediaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindViewHolder()
        initView()
        return binding.root
    }

    private fun bindViewHolder() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mediaFlow.collect {
                mediaAdapter.submitData(it)
            }
        }

        viewModel.eventShowToast.observe(viewLifecycleOwner) { showToast(it) }

        viewModel.eventShowMediaDetail.observe(viewLifecycleOwner) { media ->
            when (media) {
                is Video -> showVideo(media)
                is Image -> showImage(media)
            }
        }
    }

    private fun initView() {
        binding.swipeLayout.setOnRefreshListener {
            mediaAdapter.refresh()
            binding.swipeLayout.isRefreshing = false
        }

        mediaAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }

            errorState?.let {
                showToast(it.error.message.toString())
            }
        }
        binding.rvMediaList.adapter = mediaAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).let {
            it.setSupportActionBar(binding.toolbar)
            it.addMenuProvider(this, viewLifecycleOwner)
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)

        // 검색 메뉴 세팅
        (menu.findItem(R.id.menu_item_search).actionView as SearchView?)?.apply {
            queryHint = getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.submitQuery(query.toString())
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }

            })

        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_item_search -> {
                true
            }
            R.id.menu_item_favorite -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
                true
            }
            else -> true
        }
    }
}