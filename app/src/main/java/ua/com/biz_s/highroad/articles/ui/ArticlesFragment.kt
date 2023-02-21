package ua.com.biz_s.highroad.articles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.biz_s.highroad.R
import ua.com.biz_s.highroad.databinding.FragmentArticlesBinding
//import ua.com.biz_s.highroad.articles.network.ApiStatus
//import ua.com.biz_s.highroad.articles.domain.ListViewModel
import ua.com.biz_s.highroad.HighroadApplication
import ua.com.biz_s.highroad.articles.domain.Article
import ua.com.biz_s.highroad.articles.viewmodels.ArticleViewModel
//import ua.com.biz_s.highroad.articles.domain.ListViewModelFactory
import ua.com.biz_s.highroad.articles.util.ListItemAdapter

class ArticlesFragment : Fragment() {

    private val viewModel: ArticleViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            ArticleViewModel.Factory(activity.application)
        ).get(ArticleViewModel::class.java)
    }

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private var viewModelAdapter: ListItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
//        binding.lifecycleOwner = this
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel

        viewModelAdapter = ListItemAdapter(ListItemAdapter.Clicker {
            if (it != null) {
                this.findNavController().navigate(
                    ArticlesFragmentDirections.actionArticlesFragmentToArticlesDetailFragment(it.id)
                )
            }
        })
        binding.list.adapter = viewModelAdapter

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) {
                onNetworkError()
            }
        }

        viewModel.articleList.observe(viewLifecycleOwner) {
            it.let {
                viewModelAdapter?.submitList(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshListFromRepository()

        /*viewModel.articleList.observe(viewLifecycleOwner) {items ->
            items?.apply {
                viewModelAdapter?.articles = items
            }
        }*/

        val swipeList = view.findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        swipeList.setOnRefreshListener {
            viewModel.refreshListFromRepository()
            swipeList.setRefreshing(false);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}