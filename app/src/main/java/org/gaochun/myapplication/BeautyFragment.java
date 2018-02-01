package org.gaochun.myapplication;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.gaochun.view.AutoLoadRecyclerView;
import org.gaochun.view.LoadFinishCallBack;

import java.util.List;


public class BeautyFragment extends Fragment {


    AutoLoadRecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageAdapter mAdapter;
    private LoadFinishCallBack mLoadFinisCallBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty, container, false);

        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);


        //图片显示控件
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;

        //自动加载更多
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                //mAdapter.loadNextPage();
            }
        });

        //上下拉加载控件
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.mImageUrls.clear();

                mAdapter = new ImageAdapter();
                mRecyclerView.setAdapter(mAdapter);

                if (mSwipeRefreshLayout.isRefreshing()) {//取消刷新
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setOnPauseListenerParams(ImageLoader.getInstance(), false, true);

        mAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }


    public class ImageAdapter extends RecyclerView.Adapter<ViewHolder> {

        private int page;
        private List<String> mImageUrls;
        private int lastPosition = -1;

        public ImageAdapter() {
            mImageUrls = ImageUrl.imageList();
        }

        //动画加载
        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                        .anim.item_bottom_in);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);

            holder.card.clearAnimation();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_beauty;

            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final String url = mImageUrls.get(position);
            ImageLoader.getInstance().displayImage(url, holder.img, AppAplication.mOptions);
            setAnimation(holder.card, position);
        }

        @Override
        public int getItemCount() {
            return mImageUrls.size();
        }

        /*public void loadFirst() {
            page = 1;
            loadDataByNetworkType();
        }

        public void loadNextPage() {
            page++;
            loadDataByNetworkType();
        }

        private void loadDataByNetworkType() {

            if (isNetWorkConnected(getActivity())) {

                executeRequest(new Request4FreshNews(FreshNews.getUrlFreshNews(page),
                        new Response.Listener<ArrayList<String>>() {
                            @Override
                            public void onResponse(ArrayList<String> response) {

                                mLoadFinisCallBack.loadFinish(null);//加载完成
                                if (mSwipeRefreshLayout.isRefreshing()) {//取消刷新
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                                if (page == 1) {
                                    //清除缓存
                                    mAdapter.mImageUrls.clear();
                                }
                                //数据显示
                                mAdapter.mImageUrls.addAll(response);
                                notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "加载失败", 0).show();
                        mLoadFinisCallBack.loadFinish(null);//加载完成
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }));
            }
        }*/
    }


    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private CardView card;

        public ViewHolder(View contentView) {
            super(contentView);
            img = (ImageView) contentView.findViewById(R.id.img);
            card = (CardView) contentView.findViewById(R.id.card);
        }
    }


    /**
     * 判断当前网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
