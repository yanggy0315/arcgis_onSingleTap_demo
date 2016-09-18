package amap;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by test001 on 2015/11/2.
 */
public class Amap_serviceLayer extends TiledServiceLayer {
    private Amap_serviceType _mapType;
    private TileInfo tileInfo;
    private int minLevel = 0;
    private int maxLevel = 13;
    private Double[] _box;

    public Amap_serviceLayer() {
        this(null, null, true, null);
    }

    public Amap_serviceLayer(Amap_serviceType mapType) {
        this(mapType, null, true, null);
    }

    public Amap_serviceLayer(Amap_serviceType mapType, UserCredentials usercredentials) {
        this(mapType, usercredentials, true, null);
    }

    public Amap_serviceLayer(Amap_serviceType mapType, Double[] _box) {
        this(mapType, null, true, _box);
    }

    public Amap_serviceLayer(Amap_serviceType mapType, UserCredentials usercredentials, boolean flag, Double[] box) {
        super("");
        this._mapType = mapType;
        this._box = box;
        setCredentials(usercredentials);

        if (flag)
            try {
                getServiceExecutor().submit(new Runnable() {

                    public final void run() {
                        a.initLayer();
                    }

                    final Amap_serviceLayer a;


                    {
                        a = Amap_serviceLayer.this;
                        //super();
                    }
                });
                return;
            } catch (RejectedExecutionException _ex) {
            }
    }

    public Amap_serviceType getMapType() {
        return this._mapType;
    }


    private double[] scales = new double[]{591657527.591555,
            295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003,
            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
            288895.277144, 144447.638572, 72223.819286, 36111.909643,
            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
            1128.4971760000001};
    private double[] resolutions = new double[]{156543.03392800014,
            78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
            1222.9924525624949, 611.49622628138, 305.748113140558,
            152.874056570411, 76.4370282850732, 38.2185141425366,
            19.1092570712683, 9.55462853563415, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
            0.29858214164761665};

    private Point origin = new Point(-20037508.342787, 20037508.342787);

    private int dpi = 96;

    private int tileWidth = 256;
    private int tileHeight = 256;

    protected void initLayer() {
//        this.setDefaultSpatialReference(SpatialReference.create(4326));
//        this.setFullExtent(new Envelope(-180, -90, 180, 90));
//        this.buildTileInfo();
//        //this.setDefaultSpatialReference(SpatialReference.create(4326));
//        this.setInitialExtent(new Envelope(103.59971618652349, 24.623580932617188, 109.58615875244169, 29.21916389465332));
//        super.initLayer();
        if (getID() == 0L) {
            nativeHandle = create();
            changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS
                    .fromInt(-1000));
        } else {
            this.setDefaultSpatialReference(SpatialReference.create(3857));
            this.setFullExtent(new Envelope(-22041257.773878,
                    -32673939.6727517, 22041257.773878, 20851350.0432886));
            //如无，则暂时固定
            if (_box == null) {
                _box = new Double[]{71.4551, 8.0157, 128.0566, 54.1367};
            }
            if (_box != null) {
                this.setInitialExtent(transform(_box));
            }
            this.setTileInfo(new TileInfo(origin, scales, resolutions,
                    scales.length, dpi, tileWidth, tileHeight));
            super.initLayer();
        }
    }

    private Envelope transform(Double[] box) {
//        String[] arr = t_config.replace("(", "").replace(")", "").split(",");
        double xmin = box[0];
        double ymin = box[1];
        double xmax = box[2];
        double ymax = box[3];

        Point min_point = new Point(xmin, ymin);
        Point min_p = (Point) GeometryEngine.project(min_point,
                SpatialReference.create(4326), SpatialReference.create(3857));

        Point max_point = new Point(xmax, ymax);
        Point max_p = (Point) GeometryEngine.project(max_point,
                SpatialReference.create(4326), SpatialReference.create(3857));
        return new Envelope(min_p.getX(), min_p.getY(), max_p.getX(), max_p.getY());
    }

    public void refresh() {
        try {
            getServiceExecutor().submit(new Runnable() {

                public final void run() {
                    if (a.isInitialized())
                        try {
                            a.b();
                            a.clearTiles();
                            return;
                        } catch (Exception exception) {
                            Log.e("ArcGIS", "Re-initialization of the layer failed.", exception);
                        }
                }

                final Amap_serviceLayer a;

                {
                    a = Amap_serviceLayer.this;
                    //super();
                }
            });
            return;
        } catch (RejectedExecutionException _ex) {
            return;
        }
    }

    final void b()
            throws Exception {

    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        if (level > maxLevel || level < minLevel)
            return new byte[0];
        String url = this.getAmapUrl(level, col, row);
        Map<String, String> map = null;
        return com.esri.core.internal.io.handler.a.a(url, map);
    }


    @Override
    public TileInfo getTileInfo() {
        return this.tileInfo;
    }

    /**
     *
     * */
    private String getAmapUrl(int level, int col, int row) {

        String url = new Amap_url(level, col, row, this._mapType).generatUrl();
        return url;
    }

    private void buildTileInfo() {
        Point originalPoint = new Point(-180, 90);
//        double[] scale={
//        		295829355.45,
//        		147914677.73,
//        		73957338.86,
//        		36978669.43,
//        		18489334.72,
//        		9244667.36,
//        		4622333.68,
//        		2311166.84,
//        		1155583.42,
//        		577791.71,
//        		288895.85,
//        		144447.93,
//        		72223.96,
//        		36111.98,
//        		18055.99,
//        		9028,
//        		4514,
//        		2257 };
//        double[] res={
//        		0.70391441567318025,
//    			0.35195720784848739,
//    			0.1759786039123464,
//    			0.087989301956173202,
//    			0.043994650989983904,
//    			0.021997325494991952,
//    			0.010998662747495976,
//    			0.005499331373747988,
//    			0.002749665686873994,
//    			0.001374832843436997,
//    			0.00068741640982119352,
//    			0.00034370821680790179,
//    			0.00017185409650664589,
//    			8.5927048253322947e-005,
//    			4.2963524126661473e-005,
//    			2.1481773960635764e-005,
//    			1.0740886980317882e-005,
//    			5.3704434901589409e-006 };
//        int levels=18;
        double[] res = {
                156543.03392800014,
                78271.516963999937, 39135.758482000092, 19567.879240999919,
                9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
                1222.9924525624949, 611.49622628138, 305.748113140558,
                152.874056570411, 76.4370282850732, 38.2185141425366,
                19.1092570712683, 9.55462853563415, 4.7773142679493699,
                2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
                0.29858214164761665
        };
        double[] scale = {
                591657527.591555,
                295828763.79577702, 147914381.89788899, 73957190.948944002,
                36978595.474472001, 18489297.737236001, 9244648.8686180003,
                4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
                288895.277144, 144447.638572, 72223.819286, 36111.909643,
                18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
                1128.4971760000001
        };
//        int levels = 21;
        int dpi = 96;
        int tileWidth = 256;
        int tileHeight = 256;
        this.tileInfo = new com.esri.android.map.TiledServiceLayer.TileInfo(originalPoint, scale, res, res.length, dpi, tileWidth, tileHeight);
        this.setTileInfo(this.tileInfo);
    }
}
