package f.star.iota.milk.ui.moeimg.moe;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import f.star.iota.milk.base.PVContract;
import f.star.iota.milk.base.StringPresenter;

public class MoeimgPresenter extends StringPresenter<List<MoeimgBean>> {

    public MoeimgPresenter(PVContract.View view) {
        super(view);
    }

    @Override
    protected List<MoeimgBean> dealResponse(String s, HashMap<String, String> headers) {
        List<MoeimgBean> list = new ArrayList<>();
        Elements select = Jsoup.parse(s).select("#main-2 > div.post");
        for (Element element : select) {
            MoeimgBean bean = new MoeimgBean();
            String preview = element.select("div.box.list > a > div > img").attr("src");
            bean.setPreview(preview);
            String url = element.select("h2 > a").attr("href");
            bean.setUrl(url);
            bean.setHeaders(headers);
            String description = element.select("h2 > a").text();
            bean.setDescription(description);
            String date = element.select("div.blog_info > ul > li.cal").text();
            if (date.contains("日")) {
                date = date.substring(0, date.indexOf("日") + 1);
            }
            bean.setDate(date);
            list.add(bean);
        }
        return list;
    }
}
