package com.application.nikestore.common

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.nikestore.R
import com.application.nikestore.feature.auth.AuthActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.IllegalStateException

interface NikeView {
    val rootView: CoordinatorLayout?
    val viewContext: Context?
    fun setProgressBarIndicator(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.baseViewLoading)
                if (loadingView == null && mustShow) {
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
                    it.addView(loadingView)
                }
                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }
    }

    fun showEmptyState(layoutResId: Int): View? {
        rootView?.let {
            viewContext?.let { context ->
                var emptyState = it.findViewById<View>(R.id.cartEmptyState)
                if (emptyState == null) {
                    emptyState = LayoutInflater.from(context).inflate(layoutResId, it, false)
                    it.addView(emptyState)
                }
                emptyState.visibility = View.VISIBLE
                return emptyState
            }
        }
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(nikeException: NikeException) {
        viewContext?.let {
            when (nikeException.type) {
                NikeException.Type.SIMPLE -> showSnackBar(
                    nikeException.serverMessage ?: it.getString(nikeException.userMessage)
                )
                NikeException.Type.AUT -> {
                    it.startActivity(Intent(it, AuthActivity::class.java))
                    Toast.makeText(it, nikeException.serverMessage, Toast.LENGTH_SHORT).show()
                }
                else -> throw IllegalStateException("invalid type")
            }
        }
    }

    fun showSnackBar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: View.OnClickListener? = null,
        actionTitle: String = ""
    ) {
        rootView?.let {
            Snackbar.make(it, message, duration).apply {
                setBackgroundTint(ContextCompat.getColor(viewContext!!, R.color.blue))
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .apply {
                        textSize = 14f
                        setTextColor(ContextCompat.getColor(viewContext!!, R.color.white))
                    }
                if (action != null && actionTitle.isNotEmpty()) {
                    setAction(actionTitle, action)
                    setActionTextColor(ContextCompat.getColor(viewContext!!, R.color.white))
                }
                show()
            }

        }
    }
}

abstract class NikeActivity : AppCompatActivity(), NikeView {
    override val viewContext: Context?
        get() = this
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("RootView must be instance of CoordinatorLayout")
            } else return viewGroup
        }


    override fun onStart() {
        EventBus.getDefault().register(this)

        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }

}

abstract class NikeFragment : Fragment(), NikeView {

    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }
}

abstract class NikeViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}