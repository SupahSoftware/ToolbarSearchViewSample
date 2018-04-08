package supahsoftware.toolbarsearchviewsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import supahsoftware.toolbarsearchview.ToolbarSearchView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbarSearchView = ToolbarSearchView(this, main_activity_layout, "My cool title")
        toolbarSearchView.setPrimaryColor(R.color.testColor1)
        toolbarSearchView.setSecondaryColor(R.color.testColor2)
        toolbarSearchView.setOnEventListener(object : ToolbarSearchView.OnEventListener {
            override fun onSearchClosed() = "Search closed".toast()
            override fun onSearchOpened() = "Search opened".toast()
            override fun onSearchTextChanged(newText: String) = newText.toast()
            override fun onSearchExecuteClicked(query: String) = query.toast()
        })
    }

    fun String.toast() = Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
}
