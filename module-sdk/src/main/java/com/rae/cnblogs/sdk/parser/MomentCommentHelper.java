package com.rae.cnblogs.sdk.parser;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 闪存评论解析
 * Created by ChenRui on 2017/11/2 0002 17:48.
 */
public class MomentCommentHelper {

    /**
     * 解析评论
     *
     * @param element 节点
     */
    @Nullable
    public List<MomentCommentBean> parseCommentInList(Element element) {

        // 解析评论
        Elements commentLiElements = element.select(".feed_ing_comment_block li");

        // 闪存ID
        String ingId = ApiUtils.getNumber(element.select(".feed_ing_comment_block ul").attr("id"));

        if (commentLiElements.size() > 0) {
            List<MomentCommentBean> commentList = new ArrayList<>();
            for (Element commentLiElement : commentLiElements) {
                MomentCommentBean commentBean = new MomentCommentBean();

                // 没有更多的评论
                if (commentLiElement.select("a").text().contains("浏览更多")) {
                    commentBean.setContent(commentLiElement.text());
                    commentBean.setId("more");
                    commentList.add(commentBean);
                    continue;
                }

                // 评论ID
                String commentId = ApiUtils.getNumber(commentLiElement.attr("id"));
                String userAlias = ApiUtils.getUserAlias(commentLiElement.select(".ing_reply").attr("onclick"));
                String content = commentLiElement.select(".ing_comment").text();
                if (TextUtils.isEmpty(commentId)) continue;

                // @用户处理
                Elements atUserElement = commentLiElement.select(".ing_comment a");
                if (atUserElement.text().contains("@")) {
                    commentBean.setAtAuthorName(atUserElement.text());
                    commentBean.setAtUserAlias(ApiUtils.getBlogApp(atUserElement.attr("href")));
                }

                commentBean.setId(commentId);
                commentBean.setIngId(ingId);
                commentBean.setUserAlias(userAlias);
                commentBean.setContent(content);
                commentBean.setAuthorName(commentLiElement.select("#comment_author_" + commentId).text());
                commentBean.setBlogApp(ApiUtils.getBlogApp(commentLiElement.select("#comment_author_" + commentId).attr("href")));
                commentBean.setPostTime(commentLiElement.select(".ing_comment_time").text().replace("回应于", ""));
                commentList.add(commentBean);
            }

            return commentList;
        }

        return null;
    }

    /**
     * 闪存详情里面解析评论
     */
    public void parseCommentInDetail(Element element, MomentBean m) {
        Elements elements = element.select(".comment_list_block li");
        if (elements.size() <= 0) {
            return;
        }

        List<MomentCommentBean> commentList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (Element commentLiElement : elements) {
            MomentCommentBean commentBean = new MomentCommentBean();

            // 评论ID
            String commentId = ApiUtils.getNumber(commentLiElement.attr("id"));
            if (TextUtils.isEmpty(commentId)) continue;


            Matcher matcher = Pattern.compile("\\d+").matcher(commentLiElement.select(".gray3").attr("onclick"));
            int i = 0;
            while (matcher.find()) {
                String text = matcher.group();
                if (i == 0) {
                    commentBean.setIngId(text);
                }
                if (i == 2) {
                    commentBean.setUserAlias(text);
                }
                i++;
            }

            commentBean.setId(commentId);
            commentBean.setAvatar(commentLiElement.select(".ing_comment_face").attr("src"));
            commentBean.setAuthorName(commentLiElement.select("#comment_author_" + commentId).text());
            commentBean.setBlogApp(ApiUtils.getBlogApp(commentLiElement.select("#comment_author_" + commentId).attr("href")));
            commentBean.setPostTime(commentLiElement.select(".text_green").text());


            // @用户处理
            Elements atUserElement = commentLiElement.select("a:contains(@)");
            if (atUserElement.size() > 0) {
                commentBean.setAtAuthorName(atUserElement.text());
                commentBean.setAtUserAlias(ApiUtils.getBlogApp(atUserElement.attr("href")));
            }

            // 内容处理

            Elements divElements = commentLiElement.select("div");
            int size = divElements.size();
            Elements bdo = commentLiElement.select("bdo");
            if (bdo.size() > 0) {
                commentBean.setContent(bdo.text()); // 详情里面的
            } else if (size <= 0) {
                commentBean.setContent(commentLiElement.text());
            } else {
                Element div = divElements.get(0);
                List<TextNode> textNodes = div.textNodes();
                sb.delete(0, sb.length()); // 删除之前的

                // 添加@用户
                if (!TextUtils.isEmpty(commentBean.getAtAuthorName())) {
                    sb.append(commentBean.getAtAuthorName());
                    sb.append(" ");
                }

                for (TextNode node : textNodes) {
                    String text = node.text().trim();
                    if (TextUtils.isEmpty(text)) continue;
                    if (text.startsWith("：") || text.startsWith(":")) text = text.substring(1);
                    sb.append(text);
                }
                commentBean.setContent(sb.toString());
            }


            commentList.add(commentBean);
        }
        m.setCommentList(commentList);
    }


    public void parseImageList(MomentBean m, Elements body) {
        // 解析图片
        String content = m.getContent();
        int startIndex = content.indexOf("#img");
        int endIndex = content.indexOf("#end");
        int linkSize = body.size(); // 图片链接

        if (startIndex > 0 && endIndex > 0 && linkSize > 0) {
            String json = content.substring(startIndex + 4, endIndex);
//            try {
//                JSONArray array = new JSONArray(json);
//                int length = array.length();
            List<String> imageList = new ArrayList<>();
            m.setImageList(imageList);

            for (int i = 0; i < linkSize; i++) {
                Element a = body.get(i);
                String href = a.attr("href");
                if (TextUtils.isEmpty(href)) continue;
                imageList.add(href);
            }
            // 去除图片标记
            m.setContent(content.substring(0, startIndex));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        // Android标签处理
        String androidTag = "[来自Android客户端]";
        if (m.getContent().contains(androidTag) || (startIndex > 0 && endIndex > 0)) {
            // 来自安卓客户端
            m.setAndroidClient(true);
            // 去除标签
            m.setContent(m.getContent().replace(androidTag, ""));
        }

    }

}
