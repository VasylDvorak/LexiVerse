package com.diplomproject.view.description

import android.animation.Animator
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.diplomproject.databinding.FragmentDescriptionBinding
import com.diplomproject.di.ConnectKoinModules.descriptionScreenScope
import com.diplomproject.domain.base.BaseFragment
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_description_request.DescriptionAppState
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.utils.network.OnlineRepository
import com.diplomproject.utils.ui.AlertDialogFragment
import com.diplomproject.utils.ui.viewById
import com.diplomproject.view.AnimatorTranslator
import com.diplomproject.view.settings_menu.BaseFragmentSettingsMenu
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import java.io.IOException


class DescriptionFragment : BaseFragmentSettingsMenu() {

    private var snack: Snackbar? = null
    protected var isNetworkAvailable: Boolean = false
    private val checkConnection: OnlineRepository by inject()
    private val descriptionFragmentRecyclerview by viewById<RecyclerView>(R.id.description_recyclerview)
    var mMediaPlayer: MediaPlayer? = null
    lateinit var model: DescriptionViewModel
    private val observer = Observer<DescriptionAppState> { renderData(it) }
    private var _binding: FragmentDescriptionBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: DiscriptionFragmentAdapter by lazy { DiscriptionFragmentAdapter(::onPlayClick) }


    private fun onPlayClick(url: String) {
        playContentUrl(url)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root

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
        val viewModel: DescriptionViewModel by lazy { descriptionScreenScope.get() }
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner, observer)
    }

    private fun setData() {
        val currentDataModel =
            (arguments?.getParcelable(CURRENT_DATA_MODEl) as DataModel?) ?: DataModel()
        currentDataModel.meanings?.let { model.getDataDescription(it, true) }

        binding.apply {
            descriptionHeader.text = currentDataModel.text
            if (currentDataModel.meanings?.size != 0) {
                translationTextview.text =
                    currentDataModel.meanings?.get(0)?.translation?.translation
                transcription.text =
                    "[" + currentDataModel.meanings?.get(0)?.transcription + "]"

                playArticulation.setOnClickListener {
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

    fun renderData(descriptionAppState: DescriptionAppState) {
        model.setQuery(descriptionAppState)
        when (descriptionAppState) {
            is DescriptionAppState.Success -> {
                binding.progressBarRoundDescription.visibility = View.GONE
                val data = descriptionAppState.data
                if (data.isNullOrEmpty()) {
                    Toast.makeText(context, getString(R.string.example_absent), Toast.LENGTH_LONG)
                } else {

                    updateAdapter(data)
                }
            }

            is DescriptionAppState.Loading -> {
                binding.progressBarRoundDescription.visibility = View.VISIBLE
            }

            is DescriptionAppState.Error -> {
                binding.progressBarRoundDescription.visibility = View.GONE
                Toast.makeText(context, getString(R.string.example_absent), Toast.LENGTH_LONG)
            }
        }
    }


    private fun updateAdapter(examples: List<Example>?) {
        //  showViewSuccess()
        adapter?.setData(examples)
    }

    private fun initViews() {
        descriptionFragmentRecyclerview.layoutManager =
            LinearLayoutManager(context)
        descriptionFragmentRecyclerview.adapter = adapter
    }

    private fun showError() {
        checkConnection.observe(
            viewLifecycleOwner,
            {
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
            })
        checkConnection.currentStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
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

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        activity?.let {
            AlertDialogFragment.newInstance(title, message)
                .show(it.supportFragmentManager, BaseFragment.DIALOG_FRAGMENT_TAG)
        }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorTranslator().setAnimator(transit, enter)
    }

    companion object {

        fun newInstance(bundle: Bundle): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val CURRENT_DATA_MODEl = "current_data_model"
    }
}