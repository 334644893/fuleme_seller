package com.fuleme.business.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.activity.ClerkManagementActivity;
import com.fuleme.business.bean.CMBean;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to
 * create and use your own look & feel.
 * <p>
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a> antoine vianey
 */
public class AddClerkDialog extends Dialog {

    public AddClerkDialog(Context context, int theme) {
        super(context, theme);
    }

    public AddClerkDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
        int type = 1;//添加类型,默认为店长
        public final static int MANAGER = 1;//店长
        public final static int ASSISTANT = 2;//店员
        TextView tvStore;
        EditText etPhone;
        EditText etPassword;
        EditText etName;
        ImageView ivDianzhang;
        ImageView ivDianyuan;
        private Context context;
        private View contentView;

        private OnClickListener
                positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param listener
         * @return
         */
        public Builder setPositiveButton(OnClickListener listener) {

            this.positiveButtonClickListener = listener;
            return this;
        }

        public interface OnClickListener {
            //点击传递item信息接口
            void onClick(String etPhone, String etPassword, String etName, int state);
        }

        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Create the custom dialog
         */
        public AddClerkDialog create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final AddClerkDialog dialog = new AddClerkDialog(context,
                    R.style.Dialog);
            dialog.setCanceledOnTouchOutside(true);
            View layout = inflater.inflate(R.layout.add_clerk_custom_dialog_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            //init
            etPhone = (EditText) layout.findViewById(R.id.et_phone);
            etPassword = (EditText) layout.findViewById(R.id.et_password);
            etName = (EditText) layout.findViewById(R.id.et_name);
            tvStore = (TextView) layout.findViewById(R.id.tv_store);
            ivDianzhang = (ImageView) layout.findViewById(R.id.iv_dianzhang);
            ivDianyuan = (ImageView) layout.findViewById(R.id.iv_dianyuan);

            //initView
            tvStore.setText(ClerkManagementActivity.storeName);
            setStatetype(type);
            // set the confirm button
            layout.findViewById(R.id.ll_dianzhang)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setStatetype(MANAGER);
                        }
                    });
            layout.findViewById(R.id.ll_dianyuan)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setStatetype(ASSISTANT);
                        }
                    });
            layout.findViewById(R.id.positiveButton)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(
                                    etPhone.getText().toString(),
                                    etPassword.getText().toString(),
                                    etName.getText().toString(),
                                    type);
                        }
                    });


            dialog.setContentView(layout);
            return dialog;
        }

        /**
         * 改变店员店长
         *
         * @param state 0:店长 1：店员
         */
        private void setStatetype(int state) {
            type = state;
            switch (state) {

                case MANAGER:
                    ivDianzhang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon40));
                    ivDianyuan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon41));
                    break;
                case ASSISTANT:
                    ivDianzhang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon41));
                    ivDianyuan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon40));
                    break;
            }
        }
    }

}
