package com.sangik.iluvbook.quiz.main.ui

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentQuizMainBinding
import com.sangik.iluvbook.network.repository.QuizMainRepository
import com.sangik.iluvbook.network.repository.UserPreferenceRepository
import com.sangik.iluvbook.network.repository.UserProfileRepository
import com.sangik.iluvbook.quiz.main.viewmodel.QuizMainViewModel

class QuizMainFragment : Fragment() {
    private lateinit var quizMainViewModel : QuizMainViewModel
    private lateinit var binding : FragmentQuizMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizMainViewModel = QuizMainViewModel(
            UserPreferenceRepository(requireContext()),
            QuizMainRepository(),
            UserProfileRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_main, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.quizMainViewModel = quizMainViewModel

        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        // 로딩 상태 관찰
        quizMainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 로딩 중 처리
                showLoading()
            } else {
                // 로딩 종료 처리
                hideLoading()
            }
        }

        // 에러 메시지 관찰
        quizMainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // 사용자 프로필 이미지
        quizMainViewModel.userProfile.observe(viewLifecycleOwner) { imageUrl ->
            imageUrl?.let {
                fetchImage(it.imageUrl)
            }
        }
    }

    private fun hideLoading() {
        binding.quizLayout.visibility = View.VISIBLE
        binding.loadingProgress.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.quizLayout.visibility = View.INVISIBLE
        binding.loadingProgress.visibility = View.VISIBLE
    }


    private fun initRecyclerView() {
        setupAdapter()
        observeRecyclerViewData()
        setupInfiniteScroll()
    }

    // 무한 스크롤
    private fun setupInfiniteScroll() {
        binding.quizRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val threshold = 5 // 임계값

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // 로딩 중 x 마지막 페이지 x, 임계값 도달한 경우
                if (!quizMainViewModel.isLoading.value!! &&
                    !quizMainViewModel.isLastPage &&
                    lastVisibleItemPosition >= totalItemCount - threshold) {
                    quizMainViewModel.loadQuizItems()
                }
            }
        })
    }

    private fun setupAdapter() {
        val adapter = QuizMainAdapter(emptyList())
        binding.quizRecyclerview.adapter = adapter
        binding.quizRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRecyclerViewData() {
        quizMainViewModel.quizTotalItems.observe(viewLifecycleOwner) {
            (binding.quizRecyclerview.adapter as QuizMainAdapter).submitList(it)
        }
    }


    // 프로필 이미지
    private fun fetchImage(imageUrl : String) {
        Glide.with(requireActivity())
            .load(imageUrl)
            .apply(
                RequestOptions().transform(CenterCrop(), RoundedCorners(dpToPx(25)))
            )
            .into(binding.userProfileImg)
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()
    }
}