package com.sangik.iluvbook.fairytale.detail.ui

import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.sangik.iluvbook.BuildConfig
import com.sangik.iluvbook.R
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.Page
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@MediumTest
@RunWith(AndroidJUnit4::class)
class FairyTaleDetailFragmentTest {

    // 테스트에 사용할 FairyTaleResponse 데이터 
    private val fairyTaleResponse = FairyTaleResponse(
        title = "The Adventure of Sang-Ik at Moonsu Stadium",
        summary = "Sang-Ik, a boy passionate about sports and life, visits Moonsu Stadium...",
        pages = listOf(
            Page(
                content = "Test Content 1",
                imgURL = BuildConfig.THUMBNAIL_TEST_IMG_URL
            ),
            Page(
                content = "Test Content 2",
                imgURL = BuildConfig.FAIRYTALE_TEST_IMG_URL
            ),
            Page(
                content = "Test Content 3",
                imgURL = BuildConfig.FAIRYTALE_TEST_IMG_URL
            ),
            Page(
                content = "Test Content 4",
                imgURL = BuildConfig.THUMBNAIL_TEST_IMG_URL
            )

        )
    )

    private val fragmentArgs = FairyTaleDetailFragmentArgs(
        fairyTaleResponse = fairyTaleResponse
    ).toBundle()

    private val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Test
    fun testInitialPageDisplay() {
        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleDetailFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 첫 번째 페이지 내용이 UI에 표시되는지 확인
        onView(withId(R.id.page_content))
            .check(matches(withText(fairyTaleResponse.pages[0].content)))

    }

    // 페이지에 따른 버튼 활성화 테스트
    @Test
    fun testSwipeButtonVisibility(){
        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleDetailFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 첫 번째 페이지는 왼쪽 버튼이 보이면 안됨
        onView(withId(R.id.btn_left)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))

        // 마지막 페이지까지 넘길 횟수
        val swipeIteration = fairyTaleResponse.pages.size - 1

        repeat(swipeIteration) {
            onView(withId(R.id.btn_right)).perform(click())
        }
        // 마지막 페이지는 왼쪽 버튼이 보여야 함.
        onView(withId(R.id.btn_left)).check(matches(isDisplayed()))
    }

    @Test
    fun testViewPagerRightButton() {
        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleDetailFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 오른쪽 버튼을 클릭하여 다음 페이지로 이동
        onView(withId(R.id.btn_right)).perform(click())

        // 페이지 2로 이동했는지 확인
        onView(withId(R.id.page_content))
            .check(matches(withText(fairyTaleResponse.pages[1].content)))
    }

    @Test
    fun testViewPagerLeftButton() {
        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleDetailFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 오른쪽 버튼을 클릭하여 다음 페이지로 이동한 후, 다시 왼쪽 버튼으로 돌아오기
        onView(withId(R.id.btn_right)).perform(click())
        onView(withId(R.id.btn_left)).perform(click())

        // 다시 첫 번째 페이지로 돌아왔는지 확인
        onView(withId(R.id.page_content))
            .check(matches(withText(fairyTaleResponse.pages[0].content)))
    }

    @Test
    fun testTitleDisplayed() {
        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleDetailFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 타이틀이 올바르게 표시되는지 확인
        onView(withId(R.id.fairy_tale_page_title))
            .check(matches(withText(fairyTaleResponse.title)))
    }


}
