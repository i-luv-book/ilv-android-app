package com.sangik.iluvbook.fairytale.intro.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.sangik.iluvbook.BuildConfig
import com.sangik.iluvbook.R
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.Page
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class FairyTaleIntroFragmentTest {

    @Test
    fun testNavigationAndDataBinding() {
        // 테스트에 사용할 FairyTaleResponse 데이터 준비
        val fairyTaleResponse = FairyTaleResponse(
            title = "The Adventure of Sang-Ik at Moonsu Stadium",
            summary = "Sang-Ik, a boy passionate about sports and life, visits Moonsu Stadium and rescues Mr. Kim, the national soccer team's coach. Impressed by Sang-Ik's bravery and talent, Mr. Kim invites him to join a training session. Sang-Ik excels and learns valuable life lessons about perseverance and dedication.",
            pages = listOf(
                Page(
                    content = "Once upon a time, in the bustling city of Busan, there was a young boy named Sang-Ik...",
                    imgURL = BuildConfig.THUMBNAIL_TEST_IMG_URL
                ),
                Page(
                    content = "Without a second thought, Sang-Ik rushed to lend a hand. Using all his strength...",
                    imgURL = BuildConfig.THUMBNAIL_TEST_IMG_URL
                ),
                Page(
                    content = "Kim threw at him. The other players were fascinated by his talent and energy...",
                    imgURL = BuildConfig.THUMBNAIL_TEST_IMG_URL
                )
            )
        )


        // NavController 설정
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // FairyTaleIntroFragmentArgs 객체 생성
        val fragmentArgs = FairyTaleIntroFragmentArgs(
            fairyTaleResponse = fairyTaleResponse,
            keywords = arrayOf("동물", "공룡", "상익", "진", "놀이공원", "숲", "액션")
        ).toBundle()

        // Fragment를 launchFragmentInContainer로 시작하고 arguments 설정
        val scenario = launchFragmentInContainer<FairyTaleIntroFragment>(
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_ILuvBook
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // 제목이 UI에 표시 확인
        onView(withId(R.id.fairytale_intro_title))
            .check(matches(withText(fairyTaleResponse.title)))

        // 요약 UI 표시 확인
        onView(withId(R.id.fairytale_intro_summary))
            .check(matches(withText(fairyTaleResponse.summary)))

        // RecyclerView 아이템 UI 표시 확인
        val keywords = listOf("동물", "공룡", "상익", "진", "놀이공원", "숲", "액션")

        keywords.forEachIndexed { index, keyword ->
            // 해당 키워드가 있는 위치로 스크롤
            onView(withId(R.id.keywords_recyclerview))
                .perform(RecyclerViewActions.scrollToPosition<KeywordAdapter.KeywordViewHolder>(index))

            // 해당 키워드가 화면에 표시되는지 확인
            onView(withText(keyword)).check(matches(isDisplayed()))
        }

    }
}