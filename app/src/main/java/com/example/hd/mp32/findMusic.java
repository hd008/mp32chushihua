package com.example.hd.mp32;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class findMusic extends AppCompatActivity {
    private EditText name;
    private Button find_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);

        name = findViewById(R.id.find_name);
        find_btn = findViewById(R.id.find_btn);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = name.getText().toString();
                if(!TextUtils.isEmpty(name_)){
                Toast.makeText(findMusic.this,name_,Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(findMusic.this, "歌名不能为空", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
