/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Carlos Piñan
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.carlospinan.demoretrofit2.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlospinan.demoretrofit2.R;
import com.carlospinan.demoretrofit2.adapter.PostsAdapter;
import com.carlospinan.demoretrofit2.helpers.API;
import com.carlospinan.demoretrofit2.models.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * @author Carlos Piñan
 */
public class PostsActivity extends AppCompatActivity {

    @Bind(R.id.listView)
    ListView listView;

    private PostsAdapter adapter;
    private Call<List<Post>> callListPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);
        View header = getLayoutInflater().inflate(R.layout.list_posts, null);
        ((TextView) header.findViewById(R.id.postIdTextView)).setText(R.string.s_post_id);
        ((TextView) header.findViewById(R.id.userIdTextView)).setText(R.string.s_user_id);
        ((TextView) header.findViewById(R.id.titleTextView)).setText(R.string.s_post_title);
        listView.addHeaderView(header, null, false);
        listView.setAdapter(null);
        ((TextView) findViewById(R.id.titleTextView)).setText(R.string.s_title_global_posts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callListPosts();
    }

    @Override
    protected void onPause() {
        if (callListPosts != null) {
            callListPosts.cancel();
        }
        super.onPause();
    }

    private void callListPosts() {
        if (adapter == null) {
            adapter = new PostsAdapter(this, new ArrayList<Post>());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    int index = position - 1; // For header view it could be fixed just using the adapter from the adapterView
                    if (adapter.getItem(index) != null && adapter.getItem(index) instanceof Post) {
                        Post post = adapter.getItem(index);
                        Intent intent = new Intent(PostsActivity.this, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.POST, post);
                        startActivity(intent);
                    }
                }
            });
        }
        callListPosts = API.get().getRetrofitService().getPosts();
        callListPosts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Response<List<Post>> response) {
                adapter.setListPosts(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO Implement generic alert dialog.
            }
        });
    }
}