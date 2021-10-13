package com.example.linah_alkhurayyif_rssfeedparsing

import android.app.AlertDialog
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.alert.view.*
import kotlinx.android.synthetic.main.item.view.*

class RecentQuestionsAdapter ( val questions: ArrayList<RecentQuestionsDetails>) :
    RecyclerView.Adapter<RecentQuestionsAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val question = questions[position]
        holder.itemView.apply {
              title_tv.text = question.title
            title_cv.setOnClickListener{

                //Inflate the dialog as custom view
                val messageBoxView = LayoutInflater.from(context).inflate(R.layout.alert, null)

                //AlertDialogBuilder
                val messageBoxBuilder = AlertDialog.Builder(context).setView(messageBoxView)
                var summary= Html.fromHtml(Html.fromHtml(question.summary).toString())
                //setting text values
                messageBoxView.alertTitle.text = "More Details"
                messageBoxView.Title_tv.text ="Title: "+ question.title
                messageBoxView.Rank_tv.text ="Rank: "+ question.rank
                messageBoxView.Author_tv.text ="Author: "+ question.author
                messageBoxView.Published_tv.text ="Published: "+ question.published

                messageBoxView.Updated_tv.text ="Updated: "+ question.updated
                messageBoxView.summary_tv.text ="Summary: "+ summary


                //show dialog
                val  messageBoxInstance = messageBoxBuilder.show()
                //set Listener
                messageBoxView.close.bringToFront();
                messageBoxView.close.setOnClickListener(){
                    //close dialog
                    messageBoxInstance.dismiss()
                }
            }

        }
    }

    override fun getItemCount()=questions.size
}