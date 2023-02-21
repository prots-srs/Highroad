package ua.com.biz_s.highroad.articles.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.com.biz_s.highroad.R
import ua.com.biz_s.highroad.articles.viewmodels.ArticleViewModel
import ua.com.biz_s.highroad.databinding.FragmentArticlesDetailBinding
import kotlin.math.absoluteValue

class ArticleDetailFragment : Fragment() {

    private val args: ArticleDetailFragmentArgs by navArgs()
    private val viewModel: ArticleViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, ArticleViewModel.Factory(activity.application)
        ).get(ArticleViewModel::class.java)
    }

    private var _binding: FragmentArticlesDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentArticlesDetailBinding.inflate(inflater, container, false)

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) {
                onNetworkError()
            }
        }

        viewModel.articleList.observe(viewLifecycleOwner) {
            viewModel.defineArticleItem(args.id)
        }

        viewModel.articleItem.observe(viewLifecycleOwner) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setTitle(it.title)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshItemFromRepository(args.id)

        val swipeItem = view.findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        swipeItem.setOnRefreshListener {
            viewModel.refreshItemFromRepository(args.id)
            swipeItem.setRefreshing(false);
        }

        val linearLayout = view.findViewById(R.id.linear) as LinearLayout
        val swipe = SwipeDetect()
        swipe.run(linearLayout, viewModel, args.id)
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

class SwipeDetect {
    private var startX: Float = 0.0F
    private var startY: Float = 0.0F
    private var moveToId: Int? = null

    @SuppressLint("ClickableViewAccessibility")
    fun run(view: View, viewModel: ArticleViewModel, currentId: Int) {
        view.setOnTouchListener { v, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = motionEvent.x
                    startY = motionEvent.y
                }
                MotionEvent.ACTION_MOVE -> {
                    var pathX = (motionEvent.x - startX) / v.width
                    var pathY = (motionEvent.y - startY) / v.height
                    if (pathX.absoluteValue > 0.4F && pathY.absoluteValue < 0.2F) {

                        if (pathX < 0) {
                            moveToId = viewModel.getNextId(currentId)
                            if (moveToId != null) {
                                v.findNavController().navigate(
                                    ArticleDetailFragmentDirections.actionArticlesDetailFragmentNext(
                                        moveToId!!
                                    )
                                )
                            }
                        } else {
                            moveToId = viewModel.getPrevId(currentId)
                            if (moveToId != null) {
                                v.findNavController().navigate(
                                    ArticleDetailFragmentDirections.actionArticlesDetailFragmentPrev(
                                        moveToId!!
                                    )
                                )
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {}
            }
            true
        }
    }
}