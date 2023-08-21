package com.diplomproject.view.test_english

import android.animation.Animator
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.diplomproject.R
import com.diplomproject.databinding.FragmentTestEnglishBinding
import com.diplomproject.di.ConnectKoinModules.testEnglishFragmentScreenScope
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.test_english_request.TestEnglishAppState
import com.diplomproject.utils.ui.viewById
import com.diplomproject.view.AnimatorTranslator
import com.diplomproject.view.OnlineRepository
import com.diplomproject.view.settings_menu.BaseFragmentSettingsMenu
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import java.io.IOException
import java.lang.reflect.Type


class TestEnglishFragment() :
    BaseFragmentSettingsMenu<FragmentTestEnglishBinding>(FragmentTestEnglishBinding::inflate) {

    private var snack: Snackbar? = null
    protected var isNetworkAvailable: Boolean = false
    private val checkConnection: OnlineRepository by inject()
    private val descriptionFragmentRecyclerview by viewById<RecyclerView>(R.id.description_recyclerview)
    lateinit var model: TestEnglishViewModel
    private val observer = Observer<TestEnglishAppState> { renderData(it) }

    private val adapter: TestEnglishFragmentAdapter by lazy { TestEnglishFragmentAdapter(::onPlayClick) }


    private fun onPlayClick(url: String) {
        playContentUrl(url)
    }

    fun readData(listDataModelsGson: String): List<DataModel> {
        val type: Type = object : TypeToken<List<DataModel?>?>() {}.type
        return Gson().fromJson(listDataModelsGson, type)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        showError()
        initViewModel()
        initViews()
        setData()
    }

    private fun initViewModel() {
        if (descriptionFragmentRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: TestEnglishViewModel by lazy { testEnglishFragmentScreenScope.get() }
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner, observer)
    }

    private fun setData() {
        val listDataModelsGson =
            (arguments?.getString(LIST_DATA_MODELS) as String?) ?: "[]"
        val listDataModels = readData(listDataModelsGson)
        if (isNetworkAvailable) {
            listDataModels?.let { model.getDataForTests(it, true) }
        }

    }

    fun renderData(testEnglishAppState: TestEnglishAppState) {
        model.setQuery(testEnglishAppState)
        when (testEnglishAppState) {
            is TestEnglishAppState.Success -> {
                binding.progressBarRoundDescription.visibility = View.GONE
                val data = testEnglishAppState.data
                if (data.isNullOrEmpty()) {
                    Toast.makeText(context, getString(R.string.example_absent), Toast.LENGTH_LONG)
                } else {

                    updateAdapter(data)
                }
            }

            is TestEnglishAppState.Loading -> {
                binding.progressBarRoundDescription.visibility = View.VISIBLE
            }

            is TestEnglishAppState.Error -> {
                binding.progressBarRoundDescription.visibility = View.GONE
                Toast.makeText(context, getString(R.string.example_absent), Toast.LENGTH_LONG)
            }
        }
    }

    fun updateMainTestEnglishFragment(example: Example) {
        val questionString = example.text
        var squareBrackets: MutableList<Pair<Int, Int>> = mutableListOf()
        questionString?.let {
            val leftSquareBrackets: List<Int> = Regex("[".toString()).findAll(questionString)
                .map { it.range.first }
                .toList()
            val rightSquareBrackets = Regex("]".toString()).findAll(questionString)
                .map { it.range.first }
                .toList()
            val minSize = minOf(leftSquareBrackets.size, rightSquareBrackets.size)
            for (i in 0 until minSize) {
                squareBrackets.add(Pair(leftSquareBrackets[i], rightSquareBrackets[i]))
            }
        }
        binding.apply {
            var transformQuestionString = questionString
            if (squareBrackets.isEmpty()) {
                testEnglishHeader.text = getString(R.string.find_word_for_sense)
            } else {
                testEnglishHeader.text = getString(R.string.find_exclude_word)
                for (brackets in squareBrackets) {
                    transformQuestionString?.replaceRange(
                        brackets.first + 1 until brackets.second,
                        "...."
                    )
                }
            }
            sentence.text = transformQuestionString ?: ""

            descriptionHeader.text = currentDataModel.text
            if (currentDataModel.meanings?.size != 0) {
                descriptionPartOfSpeech.text =
                    currentDataModel.meanings?.get(0)?.partOfSpeechCode
                translationTextview.text =
                    currentDataModel.meanings?.get(0)?.translation?.translation
                transcription.text =
                    "[" + currentDataModel.meanings?.get(0)?.transcription + "]"

                playArticulation.setOnClickListener {
                    it?.apply {
                        isEnabled = false
                        postDelayed({ isEnabled = true }, 400)
                    }
                    currentDataModel.meanings?.get(0)?.soundUrl?.let { sound_url ->
                        playContentUrl(sound_url)
                    }
                }
                val imageLink = currentDataModel.meanings?.get(0)?.imageUrl
                if (imageLink.isNullOrBlank()) {
                    //    stopRefreshAnimationIfNeeded()
                } else {
                    useGlideToLoadPhoto(descriptionImageview, imageLink)
                }
            }
        }

    }


    private fun updateAdapter(examples: List<Example>?) {
        adapter?.setData(examples)
    }

    private fun initViews() {
        descriptionFragmentRecyclerview.layoutManager =
            LinearLayoutManager(context)
        descriptionFragmentRecyclerview.adapter = adapter
    }

    private fun showError() {
        checkConnection.observe(
            viewLifecycleOwner
        ) {
            isNetworkAvailable = it
            if (isNetworkAvailable) {
                snack?.dismiss()
                snack = null
                setData()
            } else {
                snack = Snackbar.make(
                    requireView(),
                    R.string.dialog_message_device_is_offline, Snackbar.LENGTH_INDEFINITE
                )
                snack?.show()
                //  stopRefreshAnimationIfNeeded()
            }
        }
        checkConnection.currentStatus()
    }


    fun playContentUrl(url: String) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            try {
                setDataSource(url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener { start() }
                prepareAsync()
            } catch (exception: IOException) {
                release()
                null
            }
        }
    }

    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
        Glide.with(this)
            .load("https:$imageLink")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    //   stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_load_error_vector)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    //  stopRefreshAnimationIfNeeded()
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .transform(CircleCrop())
            .into(imageView)
    }


    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorTranslator().setAnimator(transit, enter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMediaPlayer?.apply {
            if (isPlaying == true) {
                stop()
                release()
                mMediaPlayer = null
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle): TestEnglishFragment {
            val fragment = TestEnglishFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val LIST_DATA_MODELS = "list_data_models"
    }
}
