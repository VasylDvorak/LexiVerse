package com.diplomproject.view.history

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.FragmentHistoryFavoriteBinding
import com.diplomproject.di.ConnectKoinModules.historyScreenScope
import com.diplomproject.domain.base.BaseFragment
import com.diplomproject.model.data_word_request.AppState
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.utils.ui.viewById
import com.diplomproject.view.BOTTOM_SHEET_FRAGMENT_DIALOG_TAG
import com.diplomproject.view.SearchDialogFragment


class HistoryFragment : BaseFragment<AppState, HistoryInteractor>() {


    private var _binding: FragmentHistoryFavoriteBinding? = null
    private val binding
        get() = _binding!!
    private val observerFindWord = Observer<DataModel> { showWordInHistory(it) }

    private val observer = Observer<AppState> { renderData(it) }

    private val historyActivityRecyclerview by viewById<RecyclerView>(R.id.history_activity_recyclerview)

    override lateinit var model: HistoryViewModel

    private val adapter: HistoryAdapter by lazy {

        HistoryAdapter(::onItemClick, ::putInFavorite, ::onPlayClick)
    }

    private fun putInFavorite(favorite: DataModel) {
        model.putInFavorite(favorite)

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.find_word_in_history -> {
                findWordInHistory()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findWordInHistory() {
        val searchDialogFragment = SearchDialogFragment.newInstance()
        searchDialogFragment.setRecyclerView(historyActivityRecyclerview)
        searchDialogFragment.setOnSearchClickListener(object :
            SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                model.apply {
                    subscribeFindWord().observe(viewLifecycleOwner, observerFindWord)
                    findWordInHistory(searchWord)
                    subscribe().observe(viewLifecycleOwner, observer)
                }

            }
        })

        searchDialogFragment.show(
            requireActivity().supportFragmentManager,
            BOTTOM_SHEET_FRAGMENT_DIALOG_TAG
        )
        if (checkSDKversion) {
            val blurEffect = RenderEffect.createBlurEffect(
                25f, 10f,
                Shader.TileMode.MIRROR
            )
            historyActivityRecyclerview.setRenderEffect(blurEffect)
        }
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
        val viewModel: HistoryViewModel by lazy { historyScreenScope.get() }
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
    }

    private fun showWordInHistory(findWord: DataModel) {
        if ((findWord.text == "") || findWord.text.isNullOrBlank() || findWord.text.isNullOrEmpty()) {
            showAlertDialog(
                getString(R.string.dialog_tittle_sorry),
                getString(R.string.no_word_in_history)
            )
        } else {
            findWord.let {
                router.navigateTo(screen.startDescriptionFragment(it))
            }
        }
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
