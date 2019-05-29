package jp.co.pise.studyapp.presentation.view.fragment.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentCouponUseBinding
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.viewmodel.fragment.dialog.CouponUseFragmentViewModel

private const val COUPON = "coupon"
private const val SWIPE_MIN_DISTANCE = 50
private const val SWIPE_THRESHOLD_VELOCITY = 200

class CouponUseFragment : BaseDialogFragment() {

    private var viewModel: CouponUseFragmentViewModel? = null
    private var gestureDetector: GestureDetector? = null
    private var useCouponListener: UseCouponListener? = null

    private lateinit var binding: FragmentCouponUseBinding
    private var isAnimation = false

    interface UseCouponListener {
        fun onUseCoupon(model: GetCouponItemModel)
    }

    companion object {
        const val TAG = "CouponUseFragment"

        fun newInstance(model: GetCouponItemModel): CouponUseFragment {
            val fragment = CouponUseFragment()
            val args = Bundle()
            args.putSerializable(COUPON, model)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is UseCouponListener) {
            this.useCouponListener = context
        } else {
            this.useCouponListener = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val coupon = arguments!!.getSerializable(COUPON) as GetCouponItemModel?
            if (coupon != null) {
                this.viewModel = CouponUseFragmentViewModel.fromResultItem(coupon)
                this.viewModel!!.onClose.observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ dismiss() }, { }).addBug(this.subscriptions)
            } else {
                this.viewModel?.dispose()
                this.viewModel = null
            }
        } else {
            this.viewModel?.dispose()
            this.viewModel = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil
                .inflate<FragmentCouponUseBinding>(inflater, R.layout.fragment_coupon_use, container, false)
                .owner(this)

        this.viewModel?.let {
            this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)

            it.imageUrl.replaceObserve(this, Observer { imageUrl -> setImage(imageUrl) })
            this.binding.viewModel = it
            it.addBug(this.subscriptions)

            this.gestureDetector = GestureDetector(this.binding.parent.context, gestureListener)
            this.binding.parent.setOnTouchListener { _, motionEvent -> gestureDetector?.onTouchEvent(motionEvent) ?: true }

            this.isCancelable = false
        }

        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setImage(imageUrl: String?) {
        try {
            if (!TextUtils.isEmpty(imageUrl)) {
                this.binding.image.resizeFromDimen(
                        imageUrl,
                        R.dimen.coupon_use_image_width,
                        R.dimen.coupon_use_image_height)
                this.binding.image.visibility = View.VISIBLE
            } else {
                this.binding.image.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            this.binding.image.visibility = View.INVISIBLE
        }
    }

    // タッチイベントのリスナー
    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        // フリックイベント
        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            try {
                // 上へ移動
                if (event1.y > event2.y) {
                    // 移動距離・スピードを出力
                    val disY = event1.y - event2.y
                    val velY = Math.abs(velocityY)

                    // 移動距離・スピードが規定値を超えていれば
                    if (!isAnimation && disY > SWIPE_MIN_DISTANCE && velY > SWIPE_THRESHOLD_VELOCITY) {
                        isAnimation = true
                        val animation = AnimationUtils.loadAnimation(activity, R.anim.coupon_use)
                        animation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}

                            override fun onAnimationEnd(animation: Animation) {
                                if (viewModel != null)
                                    useCouponListener?.onUseCoupon(viewModel!!.toItemModel())
                                dismiss()
                            }

                            override fun onAnimationRepeat(animation: Animation) {
                            }
                        })
                        binding.content.startAnimation(animation)
                    }
                }
            } catch (e: Exception) {
                dismiss()
            }
            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }
}