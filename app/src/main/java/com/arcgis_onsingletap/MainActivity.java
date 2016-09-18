package com.arcgis_onsingletap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amap.Amap_serviceLayer;
import amap.Amap_serviceType;
import tools.CustomWKT;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "===yanggy===";

    Context mContext;
    Button button;

    MapView mMapView;
    Amap_serviceLayer amap_c;
    Amap_serviceLayer amap_r;
    Double[] box = {58.6230, 7.9287, 142.91020, 58.6037};
    GraphicsLayer graphicsLayer = null;

    private final OnSingleTapListener mapOnsigleTapListener = new OnSingleTapListener() {
        @Override
        public void onSingleTap(float v, float v1) {
            Log.d(TAG,"<onSingleTap> x:"+v+",y:"+v1+" clicked!!!");
            // Find out if we tapped on a Graphic
            int[] graphicIDs = graphicsLayer.getGraphicIDs(v,v1,25);
            if (graphicIDs != null && graphicIDs.length >0){
                // If there is more than one graphic, only select the first found.
                if (graphicIDs.length >1){
                    int id = graphicIDs[0];
                    graphicIDs = new int[] {id};
                }
                // Only deselect the last graphic if user has tapped a new one. App
                // remains showing the last selected nearby service information,
                // as that is the main focus of the app.
                graphicsLayer.clearSelection();

                // Select the graphic
                graphicsLayer.setSelectedGraphics(graphicIDs, true);

                // Use the graphic attributes to update the information views.
                Graphic gr = graphicsLayer.getGraphic(graphicIDs[0]);
                updateContent(gr.getAttributes());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        button = (Button) findViewById(R.id.draw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawIcon();
            }
        });

        init_map();
    }

    private void updateContent(Map<String, Object> attributes){
        String addr = (String) attributes.get("addr");
        Toast.makeText(mContext,"addr: "+addr+" clicked!!!",Toast.LENGTH_SHORT).show();
    }
    private void init_map() {
        mMapView = (MapView) findViewById(R.id.frag_map);

        //加载高德地图影像图
        amap_c = new Amap_serviceLayer(Amap_serviceType.Amap_c, box);
        mMapView.addLayer(amap_c);

        //加载高德地图道路图
        amap_r = new Amap_serviceLayer(Amap_serviceType.Amap_r, box);
        mMapView.addLayer(amap_r);

        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);

        mMapView.setOnSingleTapListener(mapOnsigleTapListener);
    }

    private void drawIcon() {
        String kunMing = "102.7220491961 25.0187032122";
        String guiYang = "106.7007926207 26.5571820723";
        String chengDu = "104.0683743993 30.6540196723";
        String chongQing = "106.5507633008 29.5647107950";
        String xiAn = "108.9622216264 34.2802998643";

        String guiYang1 = "106.9007926207 26.5571820723";
        String guiYang2 = "106.0007926207 26.5571820723";
        String guiYang3 = "106.7007926207 26.0571820723";
        String guiYang4 = "106.7007926207 26.9571820723";

        ArrayList<String> addr = new ArrayList<>();
        addr.add("kunMing");
        addr.add("guiYang");
        addr.add("chengDu");
        addr.add("chongQing");
        addr.add("xiAn");
        addr.add("guiYang1");
        addr.add("guiYang2");
        addr.add("guiYang3");
        addr.add("guiYang4");

        ArrayList<String> geoInfo = new ArrayList<>();
        geoInfo.add(kunMing);
        geoInfo.add(guiYang);
        geoInfo.add(chengDu);
        geoInfo.add(chongQing);
        geoInfo.add(xiAn);
        geoInfo.add(guiYang1);
        geoInfo.add(guiYang2);
        geoInfo.add(guiYang3);
        geoInfo.add(guiYang4);

        CustomWKT c_wkt = new CustomWKT();
        for (int i = 0; i < addr.size();i++){
            Map<String,Object> gra_attr = new HashMap<>();
            gra_attr.put("addr",addr.get(i));
            ArrayList<Point> pt = c_wkt.getMultiPontByPolygon(geoInfo.get(i));
            Graphic gra_pt = new Graphic(pt.get(0),getPicStyle(addr.get(i)),gra_attr);
            int uid = graphicsLayer.addGraphic(gra_pt);
            graphicsLayer.updateGraphic(uid, gra_pt);
        }

    }

    private PictureMarkerSymbol getPicStyle(String addr){
        PictureMarkerSymbol mks;
        if ("kunMing".equalsIgnoreCase(addr)){
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.pstation_10));
        }else if ("guiYang".equalsIgnoreCase(addr)){
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.reservoir_10));
        }else if ("chengDu".equalsIgnoreCase(addr)){
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.town_10));
        }else if ("chongQing".equalsIgnoreCase(addr)){
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.watershed_10));
        }else if ("xiAn".equalsIgnoreCase(addr)){
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.wstation_10));
        }else {
            mks = new PictureMarkerSymbol(mContext,getResources().getDrawable(R.mipmap.wstation_4));
        }
        return mks;
    }
}
