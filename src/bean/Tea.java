package bean;

import java.util.Date;
import java.util.Map;

public class Tea {

    private int teaId; // 商品编号
    private String teaName; // 商品名称
    private double price; // 价格
    private String description; // 描述信息
    private int catalogId; // 商品分类id
    private int imgId; // 图片id
    private Date addTime; // 上架时间
    private boolean recommend; // 是否为推荐横幅

    private Catalog catalog = new Catalog();
    private UpLoadImg upLoadImg = new UpLoadImg();

    public Tea() {
    }

    public Tea(Map<String, Object> map) {
        this.teaId = (int) map.get("teaId");
        this.teaName = (String) map.get("teaName");
        this.price = (double) map.get("price");
        this.description = (String) map.get("description");
        this.addTime = (Date) map.get("addTime");
        this.catalog = new Catalog(map);
        this.upLoadImg = new UpLoadImg(map);

        // 检查 recommend 字段是否为 null
        Object recommendValue = map.get("recommend");
        if (recommendValue instanceof Boolean) {
            this.recommend = ((Boolean) recommendValue).booleanValue();
        } else {
            this.recommend = false; // 默认值为 false
        }
    }

    public int getTeaId() {
        return teaId;
    }

    public void setTeaId(int teaId) {
        this.teaId = teaId;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public UpLoadImg getUpLoadImg() {
        return upLoadImg;
    }

    public void setUpLoadImg(UpLoadImg upLoadImg) {
        this.upLoadImg = upLoadImg;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Tea [teaId=" + teaId + ", teaName=" + teaName + ", price=" + price + ", description=" + description
                + ", catalogId=" + catalogId + ", imgId=" + imgId + ", addTime=" + addTime + ", recommend=" + recommend
                + ", catalog=" + catalog + ", upLoadImg=" + upLoadImg + "]";
    }
}



