package com.android.juzbao.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.PictureSelectUtil;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.richededit.RichEditor;
import com.server.api.model.UploadFile;
import com.server.api.service.FileService;

public class EditDescActivity extends BaseActivity {

    public static final int CODE_EDIT_COMPLETE = 1;

    private RichEditor mEditor;

    protected PictureSelectUtil mPictureSelectUtil;

    protected String mstrFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_desc);

        mPictureSelectUtil = new PictureSelectUtil(this);
        mPictureSelectUtil.setOutParams(4, 3, 1024, 768);
        mPictureSelectUtil.setBtnOpenRecordGone();

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(18);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);

        mEditor.setFocusable(true);
        mEditor.setFocusableInTouchMode(true);
        mEditor.focusEditor();

        mEditor.setPadding(10, 10, 10, 10);
        //    mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("请输入商品描述");

        Intent intent = getIntent();
        getTitleBar().setTitleText(intent.getStringExtra("title"));
        String content = intent.getStringExtra("content");
        mEditor.setHtml(content);

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
                        "dachshund");
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });

        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        findViewById(R.id.tvew_submit_click).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickTvewSubmit();
            }
        });

        findViewById(R.id.tvew_pic_click).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPictureSelectUtil.selectPicture();
            }
        });
    }

    void onClickTvewSubmit() {
        ClientInfo.closeSoftInput(mEditor, this);
        Intent intent = new Intent();
        intent.putExtra("content", mEditor.getHtml());
        FramewrokApplication.getInstance().setActivityResult(
                CODE_EDIT_COMPLETE, intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (null == mPictureSelectUtil) {
                return;
            }
            mstrFilePath =
                    mPictureSelectUtil.getPicture(requestCode, resultCode,
                            data);
            if (!StringUtil.isEmptyString(mstrFilePath)) {
                Bitmap bitmap = BitmapFactory.decodeFile(mstrFilePath);
                if (null != bitmap) {
                    sendPostRequest(mstrFilePath);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (null != mPictureSelectUtil) {
                    mPictureSelectUtil.deleteTempFile();
                }
            }
        }
    }

    public void sendPostRequest(String filePath) {
        showWaitDialog(1, false, "正在上传文件");

        FileService.PostFileRequest request = new FileService.PostFileRequest();
        getHttpDataLoader().doPostFileProcess(request, filePath, String.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(FileService.PostFileRequest.class)) {
            UploadFile uploadFile = ProviderProductBusiness.parseUploadFile(this, msg);
            if (null != uploadFile && null != uploadFile.Data) {
                String path = Endpoint.HOST + uploadFile.Data.file.path;
                mEditor.insertImage(path, uploadFile.Data.file.create_time);
            }
        }
    }
}