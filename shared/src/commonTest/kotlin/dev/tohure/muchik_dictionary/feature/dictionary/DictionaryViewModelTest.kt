package dev.tohure.muchik_dictionary.feature.dictionary

import dev.tohure.muchik_dictionary.feature.dictionary.data.repository.DictionaryRepositoryImpl
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordCategory
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.GetCategoryCountsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.domain.usecase.SearchWordsUseCase
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.state.DictionaryViewMode
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.viewmodel.DictionaryViewModel
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncRepository
import dev.tohure.muchik_dictionary.feature.sync.domain.repository.SyncResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakeSyncRepository : SyncRepository {
    override fun syncFlow(): Flow<SyncResult> = flowOf(SyncResult.HasLocalData)

    override fun deltaSyncFlow(): Flow<SyncResult> = flowOf(SyncResult.Done(0))
}

@OptIn(ExperimentalCoroutinesApi::class)
class DictionaryViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DictionaryViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val repo = DictionaryRepositoryImpl()
        viewModel =
            DictionaryViewModel(
                searchWords = SearchWordsUseCase(repo),
                getCategoryCounts = GetCategoryCountsUseCase(repo),
                syncRepository = FakeSyncRepository(),
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has loading true`() {
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `after idle state loads entries`() =
        runTest {
            advanceUntilIdle()
            assertFalse(viewModel.uiState.value.isLoading)
            assertTrue(
                viewModel.uiState.value.entries
                    .isNotEmpty()
            )
        }

    @Test
    fun `category counts are loaded`() =
        runTest {
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.totalCount > 0)
            assertTrue(
                viewModel.uiState.value.categoryCounts
                    .isNotEmpty()
            )
        }

    @Test
    fun `onQueryChange filters entries`() =
        runTest {
            advanceUntilIdle()
            viewModel.onQueryChange("Apuk")
            advanceUntilIdle()
            val state = viewModel.uiState.value
            assertEquals("Apuk", state.query)
            assertTrue(state.entries.any { it.muchikTerm.contains("Apuk", ignoreCase = true) })
        }

    @Test
    fun `onCategorySelected filters by category`() =
        runTest {
            advanceUntilIdle()
            viewModel.onCategorySelected(WordCategory.TIEMPO)
            advanceUntilIdle()
            val state = viewModel.uiState.value
            assertEquals(WordCategory.TIEMPO, state.selectedCategory)
            assertTrue(state.entries.all { it.category == "Tiempo" })
        }

    @Test
    fun `onViewModeToggle switches between CARDS and LIST`() {
        assertEquals(DictionaryViewMode.CARDS, viewModel.uiState.value.viewMode)
        viewModel.onViewModeToggle()
        assertEquals(DictionaryViewMode.LIST, viewModel.uiState.value.viewMode)
        viewModel.onViewModeToggle()
        assertEquals(DictionaryViewMode.CARDS, viewModel.uiState.value.viewMode)
    }
}
