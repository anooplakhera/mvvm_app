package com.app.mvvmtask.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar

fun Context.dismissKeyboard(view: View?) {
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun showToast(ctx: Context, message: String) {
    if (message != null) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ShapeableImageView, url: String?) {
    val context = imageView.context
    if (url != null)
        Glide.with(context).load(url).into(imageView)
}