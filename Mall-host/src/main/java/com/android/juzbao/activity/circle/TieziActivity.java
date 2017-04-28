package com.android.juzbao.activity.circle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.model.circle.CommitDynamicBean;
import com.android.juzbao.model.circle.PictureBean;
import com.android.juzbao.model.circle.VideoBean;
import com.android.juzbao.utils.BitmapUtils;
import com.android.juzbao.utils.Util;
import com.android.juzbao.view.MyGridView;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.server.api.service.CircleService;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ContentBody;
import internal.org.apache.http.entity.mime.content.FileBody;


public class TieziActivity extends BaseActivity implements View.OnClickListener {
    private static final int IMAGE_CODE = 110;
    private static final int VIDEO_CODE = 111;
    private static final int ERROR_CODE = 112;

    private ImageView back, add, video;
    private TextView commit, popPic, popVideo, popCancle;
    private EditText editText;
    private MyGridView propostQuestionGridview;
    private LinearLayout layout;
    private List<String> mPaths;
    private LinearLayout relativeLayout;
    private View view;

    private GridImagePhotoAdapter mGridImagePhotoAdapter;
    //存放所有选择的照片的路径集合
    private ArrayList<String> allSelectedPicture;
    //存放从选择界面选择的照片的路径集合
    private ArrayList<String> selectedPicture;
    //这里放置压缩后上传服务器的图片
    private ArrayList<String> compressedPicture;
    private PopupWindow window;

