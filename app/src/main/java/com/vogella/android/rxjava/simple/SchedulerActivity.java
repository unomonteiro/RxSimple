package com.vogella.android.rxjava.simple;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/** Demonstrates a long running operation of the main thread
 * during which a  progressbar is shown
 *
 */
public class SchedulerActivity extends AppCompatActivity {

    private Disposable subscription;
    private ProgressBar progressBar;
    private TextView messageArea;
    private View button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_scheduler);
        progressBar = findViewById(R.id.progressBar);
        messageArea = findViewById(R.id.messagearea);
        button = findViewById(R.id.scheduleLongRunningOperation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progressBar.setVisibility(View.VISIBLE);
                Observable.fromCallable(callable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                            progressBar.setVisibility(View.VISIBLE);
                            button.setEnabled(false);
                            messageArea.setText(messageArea.getText().toString() + "\n" + "Progressbar set visible");
                        })
                        .subscribe(getDisposableObserver());
            }
        });
    }

    private void createObservable() {

    }

    Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            return doSomethingLong();
        }
    };

    private String doSomethingLong() {
        SystemClock.sleep(1000);
        return "Hello";
    }

    /**
     * Observer
     * Handles the stream of data:
     */
    private DisposableObserver<String> getDisposableObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onComplete() {
                messageArea.setText(addNewLine() + "OnComplete");
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messageArea.setText(addNewLine() + "Hidding Progressbar");
            }

            @Override
            public void onError(Throwable e) {
                messageArea.setText(addNewLine() + "OnError");
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messageArea.setText(addNewLine() + "Hidding Progressbar");
            }

            @Override
            public void onNext(String message) {
                messageArea.setText(addNewLine() + "onNext " + message);
            }
        };
    }

    private String addNewLine() {
        return messageArea.getText().toString() + "\n";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
