package supahsoftware.toolbarsearchview

import android.annotation.SuppressLint
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_toolbar_search.view.*

@SuppressLint("ViewConstructor")
class ToolbarSearchView(
        activity: AppCompatActivity,
        activityLayout: ViewGroup,
        actionBarTitle: String
) : RelativeLayout(activity), ToolbarSearchViewPresenter.View {

    private var onEventListener: OnEventListener? = null
    private var searchText: String
        get() = search_edit_text.text.toString()
        set(value) = search_edit_text.setText(value)

    private val presenter = ToolbarSearchViewPresenter()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_toolbar_search, this)

        presenter.attachView(this)

        val toolbar = Toolbar(activity)
        toolbar.setContentInsetsRelative(0, 0)

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 56.asDp())
        toolbar.layoutParams = layoutParams
        toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        toolbar.elevation = 6f

        activityLayout.addView(toolbar)
        activity.setSupportActionBar(toolbar)

        open_search_button.setOnClickListener { presenter.openSearchClicked() }
        close_search_button.setOnClickListener { presenter.closeSearchClicked() }
        execute_search_button.setOnClickListener { presenter.executeSearchClicked() }
        search_edit_text.setOnEditorActionListener(SearchActionListener({ presenter.executeSearchClicked() }))

        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable) = presenter.searchQueryUpdated(s.toString())
        })

        user_search_screen.setOnKeyListener(this::onBackListener)

        toolbar_title.text = actionBarTitle
        activity.supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowCustomEnabled(true)
            customView = this@ToolbarSearchView
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detachView()
    }

    fun setOnEventListener(listener: OnEventListener) {
        onEventListener = listener
    }

    fun setPrimaryColor(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        closed_search_container.setBackgroundColor(color)
        close_search_button.setColorFilter(color)
        execute_search_button.setColorFilter(color)
        search_edit_text.setTextColor(color)
        search_edit_text.setHintTextColor(color)
        open_search_button.setBackgroundColor(color)
    }

    fun setSecondaryColor(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        opened_search_container.setBackgroundColor(color)
        toolbar_title.setTextColor(color)
        open_search_button.setColorFilter(color)
    }

    private fun onBackListener(view: View, keyCode: Int, event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.closeSearchClicked()
            true
        } else false
    }

    override fun focusEditText() {
        search_edit_text.isFocusableInTouchMode = true
        search_edit_text.requestFocus()
        search_edit_text.setOnKeyListener(this@ToolbarSearchView::onBackListener)
    }

    override fun unfocusEditText() {
        search_edit_text.isFocusableInTouchMode = false
        search_edit_text.clearFocus()
    }

    override fun clearSearchText() {
        searchText = ""
    }

    override fun startOpenAnimation() {
        opened_search_container.visible()

        val revealAnim = ViewAnimationUtils.createCircularReveal(
                opened_search_container,
                opened_search_container.right - 28.asDp(),
                opened_search_container.bottom / 2,
                0f, opened_search_container.width.toFloat())

        revealAnim.duration = 500
        revealAnim.start()
        revealAnim.addListener(AnimatorEndListener({ presenter.setSearchState(ToolbarSearchViewPresenter.SearchState.OPEN) }))
    }

    override fun startCloseAnimation() {
        val revealAnim = ViewAnimationUtils.createCircularReveal(
                opened_search_container,
                opened_search_container.right - 28.asDp(),
                opened_search_container.bottom / 2,
                opened_search_container.width.toFloat(), 0f)

        revealAnim.duration = 500
        revealAnim.start()
        revealAnim.addListener(AnimatorEndListener({
            opened_search_container.invisible()
            presenter.setSearchState(ToolbarSearchViewPresenter.SearchState.CLOSED)
        }))
    }

    override fun showKeyboard() = search_edit_text.showKeyboard()

    override fun hideKeyboard() = search_edit_text.hideKeyboard()

    override fun notifyTextChanged(query: String) {
        onEventListener?.onSearchTextChanged(query)
    }

    override fun notifySearchOpened() {
        onEventListener?.onSearchOpened()
    }

    override fun notifySearchClosed() {
        onEventListener?.onSearchClosed()
    }

    override fun notifySearchExecuted() {
        search_edit_text.clearFocus()
        onEventListener?.onSearchExecuteClicked(searchText)
    }

    interface OnEventListener {
        fun onSearchClosed()
        fun onSearchOpened()
        fun onSearchTextChanged(newText: String)
        fun onSearchExecuteClicked(query: String)
    }
}