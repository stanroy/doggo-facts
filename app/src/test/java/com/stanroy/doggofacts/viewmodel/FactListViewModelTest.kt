package com.stanroy.doggofacts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.stanroy.MainCoroutineRule
import com.stanroy.doggofacts.model.mock.FakeDoggoRepository
import com.stanroy.doggofacts.model.mock.FakeNetworkCheck
import com.stanroy.doggofacts.model.utils.FactListStatus.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class FactListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FactListViewModel
    private lateinit var fakeRepository: FakeDoggoRepository
    private lateinit var fakeNetworkCheck: FakeNetworkCheck


    @Before
    fun setUp() {
        fakeRepository = FakeDoggoRepository()
        fakeNetworkCheck = FakeNetworkCheck()
        viewModel = FactListViewModel(fakeRepository, fakeNetworkCheck)
    }

    @Test
    fun test_makeRepositoryCall_returnsError() {
        fakeRepository.setShouldReturnNetworkError(true)

        viewModel.onRefreshClicked()

        val value = viewModel.status
        assertThat(value.value?.first).isEqualTo(ERROR)
    }

    @Test
    fun test_makeRepositoryCall_returnsNoInternetConnectionError() {
        fakeNetworkCheck.setShouldUserBeOnline(false)

        viewModel.onRefreshClicked()

        val value = viewModel.status
        assertThat(value.value?.first).isEqualTo(NO_INTERNET_CONNECTION)
    }

    @Test
    fun test_makeRepositoryCall_returnsSuccessStatus() {
        fakeNetworkCheck.setShouldUserBeOnline(true)

        viewModel.onRefreshClicked()

        val value = viewModel.status
        assertThat(value.value?.first).isEqualTo(SUCCESS)
    }

    @Test
    fun test_makeRepositoryCall_returnsSuccessStatus_and_factList() {
        fakeRepository.clearFactList()
        fakeNetworkCheck.setShouldUserBeOnline(true)


        viewModel.onRefreshClicked()

        val status = viewModel.status.value
        val factList = viewModel.storedFacts.value

        assertThat(status?.first).isEqualTo(SUCCESS)
        assertThat(factList).isNotEmpty()
        assertThat(factList).hasSize(30)
    }

    @Test
    fun test_makeRepositoryCall_noInternetConnection_connectToInternet_returnsSuccessStatus() {
        fakeNetworkCheck.setShouldUserBeOnline(false)

        viewModel.onRefreshClicked()
        val status = viewModel.status.value

        assertThat(status?.first).isEqualTo(NO_INTERNET_CONNECTION)

        fakeNetworkCheck.setShouldUserBeOnline(true)
        viewModel.onReloadClicked()

        val newStatus = viewModel.status.value

        assertThat(newStatus?.first).isEqualTo(SUCCESS)
    }
}