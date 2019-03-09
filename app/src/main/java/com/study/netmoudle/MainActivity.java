package com.study.netmoudle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.study.netmoudle.usecase.TestUsecase;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
        btnTest.setOnClickListener(view -> disposable = testUsecase.getTest().subscribe(testBean -> {
//            if (testBean!=null&&testBean.getData()!=null){
//                TestBean.DataBean data= testBean.getData();
//                String name = data.getName();
//                String sex = data.getSex();
//                int age = data.getAge();
//                tvResult.setText(name+"  "+sex+"  "+age);
//            }
        }, new Consumer<Throwable>() {
           @Override
           public void accept(Throwable throwable) throws Exception {
               Log.i("LHD","请求失败 = "+throwable.getMessage());
               throwable.printStackTrace();
           }
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
