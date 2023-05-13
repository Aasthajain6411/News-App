package com.example.newsfresh

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {
    //called this adapter out so make it a member function so that it is accessible everywhere
    private lateinit var mAdapter: NewListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // specify the type of recycler view
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()

        //creating an adapter of the class i.e creating the instance of the adapter class
        mAdapter=NewListAdapter(this)
        recyclerView.adapter=mAdapter
    }
    // after : comes the return type of the function

    private fun fetchData(){
        val url= "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=1633a1383c334a97abc93013fa1b7d24"
        val jsonObjectRequest=object:JsonObjectRequest(
            Request.Method.GET,url, null,
            {

                //get the json array out of the json object request
                val newsJsonArray= it.getJSONArray("articles")
                //now we need to pass it through a news list
                val newsArray= ArrayList<News>()

                //iterate through the json array that you got
                for(i in 0 until newsJsonArray.length()){
                    //get json object at index i
                    val obj=newsJsonArray.getJSONObject(i);
                    //create instance of news class
                    val news=News(
                        obj.getString("title"),
                        obj.getString("author"),
                        obj.getString("url"),
                        obj.getString("urlToImage")
                    )
                    //add the created object to the newsArray
                    newsArray.add(news)
                }
                //made the adapter a member so that it is accessible here
                //directly give the news to the adapter
                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }

            )
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"]="Mozilla/5.0"
                    return headers
                }
            }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
         //dummy data
//        val list= ArrayList<String>()
//        for(i in 0 until 100){
//            list.add("Item $i")
//        }
//        return list
    }

    //this was done to add the listener to the adapter function. This function will tell you what to actually do when a view is clicked
    override fun onItemClicked(item: News) {
        //Toast.makeText(this, "Clicked item is $item",Toast.LENGTH_LONG).show()
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(this@MainActivity, Uri.parse(item.url))
    }

}