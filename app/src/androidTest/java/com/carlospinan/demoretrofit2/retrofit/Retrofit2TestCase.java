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

package com.carlospinan.demoretrofit2.retrofit;

import android.test.AndroidTestCase;
import android.util.Log;

import com.carlospinan.demoretrofit2.helpers.API;
import com.carlospinan.demoretrofit2.interfaces.APIService;
import com.carlospinan.demoretrofit2.models.Comment;
import com.carlospinan.demoretrofit2.models.Post;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Carlos Piñan
 */
public class Retrofit2TestCase extends AndroidTestCase {

    private static final String LOG_TAG = Retrofit2TestCase.class.getName();
    private APIService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        service = API.get().getRetrofitService();
    }

    /**
     * @throws IOException
     */
    public void testGetPosts() throws IOException {
        log("*** testGetPosts() ***");
        List<Post> posts = getPosts();

        for (Post post : posts) {
            log("id: " + post.getId());
            log("userId: " + post.getUserId());
            log("titleTextView: " + post.getTitle());
            log("body: " + post.getBody());
            log("=========================");
        }
    }

    /**
     * @throws IOException
     */
    public void testGetPost() throws IOException {
        log("*** testGetPost() ***");
        List<Post> posts = getPosts();
        int postId = posts.get(0).getId();
        Call<Post> call = service.getPost(postId);
        Response<Post> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());

        Post post = response.body();
        assertEquals(postId, post.getId());
        log("id: " + post.getId());
        log("userId: " + post.getUserId());
        log("titleTextView: " + post.getTitle());
        log("body: " + post.getBody());
        log("=========================");
    }

    /**
     * @throws IOException
     */
    public void testGetCommentsForPost() throws IOException {
        log("*** testGetCommentsForPost() ***");
        List<Post> posts = getPosts();
        int postId = posts.get(0).getId();
        Call<List<Comment>> call = service.getCommentsForPost(postId);
        Response<List<Comment>> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());
        List<Comment> comments = response.body();

        for (Comment comment : comments) {
            log("id: " + comment.getId());
            log("postId: " + comment.getPostId());
            log("name: " + comment.getName());
            log("email: " + comment.getEmail());
            log("body: " + comment.getBody());
            log("=========================");
        }
    }

    /**
     * @throws IOException
     */
    public void testGetPostsForUser() throws IOException {
        log("*** testGetPostsForUser() ***");
        List<Post> posts = getPosts();
        int userId = posts.get(0).getUserId();
        Call<List<Post>> call = service.getPostsForUser(userId);
        Response<List<Post>> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());
        List<Post> postsForUser = response.body();

        for (Post post : postsForUser) {
            log("id: " + post.getId());
            log("userId: " + post.getUserId());
            log("titleTextView: " + post.getTitle());
            log("body: " + post.getBody());
            log("=========================");
        }
    }

    /**
     * @throws IOException
     */
    public void testCreatePost() throws IOException {
        log("*** testCreatePost() ***");
        List<Post> posts = getPosts();
        Post post = posts.get(0);

        Post newPost = new Post();
        newPost.setUserId(post.getUserId());
        newPost.setBody("This is an example body for Carlos Piñan");
        newPost.setTitle("My example titleTextView");

        Call<Post> call = service.createPost(newPost);
        Response<Post> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());

        post = response.body();
        log("id: " + post.getId());
        log("userId: " + post.getUserId());
        log("titleTextView: " + post.getTitle());
        log("body: " + post.getBody());
        log("=========================");
    }

    /**
     * @throws IOException
     */
    public void testUpdatePost() throws IOException {
        log("*** testUpdatePost() ***");
        List<Post> posts = getPosts();
        Post post = posts.get(5);

        Post updatePost = new Post();
        updatePost.setUserId(post.getUserId());
        updatePost.setBody("This is an example body for Carlos Piñan");
        updatePost.setTitle("My example titleTextView");
        updatePost.setId(post.getId());

        Call<Post> call = service.updatePost(post.getId(), updatePost);
        Response<Post> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());

        post = response.body();
        log("id: " + post.getId());
        log("userId: " + post.getUserId());
        log("titleTextView: " + post.getTitle());
        log("body: " + post.getBody());
        log("=========================");
    }

    /**
     * @throws IOException
     */
    public void testDeletePost() throws IOException {
        log("*** testDeletePost() ***");
        List<Post> posts = getPosts();
        Post post = posts.get(0);

        Call<Post> call = service.deletePost(post.getId());
        Response<Post> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());

        post = response.body();
        log("id: " + post.getId());
        log("userId: " + post.getUserId());
        log("titleTextView: " + post.getTitle());
        log("body: " + post.getBody());
        log("=========================");
    }

    // Helpers
    private void log(String message) throws IOException {
        Log.d(LOG_TAG, message);
    }

    private List<Post> getPosts() throws IOException {
        Call<List<Post>> call = service.getPosts();
        Response<List<Post>> response = call.execute();
        assertNotNull(response);
        assertNotNull(response.body());
        List<Post> posts = response.body();
        assertFalse(posts.isEmpty());
        return posts;
    }

}
