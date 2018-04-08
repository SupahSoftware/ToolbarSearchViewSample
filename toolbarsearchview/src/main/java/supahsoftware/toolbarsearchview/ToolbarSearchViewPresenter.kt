package supahsoftware.toolbarsearchview

class ToolbarSearchViewPresenter {

    private var mView: View? = null
    private var mSearchState = SearchState.CLOSED
    private var mLastKnownSearchQuery = ""

    fun attachView(view: View) {
        mView = view
    }

    fun detachView() {
        mView = null
    }

    fun openSearchClicked() {
        if (mSearchState == SearchState.CLOSED) {
            mSearchState = SearchState.ANIMATING
            mView?.startOpenAnimation()
            mView?.showKeyboard()
            mView?.clearSearchText()
            mView?.focusEditText()
            mView?.notifySearchOpened()
        }
    }

    fun closeSearchClicked() {
        if (mSearchState == SearchState.OPEN) {
            mSearchState = SearchState.ANIMATING
            mView?.startCloseAnimation()
            mView?.hideKeyboard()
            mView?.clearSearchText()
            mView?.unfocusEditText()
            mView?.notifySearchClosed()
        }
    }

    fun setSearchState(state: SearchState) {
        mSearchState = state
    }

    fun executeSearchClicked() {
        mView?.notifySearchExecuted()
    }

    fun searchQueryUpdated(query: String) {
        if (mLastKnownSearchQuery != query) {
            mLastKnownSearchQuery = query
            mView?.notifyTextChanged(query)
        }
    }

    interface View {
        fun notifyTextChanged(query: String)
        fun startCloseAnimation()
        fun startOpenAnimation()
        fun clearSearchText()
        fun notifySearchExecuted()
        fun showKeyboard()
        fun hideKeyboard()
        fun focusEditText()
        fun unfocusEditText()
        fun notifySearchOpened()
        fun notifySearchClosed()
    }

    enum class SearchState { OPEN, CLOSED, ANIMATING }
}