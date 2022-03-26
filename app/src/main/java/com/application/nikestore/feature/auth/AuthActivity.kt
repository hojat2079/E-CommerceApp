package com.application.nikestore.feature.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.nikestore.R
import com.application.nikestore.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var bind: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(bind.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, LoginFragment())
            commit()
        }
    }
}