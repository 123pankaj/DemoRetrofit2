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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carlospinan.demoretrofit2.R;
import com.carlospinan.demoretrofit2.adapter.CommentsAdapter;
import com.carlospinan.demoretrofit2.helpers.API;
import com.carlospinan.demoretrofit2.models.Comment;
import com.carlospinan.demoretrofit2.models.Post;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * We can use the object postObject to show the data but for dev purposes we are going to use the Retrofit call.
 *
 * @author Carlos Piñan
 */
public class PostDetailActivity extends AppCompatActivity {

    public static final String POST = "POST_OBJECT_KEY";

    @Bind(R.id.listView)
    ListView listView;

    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView commentsTextView;

    private View header;
    private Post postObject;
    private Call<Post> callPost;
    private Call<List<Comment>> callComments;

    private ProgressDialog progressDialog;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        postObject = (Post) getIntent().getExtras().get(POST);

        header = getLayoutInflater().inflate(R.layout.header_post_detail, null);
        titleTextView = (TextView) header.findViewById(R.id.titleTextView);
        bodyTextView = (TextView) header.findViewById(R.id.bodyTextView);
        commentsTextView = (TextView) header.findViewById(R.id.commentsTextView);
        listView.addHeaderView(header);
        listView.setAdapter(null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });
        progressDialog.setTitle(null);
        progressDialog.setMessage(getString(R.string.s_loading_post_detail));
    }

    @Override
    protected void onResume() {
        super.onResume();
        callServices();
    }

    @Override
    protected void onPause() {
        pauseServices();
        super.onPause();
    }

    private void callServices() {
        final int postId = postObject.getId();
        callPost = API.get().getRetrofitService().getPost(postId);
        callComments = API.get().getRetrofitService().getCommentsForPost(postId);

        // Just an example to show how work multiple calls.
        callPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Response<Post> response) {
                Post postBody = response.body();
                titleTextView.setText(postBody.getTitle());
                bodyTextView.setText(Html.fromHtml(postBody.getBody()));
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO Implement generic alert dialog.
            }
        });

        callComments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Response<List<Comment>> response) {
                if (!response.body().isEmpty()) {
                    commentsTextView.setText(R.string.s_comments_in_post);
                    if (adapter == null) {
                        adapter = new CommentsAdapter(PostDetailActivity.this, response.body());
                        listView.setAdapter(adapter);
                    } else {
                        adapter.setListComments(response.body());
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO Implement generic alert dialog.
            }
        });

        progressDialog.show();

    }

    private void pauseServices() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (callPost != null) {
            callPost.cancel();
        }
        if (callComments != null) {
            callComments.cancel();
        }
    }

}
