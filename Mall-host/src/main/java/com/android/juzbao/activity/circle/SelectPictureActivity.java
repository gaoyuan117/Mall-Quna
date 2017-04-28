package com.android.juzbao.activity.circle;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.base.BaseActivity;
import com.android.quna.activity.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SelectPictureActivity extends BaseActivity implements OnItemClickListener,OnClickListener {
    // 最多选择图片的个数
    private static int MAX_NUM = 9;
    private static final int TAKE_PICTURE = 520;
    public static final String INTENT_MAX_NUM = "intent_max_num";
    public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
    ImageView selestPrctureBack;
    TextView selestPrctureOkGo;
    GridView selestPrctureGridview;
    private PictureAdapter adapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private ArrayList<ImageFloder> mDirPaths;
    private ImageFloder imageAll, currentImageFolder;
    //已选择的图片
    private ArrayList<String> selectedPicture;
    private String cameraPath = null;
    private int isExisted;
    private ArrayList<String> existedPictureList;
    private ContentResolver mContentResolver;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        selestPrctureBack = (ImageView) findViewById(R.id.selest_prcture_back);
        selestPrctureOkGo = (TextView) findViewById(R.id.selest_prcture_ok_go);
        selestPrctureGridview = (GridView) findViewById(R.id.selest_prcture_gridview);
        mDirPaths = new ArrayList<>();
        selectedPicture = new ArrayList<>();
        existedPictureList = new ArrayList<>();
        //TODO
        MAX_NUM = getIntent().getIntExtra(INTENT_MAX_NUM, MAX_NUM);
        existedPictureList = getIntent().getExtras().getStringArrayList("allSelectedPicture");
        isExisted = existedPictureList.size();
        imageAll = new ImageFloder();
        adapter = new PictureAdapter();
        tmpDir = new HashMap<>();
        mContentResolver = getContentResolver();
    }

    /**
     * 设置数据
     */
    @Override
    protected void setData() {
        imageAll.setDir("全部文件夹");
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        selestPrctureOkGo.setText("完成 " + isExisted + "/" + MAX_NUM);
        selestPrctureGridview.setAdapter(adapter);
        getThumbnail();
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        selestPrctureGridview.setOnItemClickListener(this);
        selestPrctureOkGo.setOnClickListener(this);
        selestPrctureBack.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_picture;
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    @Override
    protected boolean setImmersive() {
        return true;
    }

    /**
     * 使用相机拍照
     */
    protected void goCamare() {
        if (isExisted + selectedPicture.size() + 1 > MAX_NUM) {
            Toast.makeText(this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 用于拍照时获取输出的Uri
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && cameraPath != null) {
            selectedPicture.add(cameraPath);
            Intent data2 = new Intent();
            data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }

    /**
     * 这里点击完成的相应事件
     */
    private void ok() {
        Intent data = new Intent();
        data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
        setResult(RESULT_OK, data);
        finishActivity();
    }

    /**
     * 判断使用相机
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            goCamare();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selest_prcture_back:
                finish();
                break;
            case R.id.selest_prcture_ok_go:
                ok();
                break;
        }
    }

    /**
     * 展示所有图片数据的适配器
     */
    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //自定义的一个类用来缓存convertview
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(SelectPictureActivity.this, R.layout.grid_item_picture, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置第一个，拍摄图片
            if (position == 0) {
                //TODO　照相机
                holder.selectPictureImage.setImageResource(R.drawable.xiangji);
                holder.selectPictureImage.setScaleType(ImageView.ScaleType.CENTER);
                holder.selectPictureCheckButton.setVisibility(View.INVISIBLE);
            } else {
                // 除去拍摄相机
                position = position - 1;
                holder.selectPictureCheckButton.setVisibility(View.VISIBLE);

                final ImageItem item = currentImageFolder.images.get(position);
                //显示图片
                Glide.with(SelectPictureActivity.this).load("file://" + item.path).into(holder.selectPictureImage);
                //是否选中
                boolean isSelected = (selectedPicture.contains(item.path) || existedPictureList.contains(item.path));
                holder.selectPictureCheckButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //若选中的图片多于所设置的上限，不再加入
                        if (!v.isSelected() && isExisted + selectedPicture.size() + 1 > MAX_NUM) {
                            Toast.makeText(SelectPictureActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //二次选择，移除
                        if (selectedPicture.contains(item.path) || existedPictureList.contains(item.path)) {
                            selectedPicture.remove(item.path);
                        } else {
                            //加入数组
                            selectedPicture.add(item.path);
                        }
                        selestPrctureOkGo.setEnabled(selectedPicture.size() > 0);
                        selestPrctureOkGo.setText("完成" + (selectedPicture.size() + isExisted) + "/" + MAX_NUM);

                        v.setSelected(selectedPicture.contains(item.path) || existedPictureList.contains(item.path));
                    }
                });
                holder.selectPictureCheckButton.setSelected(isSelected);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView selectPictureImage;
            Button selectPictureCheckButton;

            ViewHolder(View view) {
                selectPictureImage = (ImageView) view.findViewById(R.id.select_picture_image);
                selectPictureCheckButton = (Button) view.findViewById(R.id.select_picture_check_button);
            }
        }
    }

    /**
     * 获取缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");

        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                imageAll.images.add(new ImageItem(path));

                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFloder imageFloder;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }

                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        tmpDir = null;
    }

    class ImageFloder {
        /**
         * 图片的文件夹路径
         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            if(lastIndexOf==-1){
                this.name = dir;
            }else {
                this.name = this.dir.substring(lastIndexOf);
            }
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {
        String path;
        public ImageItem(String p) {
            this.path = p;
        }
    }

}
