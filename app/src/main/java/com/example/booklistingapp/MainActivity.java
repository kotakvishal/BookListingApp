package com.example.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button mFindButton;
    TextView mNotFound;
    EditText mTextEntered;
    ProgressBar mLoader;

    BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoader = (ProgressBar) findViewById(R.id.progress_bar);
        mTextEntered = (EditText) findViewById(R.id.search_box);
        mNotFound = (TextView) findViewById(R.id.no_book_or_internet_found);
        mFindButton = (Button) findViewById(R.id.seach_button);

        adapter = new BookAdapter(this);

        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                adapter.clear();

                if (mTextEntered.getText().toString().trim().matches("")) {
                    mNotFound.setVisibility(View.VISIBLE);
                    mNotFound.setText("Please Enter Something in Search Box");
                } else {
                    if (isNetworkConnected()) {
                        BookAsyncTask task = new BookAsyncTask();
                        task.execute();
                    } else {
                        mNotFound.setVisibility(View.VISIBLE);
                        mNotFound.setText("Not connected to Internet");
                    }
                }
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.list_items);
        bookListView.setAdapter(adapter);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            return false;
    }
    public String getText() {
        return mTextEntered.getText().toString();
    }

    private class BookAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            mNotFound.setVisibility(View.INVISIBLE);
            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> result = Utils.fetchBookData(getText());
            return result;

        }

        @Override
        protected void onPostExecute(List<Book> books) {
            mLoader.setVisibility(View.INVISIBLE);

            if (books == null) {
                mNotFound.setVisibility(View.VISIBLE);
                mNotFound.setText("Books Not Found");
            } else {
                mNotFound.setVisibility(View.GONE);
                adapter.addAll(books);
            }
        }
    }
}