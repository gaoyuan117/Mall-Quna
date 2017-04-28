package com.android.juzbao.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.HorizontialListView;
import com.android.zcomponent.views.MeasureGridView;
import com.server.api.model.CommonReturn;
import com.server.api.model.ProductOptions;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EActivity(resName = "activity_edit_option")
public class EditOptionActivity extends BaseActivity {

    @ViewById(resName = "edtvew_option_name_show")
    EditText mEdtvewOptionName;

    @ViewById(resName = "edtvew_price_name_show")
    EditText mEdtvewOptionPrice;

    @ViewById(resName = "tvew_option_add_show")
    TextView mTvweAddOptionName;

    @ViewById(resName = "llayout_product_options")
    LinearLayout mLlayoutOptions;

    @ViewById(resName = "gridview_price_show")
    MeasureGridView mGvewPriceOption;

    private String mstrProductId;

    private List<OptionItemAdapter> mOptionAdapters = new ArrayList<>();

    private Map<Integer, ProductOptions.ProductOptionItem> mSelectOptions =
            new HashMap<Integer, ProductOptions.ProductOptionItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("编辑选项");
        mstrProductId = getIntentHandle().getString("id");
        ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ProductOptionRequest.class)) {
            ProductOptions response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                initProductOption(response.data.option, response.data.option_price);
            } else {
                dismissWaitDialog();
            }
        } else if (msg.valiateReq(ProductService.EditOptionRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "添加失败")) {
                ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
            } else {
                dismissWaitDialog();
            }
        } else if (msg.valiateReq(ProductService.EditChildOptionRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "添加失败")) {
                ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
            } else {
                dismissWaitDialog();
            }
        } else if (msg.valiateReq(ProductService.AddOptionPriceRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "添加失败")) {
                ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
            } else {
                dismissWaitDialog();
            }
        } else if (msg.valiateReq(ProductService.DelOptionRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "删除失败")) {
                ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
            } else {
                dismissWaitDialog();
            }
        } else if (msg.valiateReq(ProductService.DelOptionPriceRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "删除失败")) {
                ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), mstrProductId);
            } else {
                dismissWaitDialog();
            }
        }
    }

    private void initProductOption(
            ProductOptions.ProductOption[] productOption,
            ProductOptions.ProductPrices[] productPrices) {
        if (null == productOption) {
            return;
        }

        mOptionAdapters =
                new ArrayList<>();
        mLlayoutOptions.removeAllViews();
        for (int i = 0; i < productOption.length; i++) {
            View view =
                    LayoutInflater.from(this).inflate(
                            R.layout.llayout_add_product_option, null);
            TextView tvewTitle =
                    (TextView) view
                            .findViewById(R.id.tvew_option_title_show);
            EditText tvewName =
                    (EditText) view
                            .findViewById(R.id.edtvew_option_child_name_show);
            TextView tvewAddName =
                    (TextView) view
                            .findViewById(R.id.tvew_option_add_show);
            GridView gvewOptions =
                    (GridView) view
                            .findViewById(R.id.gridview_options_show);
            tvewTitle.setText(productOption[i].title);
            tvewAddName.setOnClickListener(new AddChildOptionClickListener(productOption[i], i, tvewName));
            if (null != productOption[i]._child && productOption[i]._child.length > 0) {
                final List<ProductOptions.ProductOptionItem> optionItems =
                        ListUtil.arrayToList(productOption[i]._child);
                final OptionItemAdapter adapter =
                        new OptionItemAdapter(this, i, optionItems);
                gvewOptions.setAdapter(adapter);
                mOptionAdapters.add(adapter);
            }
            mLlayoutOptions.addView(view);
        }

        initOptionPrice(productOption, productPrices);
    }

    private void initOptionPrice(ProductOptions.ProductOption[] productOption, ProductOptions.ProductPrices[] productPrices) {
        if (null == productPrices) {
            mGvewPriceOption.setAdapter( new OptionPriceItemAdapter(this, null));
            return;
        }
        for (int j = 0; j < productPrices.length; j++) {
            ProductOptions.ProductPrices price = productPrices[j];
            String[] priceOptions = StringUtil.splitString(price.product_option_id, ",");
            StringBuilder builder = new StringBuilder();
            builder.append("¥" + price.price);
            for (int i = 0; i < priceOptions.length; i++) {
                ProductOptions.ProductOptionItem[] optionItem = productOption[i]._child;
                if (null == optionItem) {
                    continue;
                }
                if (builder.length() != 0) {
                    builder.append(",");
                }
                for (int k = 0; k < optionItem.length; k++) {
                    if (priceOptions[i].equals(optionItem[k].id)) {
                        builder.append(optionItem[k].title);
                    }
                }
            }
            price.product_option_text = builder.toString();
        }

        List<ProductOptions.ProductPrices> optionItems =
                ListUtil.arrayToList(productPrices);
        final OptionPriceItemAdapter adapter =
                new OptionPriceItemAdapter(this, optionItems);
        mGvewPriceOption.setAdapter(adapter);
    }


    private class AddChildOptionClickListener implements View.OnClickListener {

        private EditText mTvewName;
        private int groupPosition;
        private ProductOptions.ProductOption item;

        public AddChildOptionClickListener(ProductOptions.ProductOption item, int groupPosition, EditText tvewName) {
            mTvewName = tvewName;
            this.groupPosition = groupPosition;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            String text = mTvewName.getText().toString();
            if (TextUtils.isEmpty(text)) {
                ShowMsg.showToast(EditOptionActivity.this, "请输入属性名称");
                return;
            }
            ProviderProductBusiness.queryEditChildOption(getHttpDataLoader(), "", item.id, mstrProductId, text);
        }
    }

    private class OptionPriceItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    public class OptionItemAdapter extends CommonAdapter {

        private int groupPosition;

        public OptionItemAdapter(Context context, int groupPosition, List<?> list) {
            super(context, list);
            this.groupPosition = groupPosition;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (null == convertView) {
                convertView =
                        layoutInflater.inflate(
                                R.layout.adapter_product_edit_option, null);
            }
            RelativeLayout llayoutBg = findViewById(convertView, R.id.llayout_option_bg_show);
            TextView tvewTitle = findViewById(convertView, R.id.tvew_option_title_show);
            TextView tvewDel = findViewById(convertView, R.id.tvew_option_del_show);

            final ProductOptions.ProductOptionItem optionItem =
                    (ProductOptions.ProductOptionItem) mList.get(position);
            tvewTitle.setText(optionItem.title);

            if (getSelectPosition() == position) {
                llayoutBg
                        .setBackgroundResource(R.drawable.common_round_red_bg);
                tvewTitle.setTextColor(getColor(R.color.white));
            } else {
                llayoutBg
                        .setBackgroundResource(R.drawable.common_round_gray_bg);
                tvewTitle.setTextColor(getColor(R.color.black));
            }

            tvewDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProviderProductBusiness.queryDelChildOption(getHttpDataLoader(), optionItem.id);
                    showWaitDialog(2, false, R.string.common_submit_data);
                }
            });
            llayoutBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductOptions.ProductOptionItem optionItem =
                            (ProductOptions.ProductOptionItem) getItem(position);
                    if (getSelectPosition() == position) {
                        setSelectPosition(-1);
                        mSelectOptions.remove(groupPosition);
                    } else {
                        setSelectPosition(position);
                        mSelectOptions.put(groupPosition, optionItem);
                    }
                }
            });
            return convertView;
        }
    }

    public class OptionPriceItemAdapter extends CommonAdapter {

        public OptionPriceItemAdapter(Context context, List<?> list) {
            super(context, list);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (null == convertView) {
                convertView =
                        layoutInflater.inflate(
                                R.layout.adapter_product_edit_option, null);
            }

            TextView tvewTitle =
                    (TextView) convertView
                            .findViewById(R.id.tvew_option_title_show);
            TextView tvewDel =
                    (TextView) convertView
                            .findViewById(R.id.tvew_option_del_show);
            final ProductOptions.ProductPrices optionItem =
                    (ProductOptions.ProductPrices) mList.get(position);
            tvewTitle.setText(optionItem.product_option_text);
            tvewDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProviderProductBusiness.queryDelOptionPrice(getHttpDataLoader(), optionItem.id);
                    showWaitDialog(2, false, R.string.common_submit_data);
                }
            });
            return convertView;
        }
    }

    @Click(resName = "tvew_option_add_show")
    void onClickAddOptionName() {
        String text = mEdtvewOptionName.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ShowMsg.showToast(this, "请输入选项名称");
            return;
        }
        ProviderProductBusiness.queryEditOption(getHttpDataLoader(), "", mstrProductId, text);
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    @Click(resName = "tvew_price_add_show")
    void onClickAddPriceOptionName() {

        if (null == mSelectOptions || mSelectOptions.size() == 0
                || mSelectOptions.size() != mOptionAdapters.size()) {
            ShowMsg.showToast(this, "请选择选项");
            return;
        }

        String ids = getSelectOptionIds(mSelectOptions);

        String price = mEdtvewOptionPrice.getText().toString();

        if (TextUtils.isEmpty(price)) {
            ShowMsg.showToast(this, "请输入价格");
            return;
        }

        ProviderProductBusiness.queryAddOptionPrice(getHttpDataLoader(), ids, mstrProductId, new BigDecimal(price));
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    private String getSelectOptionTitle(
            Map<Integer, ProductOptions.ProductOptionItem> selectOptions) {
        StringBuilder buffer = new StringBuilder();

        for (Iterator iterator = selectOptions.keySet().iterator(); iterator
                .hasNext(); ) {
            ProductOptions.ProductOptionItem optionItem =
                    selectOptions.get(iterator.next());
            if (null != optionItem) {
                if (buffer.length() != 0) {
                    buffer.append(" ");
                }
                buffer.append(optionItem.title);
            }
        }
        return buffer.toString();
    }

    private String getSelectOptionIds(
            Map<Integer, ProductOptions.ProductOptionItem> selectOptions) {
        StringBuilder buffer = new StringBuilder();

        for (Iterator iterator = selectOptions.keySet().iterator(); iterator
                .hasNext(); ) {
            ProductOptions.ProductOptionItem optionItem =
                    selectOptions.get(iterator.next());
            if (null != optionItem) {
                if (buffer.length() != 0) {
                    buffer.append(",");
                }
                buffer.append(optionItem.id);
            }
        }
        return buffer.toString();
    }
}
