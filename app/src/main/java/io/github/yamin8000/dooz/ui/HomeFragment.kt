package io.github.yamin8000.dooz.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.dooz.databinding.FragmentHomeBinding
import io.github.yamin8000.dooz.ui.util.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}