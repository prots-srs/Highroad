package ua.com.biz_s.highroad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview

class MotionFragment : Fragment() {

//    private var _binding: FragmentMotionBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setContent {
//            MessageCard("Android")
//        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motion, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

//    @Composable
//    fun MessageCard(name: String) {
//        Text(text = "Hello $name!")
//    }

//    @Preview
//    @Composable
//    fun PreviewMessageCard() {
//        MessageCard("Android")
//    }
}