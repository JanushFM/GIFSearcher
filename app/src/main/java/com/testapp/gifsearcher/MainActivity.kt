package com.testapp.gifsearcher

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this); // Fresco needs to be initialized before you call setContentView()
        setContentView(R.layout.activity_main)

        val draweeView = findViewById<SimpleDraweeView>(R.id.my_image_view)

        val uri: Uri =
            Uri.parse("https://media0.giphy.com/media/U5aTN7dX9aFrr2uuj8/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif")

        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build()
        draweeView.controller = controller
    }
}