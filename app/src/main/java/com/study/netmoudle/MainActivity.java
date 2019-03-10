package com.study.netmoudle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.study.netmoudle.usecase.BlogUsecase;
import com.study.netmoudle.usecase.TestUsecase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnTest;
    private Button btnGetBlog;
    private BlogUsecase blogUsecase;
    private TestUsecase testUsecase;
    private Disposable disposable ;
    private Disposable disposableTest ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
        btnTest = findViewById(R.id.btn_test);
        btnGetBlog = findViewById(R.id.btn_get_blog_json);
        blogUsecase = new BlogUsecase();
        testUsecase = new TestUsecase();
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disposableTest = testUsecase.getTest().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(testBean -> Log.i("LHD","TestBean请求成功 = "+testBean.getData().getAge()), new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("LHD","TestBean请求失败= "+throwable.getMessage());
                            }
                        });
            }
        });

        btnGetBlog.setOnClickListener(view -> disposable = blogUsecase.getBlogJson()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(blogBean -> {
                    Log.i("LHD","请求成功 = "+blogBean.getLogin());
                    tvResult.setText(blogBean.getName()+"   "+blogBean.getLogin());
                }, throwable -> {
                    Log.i("LHD","请求失败 = "+throwable.getMessage());
                    throwable.printStackTrace();
                }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
