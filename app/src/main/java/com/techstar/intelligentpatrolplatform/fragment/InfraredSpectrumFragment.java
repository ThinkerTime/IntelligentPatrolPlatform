package com.techstar.intelligentpatrolplatform.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;
import com.techstar.intelligentpatrolplatform.utils.LogUtils;

import static android.app.Activity.RESULT_OK;

/**
 * author lrzg on 16/11/10.
 * 描述：红外图谱
 */

public class InfraredSpectrumFragment extends BaseFragment{

    public String[] allFiles;
    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";
    private MediaScannerConnection conn;
    private ImageView mPic_img;


    public static InfraredSpectrumFragment newInstance(String action) {
        InfraredSpectrumFragment fragment = new InfraredSpectrumFragment();
        Bundle args = new Bundle();
        args.putString(CommonUtils.ACTION, action);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
        }
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_infrared_spectrum, container, false);
        mPic_img = (ImageView) view.findViewById(R.id.img_pic);

        view.findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.flir.flirone");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (intent != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
                    startActivity(intent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    Toast.makeText(getActivity().getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);

            }
        });



        return view;
    }

    @Override
    public void initData() {
        hideLoading();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                LogUtils.d("path:"+path);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                mPic_img.setImageBitmap(bitmap);
            }

            cursor.close();
        }

    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onRefresh() {

    }




}
