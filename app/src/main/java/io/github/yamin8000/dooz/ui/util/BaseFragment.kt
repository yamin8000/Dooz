package io.github.yamin8000.dooz.ui.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflater<VB> = (LayoutInflater) -> VB

/**
 * Base fragment
 *
 * @param VB view binding
 *
 * @param inflater layout inflater lambda
 */
abstract class BaseFragment<VB : ViewBinding>(inflater: Inflater<VB>) : Fragment() {

    protected val binding: VB by lazy(LazyThreadSafetyMode.NONE) { inflater(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): View = binding.root
}