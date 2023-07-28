package com.diplomproject.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.FragmentHistoryFavoriteBinding
import com.diplomproject.di.ConnectKoinModules.favoriteScreenScope
import com.diplomproject.domain.base.BaseFragment
import com.diplomproject.model.data_word_request.AppState
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.utils.ui.viewById

class FavoriteFragment : BaseFragment<AppState, FavoriteInteractor>() {


    private var _binding: FragmentHistoryFavoriteBinding? = null
    private val binding
        get() = _binding!!


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
        playContentUrl(url)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewModel()
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        releaseMediaPlayer()
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
            throw IllegalStateException("The ViewModel should be initialised first")
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
        binding.history.text = getString(R.string.favorites)
        historyActivityRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            historyActivityRecyclerview
        )
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
