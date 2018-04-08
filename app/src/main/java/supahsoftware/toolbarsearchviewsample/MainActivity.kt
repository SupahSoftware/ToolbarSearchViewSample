package supahsoftware.toolbarsearchviewsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import supahsoftware.toolbarsearchview.ToolbarSearchView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbarSearchView = ToolbarSearchView(this, supportActionBar, "My cool title")
        toolbarSearchView.setOnEventListener(object : ToolbarSearchView.OnEventListener {
            override fun onSearchClosed() = "Search closed".toast()
            override fun onSearchOpened() = "Search opened".toast()
            override fun onSearchTextChanged(newText: String) = newText.toast()
            override fun onSearchExecuteClicked(query: String) = query.toast()
        })
    }

    fun String.toast() = Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
}
