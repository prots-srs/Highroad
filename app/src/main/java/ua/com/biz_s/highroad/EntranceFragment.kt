package ua.com.biz_s.highroad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ua.com.biz_s.highroad.databinding.FragmentEntranceBinding

class EntranceFragment : Fragment() {

    private var _binding: FragmentEntranceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntranceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.picMotion.setOnClickListener {
            view.findNavController().navigate(EntranceFragmentDirections.actionEntranceFragmentToMotionFragment())
        }
        binding.picArticle.setOnClickListener {
            view.findNavController().navigate(EntranceFragmentDirections.actionEntranceFragmentToArticlesFragment())
        }
        binding.picTictaktoe.setOnClickListener {
            view.findNavController().navigate(EntranceFragmentDirections.actionEntranceFragmentToTictaktoeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}