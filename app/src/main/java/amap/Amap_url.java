package amap;

import java.util.Random;

/**
 * Created by test001 on 2015/11/2.
 */
public class Amap_url {
    private Amap_serviceType _mapType;
    private int _level;
    private int _col;
    private int _row;

    public Amap_url(int level, int col, int row, Amap_serviceType mapType) {
        this._level = level;
        this._col = col;
        this._row = row;
        this._mapType = mapType;
    }

    public String generatUrl() {
        /**
         * 高德地图
         * */
        StringBuilder url = new StringBuilder("http://webst0");
        Random random = new Random();
        int subdomain = (random.nextInt(4) + 1);
        url.append(subdomain);
        switch (this._mapType) {
            case Amap_c:
                url.append(".is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=6&x=").append(this._col).append("&y=").append(this._row).append("&z=").append(this._level);
                break;
            case Amap_r:
                url.append(".is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x=").append(this._col).append("&y=").append(this._row).append("&z=").append(this._level);
                break;
            case Amap_test:
                url = null;
                url.append("http://");
            default:
                return null;
        }
        return url.toString();
        //谷歌地图
//        String url;
//        switch (this._mapType) {
//            case Amap_c:
//                url = "http://"
//                        + "www.google.cn/maps/vt?lyrs=s@187&gl=cn&x=" + _col + "&y=" + _row
//                        + "&z=" + _level;
//                break;
//            case Amap_r:
//                //谷歌道路图暂时没找到地址，暂用高德道路图
//                StringBuilder t_url = new StringBuilder("http://webst0");
//                Random random = new Random();
//                int subdomain = (random.nextInt(4) + 1);
//                t_url.append(subdomain);
//                t_url.append(".is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x=").append(this._col).append("&y=").append(this._row).append("&z=").append(this._level);
//                url = t_url.toString();
//                break;
//            default:
//                return null;
//        }
//        return url;
    }
}
