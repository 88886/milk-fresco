package f.star.iota.milk.ui.bilibili;


import com.google.gson.annotations.SerializedName;

import f.star.iota.milk.base.BaseBean;

class BilibiliBean extends BaseBean {


    @SerializedName("img_src")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
