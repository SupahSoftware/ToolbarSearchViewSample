package supahsoftware.toolbarsearchview

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener

class SearchActionListener(private val callback: () -> Unit = {}) : OnEditorActionListener {

    override fun onEditorAction(view: TextView, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (isSearchAction(keyCode, keyEvent)) {
            callback()
            return true
        }
        return false
    }

    private fun isSearchAction(keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return keyCode == EditorInfo.IME_ACTION_SEARCH || (keyEvent?.action == KeyEvent.ACTION_UP && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
    }
}