package com.hr.warzy.bela.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hr.warzy.bela.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseFragment<ViewState : Any>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    private var disposables = CompositeDisposable()

    abstract val model: BaseViewModel<ViewState>

    protected abstract fun render(viewState: ViewState)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true // To avoid clicks passing through
        initialiseView(view, savedInstanceState)
        model.viewStates().forEach { addDisposable(it.subscribe(this::render, Timber::e)) }

        initCollapsingToolbarLayoutTypeface(view)
    }

    private fun initCollapsingToolbarLayoutTypeface(view: View) =
        (view as ViewGroup).children.firstOrNull { it is AppBarLayout }?.apply {
            this as ViewGroup
            children.firstOrNull { it is CollapsingToolbarLayout }?.apply {
                this as CollapsingToolbarLayout
                setCollapsedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold))
                setExpandedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold))
            }
        }

    /** Override to initialise view */
    protected open fun initialiseView(view: View, savedInstanceState: Bundle?) {
        // Template
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}