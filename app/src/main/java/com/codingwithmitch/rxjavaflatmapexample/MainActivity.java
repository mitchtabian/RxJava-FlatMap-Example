package com.codingwithmitch.rxjavaflatmapexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codingwithmitch.rxjavaflatmapexample.models.Comment;
import com.codingwithmitch.rxjavaflatmapexample.models.Post;
import com.codingwithmitch.rxjavaflatmapexample.requests.ServiceGenerator;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnPostClickListener {

    private static final String TAG = "MainActivity";

    //ui
    private RecyclerView recyclerView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerAdapter adapter;
    private PublishSubject<Post> publishSubject = PublishSubject.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();
        retrievePosts();

        publishSubject
                .switchMap(new Function<Post, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(Post post) throws Exception {
                        return ServiceGenerator.getRequestApi()
                                .getPost(post.getId())
                                .subscribeOn(Schedulers.io())
                                .map(new Function<Post, Post>() { // function that does nothing. Just sleeps the thread
                                    @Override
                                    public Post apply(Post post) throws Exception {
                                        Log.d(TAG, "apply: retrieving individual post data: post id: " + post.getId());
                                        try {
                                            Thread.sleep(3000); // thread blocking call
                                        } catch (InterruptedException ex) {
                                            // check if the interrupt is due to cancellation
                                            // if so, no need to signal the InterruptedException
                                        }
                                        Log.d(TAG, "apply: sleeping thread: " + Thread.currentThread().getName() + " for 3000 ms");

                                        return post;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Post post) {
                        Log.d(TAG, "onNext: got the post! " + post.getId());
                        navViewPostActivity(post);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void updatePost(Post post){
        adapter.updatePost(post);
    }


    private void retrievePosts(){
        ServiceGenerator.getRequestApi()
                .getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Post> posts) {
                adapter.setPosts(posts);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void initRecyclerView(){
        adapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void navViewPostActivity(Post post){
        Intent intent = new Intent(this, ViewPostActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: called.");
        disposables.dispose();
        super.onPause();
    }

    @Override
    public void onPostClick(final int position) {

        Log.d(TAG, "onPostClick: clicked.");

        publishSubject.onNext(adapter.getPosts().get(position));
    }
}



















