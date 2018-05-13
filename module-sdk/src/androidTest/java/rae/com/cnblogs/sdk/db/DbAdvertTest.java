//package rae.com.cnblogs.sdk.db;
//
//import android.util.Log;
//
//import com.rae.cnblogs.sdk.CnblogsApiFactory;
//import com.rae.cnblogs.sdk.api.ICategoryApi;
//import com.rae.cnblogs.sdk.bean.AdvertBean;
//import com.rae.cnblogs.sdk.bean.CategoryBean;
//import com.rae.cnblogs.sdk.db.DbAdvert;
//import com.rae.cnblogs.sdk.db.DbFactory;
//import com.rae.core.sdk.ApiUiArrayListener;
//import com.rae.core.sdk.exception.ApiException;
//
//import org.junit.Test;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import rae.com.cnblogs.sdk.BaseTest;
//
///**
// * 广告测试
// * Created by ChenRui on 2017/1/25 0025 14:47.
// */
//public class DbAdvertTest extends BaseTest {
//
//    private DbAdvert mAdvert;
//
//    @Override
//    public void setup() {
//        super.setup();
//        mAdvert = DbFactory.getInstance().getAdvert();
//    }
//
//    @Test
//    public void testAdd() throws InterruptedException {
//        AdvertBean data = new AdvertBean();
//        data.setAdId("1");
//        data.setAdName("test");
//        data.setAd_type("CNBLOG_LAUNCHERa");
//        data.setJumpType("URL");
//        data.setImageUrl("http://test.com");
//        mAdvert.save(data);
//        AdvertBean ad = mAdvert.getLauncherAd();
//
//        Log.d("rae", "测试完成！" + (ad == null ? "NULL" : ad.getAdName()));
//    }
//
//
//    @Test
//    public void testAddCategory() {
//        CategoryBean category = new CategoryBean();
//        category.setName("test");
//        category.save();
//
//    }
//
//
//    @Test
//    public void testSaveCategory() throws InterruptedException {
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        ICategoryApi categoryApi = CnblogsApiFactory.getInstance(mContext).getCategoryApi();
//        categoryApi.getCategory(new ApiUiArrayListener<CategoryBean>() {
//            @Override
//            public void onApiFailed(ApiException ex, String msg) {
//                countDownLatch.countDown();
//            }
//
//            @Override
//            public void onApiSuccess(List<CategoryBean> data) {
////                mAdvert.save();
//                countDownLatch.countDown();
//            }
//        });
//        countDownLatch.await(10, TimeUnit.MINUTES);
//    }
//
//    @Test
//    public void testFindCategory() {
//        AdvertBean category = mAdvert.getLauncherAd();
////        for (CategoryBean category : list) {
////            Log.d("RAe", category.getCategoryId() + " = " + category.getName());
////        }
//    }
//
//}
