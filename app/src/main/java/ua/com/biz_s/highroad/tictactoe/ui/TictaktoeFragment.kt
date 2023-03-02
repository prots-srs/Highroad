package ua.com.biz_s.highroad.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ua.com.biz_s.highroad.articles.viewmodels.ArticleViewModel
import ua.com.biz_s.highroad.databinding.FragmentTictaktoeBinding
import ua.com.biz_s.highroad.tictactoe.viewmodels.TictaktoeViewModel

class TictaktoeFragment : Fragment() {

    private val viewModel: TictaktoeViewModel by viewModels()
    /*private val viewModel: ArticleViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, ArticleViewModel.Factory(activity.application)
        ).get(ArticleViewModel::class.java)
    }*/

    private var _binding: FragmentTictaktoeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTictaktoeBinding.inflate(inflater, container, false)

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel
        return binding.root
    }
}