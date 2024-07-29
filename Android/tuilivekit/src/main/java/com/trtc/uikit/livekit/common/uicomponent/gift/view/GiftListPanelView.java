package com.trtc.uikit.livekit.common.uicomponent.gift.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.trtc.uikit.livekit.common.uicomponent.gift.service.GiftPresenter;
import com.trtc.uikit.livekit.common.uicomponent.gift.view.adapter.GiftViewPagerAdapter;
import com.trtc.uikit.livekit.common.uicomponent.gift.model.TUIGift;
import com.trtc.uikit.livekit.common.uicomponent.gift.model.TUIGiftUser;
import com.trtc.uikit.livekit.common.utils.LiveKitLog;

import java.util.ArrayList;
import java.util.List;


public class GiftListPanelView extends ViewPager implements IGiftListPanelView {
    private static final String TAG     = "GiftPanelView";
    private static final int    COLUMNS = 4;
    private static final int    ROWS    = 2;

    private GiftViewPagerManager mGiftViewManager;
    private List<View>           mGiftViewList;
    private Context            mContext;
    private GiftPresenter      mPresenter;
    private OnSendGiftListener mOnSendGiftListener;

    public GiftListPanelView(Context context) {
        super(context);
    }

    public GiftListPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mGiftViewList = new ArrayList<>();
    }

    public void setGiftModelSource(List<TUIGift> giftDataSource) {
        if (giftDataSource == null || giftDataSource.isEmpty()) {
            LiveKitLog.warn(TAG + " giftModelSource empty!");
            return;
        }
        if (mGiftViewManager == null) {
            mGiftViewManager = new GiftViewPagerManager();
            mGiftViewManager.setGiftClickListener((position, giftModel) -> {
                if (mOnSendGiftListener != null) {
                    mOnSendGiftListener.onSendGift(giftModel, 1);
                }
            });
        }
        if (mGiftViewList != null && !mGiftViewList.isEmpty()) {
            mGiftViewList.clear();
        }
        int pageSize = mGiftViewManager.getPagerCount(giftDataSource.size(), COLUMNS, ROWS);
        // Count the number of pages
        for (int i = 0; i < pageSize; i++) {
            mGiftViewList.add(mGiftViewManager.viewPagerItem(mContext, i, giftDataSource, COLUMNS, ROWS));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
        }
        GiftViewPagerAdapter mGiftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViewList);
        this.setAdapter(mGiftViewPagerAdapter);
        this.setCurrentItem(0);
    }

    public void init(String groupId) {
        mPresenter = new GiftPresenter(groupId);
    }

    @Override
    public void sendGift(TUIGift gift, int giftCount, TUIGiftUser receiver) {
        mPresenter.sendGroupGiftMessage(gift, receiver, giftCount);
    }

    public void setListener(OnSendGiftListener listener) {
        mOnSendGiftListener = listener;
    }

    public interface OnSendGiftListener {
        void onSendGift(TUIGift gift, int giftCount);
    }
}
