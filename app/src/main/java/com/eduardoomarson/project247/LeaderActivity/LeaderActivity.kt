package com.eduardoomarson.project247.LeaderActivity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eduardoomarson.project247.LeaderActivity.Model.UserModel
import com.eduardoomarson.project247.R

class LeaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor= ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val topUsers = loadData().take(n = 3)
        val otherUsers = loadData().drop(n = 3)

        setContent {
            LeaderScreen(
                topUsers = topUsers,
                otherUsers = otherUsers,
                onBackClick = {finish()}
                )
        }
    }
    private fun loadData() : List<UserModel> {
        return listOf(
            UserModel(id = 1, name = "Sophia", pic = "person1", score = 4850),
            UserModel(id = 2, name = "Daniel", pic = "person2", score = 4560),
            UserModel(id = 3, name = "James", pic = "person3", score = 3873),
            UserModel(id = 4, name = "John Smith", pic = "person4", score = 3250),
            UserModel(id = 5, name = "Emily Johnson", pic = "person5", score = 3015),
            UserModel(id = 6, name = "David Brown", pic = "person6", score = 2970),
            UserModel(id = 7, name = "Sarah Wilson", pic = "person7", score = 2870),
            UserModel(id = 8, name = "Michael Davis", pic = "person8", score = 2670),
            UserModel(id = 9, name = "Sarah Wilson", pic = "person9", score = 2380),
            UserModel(id = 10, name = "Sarah Wilson", pic = "person10", score = 2370),
        )
    }
}