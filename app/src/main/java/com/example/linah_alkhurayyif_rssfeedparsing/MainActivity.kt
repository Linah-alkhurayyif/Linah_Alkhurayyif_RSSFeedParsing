package com.example.linah_alkhurayyif_rssfeedparsing

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var recent_questions: ArrayList<RecentQuestionsDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FetchRecentQuestions().execute()
    }

    private inner class FetchRecentQuestions: AsyncTask<Void, Void, ArrayList<RecentQuestionsDetails>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): ArrayList<RecentQuestionsDetails> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            recent_questions =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as ArrayList<RecentQuestionsDetails>
            return recent_questions
        }

        override fun onPostExecute(result: ArrayList<RecentQuestionsDetails>?) {
            super.onPostExecute(result)
            recyclerView.adapter = RecentQuestionsAdapter(recent_questions)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        }

    }

}