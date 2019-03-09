package com.study.netmoudle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.study.netmoudle.bean.TestBean2;
import com.study.netmoudle.usecase.TestUsecase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnTest;
    private TestUsecase testUsecase;
    private Disposable disposable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
        btnTest = findViewById(R.id.btn_test);
        testUsecase = new TestUsecase();
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 disposable = testUsecase.getTest()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<TestBean2>() {
                    @Override
                    public void accept(TestBean2 testBean) throws Exception {
                        Log.i("LHD","请求成功 = "+testBean.getLogin());
//                        if (testBean!=null&&testBean.getData()!=null){
//                            TestBean.DataBean data= testBean.getData();
////                            String name = data.getName();
//                            String sex = data.getSex();
//                            int age = data.getAge();
////                            tvResult.setText(name+"  "+sex+"  "+age);
//                        }
                        tvResult.setText(testBean.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("LHD","请求失败 = "+throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
