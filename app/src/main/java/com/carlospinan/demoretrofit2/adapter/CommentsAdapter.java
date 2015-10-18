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

package com.carlospinan.demoretrofit2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlospinan.demoretrofit2.R;
import com.carlospinan.demoretrofit2.models.Comment;

import java.util.List;

/**
 * @author Carlos Piñan
 */
public class CommentsAdapter extends BaseAdapter {

    private List<Comment> listComments;
    private LayoutInflater inflater;

    public CommentsAdapter(Context context, List<Comment> listComments) {
        this.listComments = listComments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return getListComments().size();
    }

    @Override
    public Comment getItem(int position) {
        return getListComments().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        final ViewHolder holder;
        final Comment comment = getItem(position);
        if (view == null || view.getTag() == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_comments, container, false);
            holder.nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            holder.emailTextView = (TextView) view.findViewById(R.id.emailTextView);
            holder.bodyTextView = (TextView) view.findViewById(R.id.bodyTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTextView.setText(String.format("Name: %s", comment.getName()));
        holder.emailTextView.setText(String.format("Email: %s", comment.getEmail()));
        holder.bodyTextView.setText(String.format("Comment:\n%s", comment.getBody()));
        return view;
    }

    public List<Comment> getListComments() {
        return listComments;
    }

    public void setListComments(List<Comment> listComments) {
        this.listComments = listComments;
        this.notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView bodyTextView;
    }

}
