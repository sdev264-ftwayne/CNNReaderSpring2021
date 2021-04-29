package edu.ivytech.cnnreader

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ivytech.cnnreader.databinding.FragmentNewsListBinding
import edu.ivytech.cnnreader.databinding.ListItemBinding
import java.util.*

class NewsListFragment: Fragment() {
    interface Callbacks {
       fun onArticleSelected(id:UUID)
    }

    private var callbacks:Callbacks? = null
    private val newsListViewModel:NewsListViewModel by viewModels()

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!
    private var adapter:NewsAdapter? = null

    companion object {
        fun newInstance(): NewsListFragment {
            return NewsListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }
    private fun updateUI() {
        val articlesObserver = Observer<List<Article>>{ articles ->
            adapter = NewsAdapter(articles)
            binding.newsRecyclerView.adapter = adapter
        }
        newsListViewModel.articlesLiveData.observe(viewLifecycleOwner,articlesObserver)

    }

    private inner class NewsHolder(itemBinding: ListItemBinding) :RecyclerView.ViewHolder(itemBinding.root),View.OnClickListener {
        private lateinit var article: Article
        val titleTextView :TextView = itemBinding.titleTextView
        val dateTextView :TextView = itemBinding.pubDateTextView
        init{
            itemBinding.root.setOnClickListener(this)
        }
        fun bind(article: Article) {
            this.article = article
            titleTextView.text = article.title
            dateTextView.text = article.getPubDateFormatted()
        }

        override fun onClick(v: View?) {
            callbacks?.onArticleSelected(article.id)
        }
    }

    private inner class NewsAdapter(var articles:List<Article>) : RecyclerView.Adapter<NewsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
            val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NewsHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: NewsHolder, position: Int) {
            val article = articles[position]
            holder.bind(article)
        }

        override fun getItemCount(): Int {
            return articles.size
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}