package com.acalamaro.xkcdviewer.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.databinding.ActivityMainBinding
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    @Inject lateinit var searchFragment : SearchFragment
    @Inject lateinit var mainFragment : MainFragment

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            if(supportFragmentManager.fragments.isEmpty()) {
                add(binding.fragmentContainer.id, mainFragment)
            }
        }
    }

    /**
     * Very bad. Fragment should not directly reference (and typecast) activity.
     * Better solution would be interface at a minimum, or better yet lifecycle-aware
     * tool such as ViewModel.
     */
    fun openSearch() {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
            add(binding.fragmentContainer.id, searchFragment)
            addToBackStack("search")
        }
    }
}