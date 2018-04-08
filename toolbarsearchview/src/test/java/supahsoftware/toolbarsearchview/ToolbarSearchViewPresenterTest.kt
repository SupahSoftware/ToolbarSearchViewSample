package supahsoftware.toolbarsearchview

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Before
import org.junit.Test

class ToolbarSearchViewPresenterTest {

    private var testObject = ToolbarSearchViewPresenter()
    private val view: ToolbarSearchViewPresenter.View = mock()

    @Before
    fun setUp() {
        testObject = ToolbarSearchViewPresenter()
    }

    @Test
    fun attemptingToOpenWhenAlreadyOpenDoesNothing() {
        testObject.attachView(view)
        testObject.setSearchState(ToolbarSearchViewPresenter.SearchState.OPEN)
        testObject.openSearchClicked()

        verifyZeroInteractions(view)
    }

    @Test
    fun openSearchShouldOpenSearch() {
        testObject.attachView(view)

        testObject.openSearchClicked()
        verify(view).startOpenAnimation()
        verify(view).showKeyboard()
        verify(view).clearSearchText()
        verify(view).focusEditText()
        verify(view).notifySearchOpened()

        testObject.openSearchClicked()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun attemptingToCloseWhenAlreadyClosedDoesNothing() {
        testObject.attachView(view)
        testObject.closeSearchClicked()

        verifyZeroInteractions(view)
    }

    @Test
    fun closeSearchShouldCloseSearch() {
        testObject.attachView(view)
        testObject.setSearchState(ToolbarSearchViewPresenter.SearchState.OPEN)
        testObject.closeSearchClicked()
        verify(view).startCloseAnimation()
        verify(view).hideKeyboard()
        verify(view).clearSearchText()
        verify(view).unfocusEditText()
        verify(view).notifySearchClosed()

        testObject.closeSearchClicked()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun executingSearchShouldNotifyTheView() {
        testObject.attachView(view)
        testObject.executeSearchClicked()

        verify(view).notifySearchExecuted()
    }

    @Test
    fun lastKnownSearchQueryIsProperlyUpdated() {
        val searchQuery1 = "searchQuery1"
        testObject.attachView(view)
        testObject.searchQueryUpdated(searchQuery1)
        verify(view).notifyTextChanged(searchQuery1)

        testObject.searchQueryUpdated(searchQuery1)
        verifyNoMoreInteractions(view)

        val searchQuery2 = "searchQuery2"
        testObject.searchQueryUpdated(searchQuery2)
        verify(view).notifyTextChanged(searchQuery2)

    }
}