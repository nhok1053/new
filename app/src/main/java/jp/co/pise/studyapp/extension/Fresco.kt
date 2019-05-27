package jp.co.pise.studyapp.extension

import android.net.Uri
import android.support.annotation.DimenRes
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import jp.co.pise.studyapp.presentation.StudyApp

@Throws(Exception::class)
fun SimpleDraweeView.resizeFromDimen(uriStr: String?, @DimenRes widthId: Int, @DimenRes heightId: Int) {
    val width = StudyApp.instance.resources.getDimension(widthId).toInt()
    val height = StudyApp.instance.resources.getDimension(heightId).toInt()
    resize(uriStr, width, height)
}

@Throws(Exception::class)
fun SimpleDraweeView.resize(uriStr: String?, width: Int, height: Int) {
    val uri = Uri.parse(uriStr)
    val options = ResizeOptions(width, height)
    val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setResizeOptions(options)
            .build()
    this.setImageRequest(request)
}