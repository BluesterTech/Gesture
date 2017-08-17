package cn.bluester.recordtask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TaskListActivity extends AppCompatActivity {
    private String[] names={"发动机","机翼","起落架"};
    private String[] task={"{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://2.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"拆开发动机\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://3.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"维修发动机\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://1.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"装发动机\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"发动机\"\n" +
            "    }\n" +
            "}","{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://4.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"拆开机翼\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://5.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"维修机翼\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://6.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"组装机翼\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"机翼\"\n" +
            "    }\n" +
            "}","{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://7.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"扭螺丝\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://8.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"卸轮胎\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://9.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"装轮胎\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"起落架\"\n" +
            "    }\n" +
            "}"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        ListView listView = (ListView) findViewById(R.id.lv_lsit);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_list,names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TaskListActivity.this,MainActivity.class);
                intent.putExtra("task",task[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        super.onResume();
    }
}
