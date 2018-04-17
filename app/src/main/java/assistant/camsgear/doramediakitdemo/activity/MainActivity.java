package assistant.camsgear.doramediakitdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import java.util.ArrayList;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.global.FileType;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private RecyclerView mList;

    private ArrayList<MenuBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList= (RecyclerView)findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        data=new ArrayList<>();
        add("播放云端H265文件",VideoActivity.class,FileType.FILE_TYPE_H265);
        add("播放云端双鱼眼文件", VideoActivity.class, FileType.FILE_TYPE_DOUBLE_EYE);
        add("播放云端双鱼眼图片", VideoActivity.class, FileType.FILE_TYPE_DOUBLE_FISHEYE_IMAGE);
        add("播放云端单鱼眼图片", VideoActivity.class,FileType.FILE_TYPE_SINGLE_FISHEYE_IMAGE);
        add("播放云端图片流",ImageStreamActivity.class,FileType.FILE_TYPE_IMAGE_STREAM);
        add("播放本地H264流", RawVideoActivity.class, FileType.FILE_TYPE_LOCAL_RAW_H264);
        add("播放云端MKV文件", VideoActivity.class, FileType.FILE_TYPE_CLOUD_MKV);
        add("播放RTSP流", VideoActivity.class, FileType.FILE_TYPE_RTSP);

        mList.setAdapter(new MenuAdapter());
    }
    private void add(String name,Class<?> clazz,FileType fileType){
        MenuBean bean=new MenuBean();
        bean.name=name;
        bean.clazz=clazz;
        bean.fileType = fileType;
        data.add(bean);
    }

    private class MenuBean{
        String name;
        Class<?> clazz;
        FileType fileType;
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean=data.get(position);
        Intent intent = new Intent(this, bean.clazz);
        intent.putExtra("fileType", bean.fileType);
        startActivity(intent);
    }
}
