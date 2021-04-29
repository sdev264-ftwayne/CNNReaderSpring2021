package edu.ivytech.cnnreader

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.ivytech.cnnreader.databinding.FragmentArticleBinding
import java.util.*

private const val ARG_LINK = "link"
class ArticleFragment: Fragment() {
    private lateinit var article:Article
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    val newsDetailViewModel:NewsDetailViewModel by viewModels()

    companion object {
        fun newInstance(id:UUID) : ArticleFragment {
            val args = Bundle().apply{
                putSerializable(ARG_LINK, id)
            }
            return ArticleFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id : UUID = arguments?.getSerializable(ARG_LINK) as UUID
        newsDetailViewModel.loadArticle(id)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsDetailViewModel.articleLiveData.observe(viewLifecycleOwner,{
            article -> article?.let {
            binding.descriptionTextView.text = article.description
            binding.linkTextView.text = article.link
            binding.pubDateTextView.text = article.getPubDateFormatted()
            binding.titleTextView.text = article.title
            binding.linkTextView.setOnClickListener {
                var i = Intent(requireActivity(), ArticleWebActivity::class.java)
                i.putExtra(ARG_LINK, article.link)
                startActivity(i)
                }
            }
        })

    }
}