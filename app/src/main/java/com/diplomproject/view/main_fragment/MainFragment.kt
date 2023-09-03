package com.diplomproject.view.main_fragment

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diplomproject.R
import com.diplomproject.databinding.FragmentMainBinding
import com.diplomproject.di.ConnectKoinModules.mainScreenScope
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.utils.network.SharedPreferencesDelegate
import com.diplomproject.utils.ui.viewById
import com.diplomproject.view.base_fragment_dictionary.BaseFragment


const val LIST_KEY = "list_key"

class MainFragment : BaseFragment<AppState, FragmentMainBinding>(FragmentMainBinding::inflate) {

    override lateinit var model: MainViewModel
    private lateinit var preferences: SharedPreferences
    private var dataWithFavorite: List<DataModel> = listOf()
    private val observer = Observer<AppState> { renderData(it) }
    private val mainFragmentRecyclerview by viewById<RecyclerView>(R.id.main_activity_recyclerview)
    private val loadingFrameLayout by viewById<FrameLayout>(R.id.loading_frame_layout)


    private val adapter: MainAdapter by lazy {
        MainAdapter(::onItemClick, ::putInFavorite, ::onPlayClick)
    }

    private fun putInFavorite(favorite: DataModel, position: Int, inFavoriteList: Boolean) {
        dataWithFavorite[position].inFavoriteList = inFavoriteList
        saveListForAdapter(dataWithFavorite)
        if (inFavoriteList) {
            model.putInFavorite(favorite)
        } else {
            model.removeFromFavorite(favorite)
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
        initViewModel()
        initViews()
        model.getRestoredData()?.let { renderData(it) }

        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val listFromSharedPreferences: List<DataModel> by SharedPreferencesDelegate(LIST_KEY)
        if (!listFromSharedPreferences.isNullOrEmpty()) {
            dataWithFavorite = listFromSharedPreferences
            updateAdapter(listFromSharedPreferences)
        }

    }

    private fun initViewModel() {
        if (mainFragmentRecyclerview.adapter != null) {
            throw IllegalStateException(getString(R.string.exception_error))
        }
        val viewModel: MainViewModel by lazy { mainScreenScope.get() }
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner, observer)
    }

    private fun initViews() {
        mainFragmentRecyclerview.layoutManager =
            LinearLayoutManager(context)
        mainFragmentRecyclerview.adapter = adapter
    }


    override fun responseEmpty() {
        showErrorScreen(getString(R.string.empty_server_response_on_success))
    }

    private fun updateAdapter(dataModel: List<DataModel>) {
        showViewSuccess()
        adapter?.setData(dataModel)
    }


    override fun renderData(appState: AppState) {
        model.setQuery(appState)
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_tittle_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {

                    setDataToAdapter(data)
                }
            }

            is AppState.Loading -> {
                showViewLoading()
                binding.apply {
                    if (appState.progress != null) {
                        progressBarRound.visibility = View.GONE
                    } else {
                        progressBarRound.visibility = View.VISIBLE
                    }
                }
            }

            is AppState.Error -> {
                showViewError()
                showAlertDialog(
                    getString(R.string.dialog_tittle_sorry),
                    getString(R.string.empty_server_response_on_success)
                )
            }
        }
    }


    private fun saveListForAdapter(dataModel: List<DataModel>) {
        var listFromSharedPreferences: List<DataModel> by SharedPreferencesDelegate(LIST_KEY)
        listFromSharedPreferences = dataModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE)
                as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchItem.apply {

            searchView.also {

                it.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {

                        if (isNetworkAvailable) {

                            query?.let { searchString ->
                                model.getData(
                                    searchString,
                                    isNetworkAvailable
                                )
                            }
                            it.clearFocus()
                            it.setQuery("", false)
                            collapseActionView()
                            Toast.makeText(
                                context,
                                resources.getString(R.string.looking_for) + " " + query ?: "",
                                Toast.LENGTH_LONG
                            ).show()
                            showViewLoading()
                        } else {
                            showNoInternetConnectionDialog()
                            showViewError()
                        }
                        return true
                    }

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.releaseMediaPlayer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                router.navigateTo(screen.startFavoriteFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showErrorScreen(error: String?) {

        showViewError()
        binding.apply {
            errorTextview.text = error ?: getString(R.string.undefined_error)
            reloadButton.setOnClickListener {
                model.getData("hi", true).observe(
                    viewLifecycleOwner,
                    observer
                )
            }
        }
    }

    private fun showViewWorking() {
        loadingFrameLayout.visibility = View.GONE
    }


    private fun showViewSuccess() {
        loadingFrameLayout.visibility = View.GONE
        binding.apply {
            successLinearLayout.visibility = View.VISIBLE
            errorLinearLayout.visibility = View.GONE
        }
    }

    override fun showViewLoading() {
        loadingFrameLayout.visibility = View.VISIBLE
        binding.apply {
            successLinearLayout.visibility = View.GONE
            errorLinearLayout.visibility = View.GONE
        }
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        dataWithFavorite = data
        saveListForAdapter(data)
        showViewSuccess()
        adapter?.setData(data)
    }


    private fun showViewError() {
        loadingFrameLayout.visibility = View.GONE
        binding.apply {
            successLinearLayout.visibility = View.GONE
            errorLinearLayout.visibility = View.VISIBLE
        }
    }


    companion object {
        fun newInstance() = MainFragment()

    }
}




