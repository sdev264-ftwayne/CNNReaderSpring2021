package edu.ivytech.cnnreader

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import edu.ivytech.cnnreader.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), NewsListFragment.Callbacks {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            val fragment = NewsListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

    }

    override fun onArticleSelected(id: UUID) {
        val fragment = ArticleFragment.newInstance(id)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_refresh -> {
                val refresh = OneTimeWorkRequest.from(DownloadWorker::class.java)
                WorkManager.getInstance(this).enqueue(refresh)
                return true
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else-> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newIntent(context: Context) : Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}