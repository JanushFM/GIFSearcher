package com.testapp.gifsearcher.ui

import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.testapp.gifsearcher.R


class DisplayBigGifDialog() : DialogFragment() {
    private  lateinit var gifURI: Uri
    private  lateinit var gifTitle: String
    private var aspectRatio: Float = 0f

    constructor(gifURI: Uri,
                gifTitle: String,
                aspectRatio: Float) : this() {
        this.gifURI=gifURI
        this.gifTitle =gifTitle
        this.aspectRatio = aspectRatio
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.big_gif_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.gif_title_TextView).text = gifTitle
        val progressBar:ProgressBar = view.findViewById(R.id.big_gif_ProgressBar)

        val controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(
                id: String?,
                imageInfo: ImageInfo?,
                animatable: Animatable?
            ) {
                progressBar.visibility = View.GONE
            }
        }

        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(gifURI)
            .setControllerListener(controllerListener)
            .setAutoPlayAnimations(true)
            .build()

        val gifView = view.findViewById<SimpleDraweeView>(R.id.big_gif_DraweeView)
        gifView.aspectRatio = aspectRatio
        gifView.controller = controller
    }
}