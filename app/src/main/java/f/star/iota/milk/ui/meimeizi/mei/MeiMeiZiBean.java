package f.star.iota.milk.ui.meimeizi.mei;


import f.star.iota.milk.base.BaseBean;

class MeiMeiZiBean extends BaseBean {
    private String preview;
    private String url;
    private String description;
    private String date;

    MeiMeiZiBean() {
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
