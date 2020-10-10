package com.yuvrajangula.memify

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }

    private fun loadmeme() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val url = response.getString("url")
                Glide.with(this).load(url).into(memeImageView)

            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun nextmeme(view: View) {
        loadmeme()
    }

    fun sharememe(view: View) {
        val image: Bitmap? = getBitmapFromView(memeImageView)
//    val intent = Intent(Intent.ACTION_SEND)
//        intent.type="text/plain"
//       intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme i got from Memify developed by YUVRAJ ANGULA $currentImageUrl")
//        val chooser = Intent.createChooser(intent,"Share this Meme using")
//        startActivity(chooser)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(this, image!!))
        startActivity(Intent.createChooser(share, "Share Meme using : "))
    }

    private fun getBitmapFromView(View: ImageView): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(View.width, View.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        View.draw(canvas)
        return bitmap
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Meme", null)
        return Uri.parse(path)
    }
}