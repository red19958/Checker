package com.checker

import androidx.fragment.app.Fragment

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as MyApplication)