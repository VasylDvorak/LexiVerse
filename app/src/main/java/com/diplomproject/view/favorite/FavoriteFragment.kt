package com.diplomproject.view.favorite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.FragmentFavoriteBinding
import com.diplomproject.di.ConnectKoinModules.favoriteScreenScope
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.utils.delegates.viewById
import com.diplomproject.view.base_for_dictionary.BaseFragment

class FavoriteFragment : BaseFragment<AppState,
        FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val historyActivityRecyclerview by viewById<RecyclerView>(R.id.history_activity_recyclerview)
    override lateinit var model: FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            ::onItemClick,
            ::onPlayClick,
            ::onRemove
        )
    }

    private fun onRemove(i: Int, dataModel: DataModel) {
        model.remove(dataModel)
        model.subscribe().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {
                        if (it.size != 0) {
                            renderData(appState)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun onItemClick(dataModel: DataModel) {
        dataModel.let {
            router.navigateTo(screen.startDescriptionFragment(it))
        }
    }

    private fun onPlayClick(url: String) {
        model.playContentUrl(url)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewModel()
        initViews()
    }


    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }


    private fun iniViewModel() {
        if (historyActivityRecyclerview.adapter != null) {
            throw IllegalStateException(getString(R.string.exception_error))
        }

        val viewModel: FavoriteViewModel by lazy { favoriteScreenScope.get() }
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {
                        if (it.size != 0) {
                            renderData(appState)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun initViews() {
        historyActivityRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            historyActivityRecyclerview
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.releaseMediaPlayer()
    }
    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
