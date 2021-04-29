package edu.ivytech.cnnreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import edu.ivytech.cnnreader.databinding.ActivityArticleWebBinding

class ArticleWebActivity : AppCompatActivity() {

    lateinit var binding:ActivityArticleWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.articleWebView.webViewClient = WebViewClient()
        val intent = intent
        val link:String? = intent.getStringExtra("link")
        binding.articleWebView.loadUrl(link!!)
    }
}