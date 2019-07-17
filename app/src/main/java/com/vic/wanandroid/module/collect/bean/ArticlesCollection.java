package com.vic.wanandroid.module.collect.bean;

import java.util.List;

public class ArticlesCollection {

    /**
     * curPage : 1
     * datas : [{"author":"咻咻ing","chapterId":318,"chapterName":"搭建博客","courseId":13,"desc":"","envelopePic":"","id":22349,"link":"https://xiuxiuing.gitee.io/blog/2018/08/08/giteepage/","niceDate":"2018-08-22","origin":"","originId":3279,"publishTime":1534876325000,"title":"使用Gitee+Hexo搭建个人博客","userId":4119,"visible":0,"zan":0},{"author":"crazysunj","chapterId":294,"chapterName":"完整项目","courseId":13,"desc":"[开源项目] 一款程序员日常放松的App，基于Material Design + MVP-Clean + Weex + RxJava2 + Retrofit + Dagger2 + Glide + Okhttp + MTRVA + BRVAH + 炫酷控件 + 炫酷动画\r\n","envelopePic":"http://wanandroid.com/blogimgs/e3c873e3-883e-4bdb-b982-d48094c1f72e.png","id":22346,"link":"http://www.wanandroid.com/blog/show/2302","niceDate":"2018-08-21","origin":"","originId":3296,"publishTime":1534864949000,"title":"一款程序员日常放松的App CrazyDaily","userId":4119,"visible":0,"zan":0}]
     * offset : 0
     * over : true
     * pageCount : 1
     * size : 20
     * total : 2
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<CollectBean> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CollectBean> getDatas() {
        return datas;
    }

    public void setDatas(List<CollectBean> datas) {
        this.datas = datas;
    }
}