    private String type;//添加的类型
    private String v_path;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == IMAGE_CODE) {
                String coverIds = (String) msg.obj;
                commit(content, coverIds, "3");
            } else if (msg.what == VIDEO_CODE) {
                String videoId = (String) msg.obj;
                commit(content, "3", videoId);
            } else if (msg.what == ERROR_CODE) {
                progressDialog.dismiss();
            }
        }
    };
    private String content;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiezi);
        initView();
        initData();
        set();

    }

    private void set() {
        add.setOnClickListener(this);
        back.setOnClickListener(this);
        video.setOnClickListener(this);
        popPic.setOnClickListener(this);
        popVideo.setOnClickListener(this);
        popCancle.setOnClickListener(this);
        layout.setOnClickListener(this);
        commit.setOnClickListener(this);
        progressDialog.setMessage("发布中..");
        progressDialog.setCanceledOnTouchOutside(false);
        propostQuestionGridview.setAdapter(mGridImagePhotoAdapter);
    }

    private void initData() {
        allSelectedPicture = new ArrayList<>();
        selectedPicture = new ArrayList<>();
        mGridImagePhotoAdapter = new GridImagePhotoAdapter();
        compressedPicture = new ArrayList<>();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        view = View.inflate(this, R.layout.item_pop, null);
        relativeLayout = (LinearLayout) findViewById(R.id.activity_tiezi);
        video = (ImageView) findViewById(R.id.img_fatie_video);
        back = (ImageView) findViewById(R.id.m_finish);
        add = (ImageView) findViewById(R.id.img_fatie_add);
        commit = (TextView) findViewById(R.id.m_more);
        editText = (EditText) findViewById(R.id.et_fatie_input);
        layout = (LinearLayout) findViewById(R.id.ll_fatie);
        propostQuestionGridview = (MyGridView) findViewById(R.id.gv_fatie);

        popPic = (TextView) view.findViewById(R.id.tv_pop_pic);
        popVideo = (TextView) view.findViewById(R.id.tv_pop_video);
        popCancle = (TextView) view.findViewById(R.id.tv_pop_cancle);
        window = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_fatie_add:
                showPopWindow();
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
                break;
            case R.id.m_finish:
                finish();
                break;
            case R.id.tv_pop_pic:
                type = "pic";
                selectClick();
                window.dismiss();
                break;
            case R.id.tv_pop_video:
                type = "video";
                chooseVideo();
                window.dismiss();
                break;
            case R.id.tv_pop_cancle:
                window.dismiss();
                break;
            case R.id.img_fatie_video:
                chooseVideo();
                break;
            case R.id.ll_fatie:
                Util.toggleSoftKeyboardState(this);
                break;
            case R.id.m_more:
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
                progressDialog.show();
                content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                if (selectedPicture.size() > 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            sendFilesPost(compressBitmap(selectedPicture));

                        }
                    }.start();
                } else if (!TextUtils.isEmpty(v_path)) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Log.e("gy", "视频地址：" + v_path);
                            upVideo(v_path);
                        }
                    }.start();
                } else {
                    commit(content, "3", "3");
                }

                break;
        }
    }

    /**
     * 发表动态
     */
    private void commit(String content, String paths, String videoPath) {
        CircleService.commitDynamic dynamic = new CircleService.commitDynamic();
        dynamic.content = content;
        dynamic.cover_ids = paths;
        dynamic.video_id = videoPath;
        getHttpDataLoader().doPostProcess(dynamic, CommitDynamicBean.class);
    }

    private void showPopWindow() {
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable());
        window.showAtLocation(relativeLayout, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 选择视频
     */
    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(intent, VIDEO_CODE);
    }

    /**
     * 展示图片的GridView的适配器
     */
    class GridImagePhotoAdapter extends BaseAdapter {
        /**
         * 这里同过集合长短来控制九宫格视图显示的变化
         * 为空显示基本视图。长度为9全部显示。其他显示布局并且显示最后基本视图
         *
         * @return 显示视图的多少
         */
        @Override
        public int getCount() {
            if (allSelectedPicture == null) {
                return 1;
            } else if (allSelectedPicture.size() == 9) {
                return 9;
            } else {
                return allSelectedPicture.size() + 1;
            }
        }

        /**
         * 1. 当长度不符合标准。直接reun null
         * 2. 如果长度不为空并且长度达到上线。直接显示对应位置的视图
         * 3. 如果没达到上限，预留最后基本视图的位置
         *
         * @param position 返回视图的位置
         * @return 返回显示的内容
         */
        @Override
        public Object getItem(int position) {
            if (allSelectedPicture != null && allSelectedPicture.size() == 9) {
                return allSelectedPicture.get(position);
            } else if (allSelectedPicture == null || position - 1 < 0 || position > allSelectedPicture.size()) {
                return null;
            } else {
                return allSelectedPicture.get(position - 1);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(TieziActivity.this, R.layout.child_grid_photo_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //如果当前的位置正好为添加照片的长度，那么直接显示默认图片
            if (position == allSelectedPicture.size()) {
                holder.childGridPhotoImage.setImageResource(R.drawable.tianjia);
                holder.childGridPhotoDelete.setVisibility(View.GONE);
                holder.childGridPhotoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击小图片进入图片的选择界面(相册界面)
                        selectClick();
                    }
                });
            } else {
                holder.childGridPhotoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击图片调到图片的预览界面
                        imageBrower(position, allSelectedPicture);
                    }
                });
                //"file://" + allSelectedPicture.get(position)路径加载图片
                Glide.with(TieziActivity.this).load("file://" + allSelectedPicture.get(position)).asBitmap().into(holder.childGridPhotoImage);
                holder.childGridPhotoDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片，TODO 这里添加一个提示框
                        allSelectedPicture.remove(position);
                        //刷新UI
                        notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            ImageView childGridPhotoImage;
            Button childGridPhotoDelete;

            ViewHolder(View view) {
                childGridPhotoImage = (ImageView) view.findViewById(R.id.child_grid_photo_image);
                childGridPhotoDelete = (Button) view.findViewById(R.id.child_grid_photo_delete);
            }
        }
    }

    /**
     * 携带存放照片(最多9张)进行跳转
     */
    private void selectClick() {
        Intent intent = new Intent(TieziActivity.this, SelectPictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("allSelectedPicture", allSelectedPicture);
        intent.putExtras(bundle);
        startActivityForResult(intent, IMAGE_CODE);
    }


    /**
     * 这里跳转到图片的预览界面
     *
     * @param position
     * @param urls
     */
    private void imageBrower(int position, List<String> urls) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        int size = urls.size();
        //携带点击位置，和路径的集合进行跳转
        String[] arr = allSelectedPicture.toArray(new String[size]);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arr);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    /**
     * 这里选择好了之后的回转传值。获取图片集合路径
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @SuppressWarnings("unchecked") 去掉因为GildView引发的警告
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                selectedPicture = (ArrayList) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                for (String str : selectedPicture) {
                    if (!allSelectedPicture.contains(str)) {
                        //便利之后如果不存在相同的路径再添加，防止重复添加
                        //这里添加。这里无法更新UI。向用户展示的是原始图片
                        allSelectedPicture.add(str);
                        propostQuestionGridview.setAdapter(mGridImagePhotoAdapter);
                        //在子线程中进行，添加之前进行图片的二次压缩
                        add.setVisibility(View.GONE);
                        propostQuestionGridview.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
        if (requestCode == VIDEO_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri.getPath().startsWith("/storage")) {
                    add.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    v_path = uri.getPath();
                    return;
                }
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                cursor.moveToFirst();
                // 文件路径
                v_path = cursor.getString(1);
                add.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.commitDynamic.class)) {
            CommitDynamicBean response = msg.getRspObject();
            if (null != response) {
                progressDialog.dismiss();
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(), msg, "发布成功");
                    Log.e("gy", "发布结果：" + response.getCode());
                    setResult(111);
                    finish();
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, "发布失败");
                }
            }
        }
    }

    /**
     * 上传图片
     *
     * @param fileNames
     * @return
     */
    public String sendFilesPost(List<String> fileNames) {
        HttpClient httpClient = null;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://xiaoyuan.jzbwlkj.com/api/File/uploadPictureApp");
            MultipartEntity reqEntity = new MultipartEntity();
            for (int i = 0; i < fileNames.size(); i++) {
                ContentBody d = new FileBody(new File(fileNames.get(i)), "image/jpeg");
                reqEntity.addPart("images" + i, d);// file1为请求后台的File upload;属性
            }
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                if (null != resEntity) {
                    result = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    PictureBean bean = new Gson().fromJson(result, PictureBean.class);
                    Log.e("gy", "上传图片结果是什么：" + bean.getData());
                    Message message = Message.obtain();
                    message.what = IMAGE_CODE;
                    message.obj = bean.getData()+"";
                    handler.sendMessage(message);
                }
            } else {
                handler.sendEmptyMessage(ERROR_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 上传视频
     *
     * @param fileName
     * @return
     */
    private String upVideo(String fileName) {

        HttpClient httpClient = null;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://xiaoyuan.jzbwlkj.com/api/File/UploadQN");
            MultipartEntity reqEntity = new MultipartEntity();
            FileBody file = new FileBody(new File(fileName));
            reqEntity.addPart("file", file);// file1为请求后台的File upload;属性
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                if (null != resEntity) {
                    result = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    Log.e("gy", "视频上传的结果：" + result);
                    VideoBean bean = new Gson().fromJson(result, VideoBean.class);
                    Message message = Message.obtain();
                    message.what = VIDEO_CODE;
                    message.obj = bean.getData();
                    handler.sendMessage(message);
                }
            } else {
                handler.sendEmptyMessage(ERROR_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 压缩图片
     *
     * @param paths
     * @return
     */
    private List<String> compressBitmap(List<String> paths) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            Log.e("gy", "paths :" + paths.get(i));
            String s = BitmapUtils.compressImageUpload(paths.get(i));
            list.add(s);

        }
        return list;
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final OkHttpClient client = new OkHttpClient();

    private void uploadImg(List<String> mImgUrls) {

        // mImgUrls为存放图片的url集合
        MultipartBuilder builder = new MultipartBuilder();

//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < mImgUrls.size(); i++) {
            File f = new File(mImgUrls.get(i));
            if (f != null) {
                builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }

        //添加其它信息
//        builder.addFormDataPart("time",takePicTime);
//        builder.addFormDataPart("mapX", SharedInfoUtils.getLongitude());
//        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
//        builder.addFormDataPart("name",SharedInfoUtils.getUserName());


        RequestBody body = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url("http://shop.jzbwlkj.com/api/File/uploadPictureApp")//地址
                .post(body)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });

    }


}
