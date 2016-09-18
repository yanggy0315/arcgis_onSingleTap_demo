package tools;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;

/**
 * Created by yanggy on 2016/9/18.
 * email: 76756382@qq.com
 */
public class CustomWKT {
    public ArrayList<Point> getMultiPontByPolygon(String wkt) {
        ArrayList<Point> mPoints = new ArrayList<Point>();
        String[] strList = wkt.split(",");

        for (int i = 0; i < strList.length; i++) {
            String item = strList[i].trim();
            String[] items = item.split(" ");
            Point point = new Point(Double.parseDouble(items[0]), Double.parseDouble(items[1]));
            Point new_point = (Point) GeometryEngine.project(point,
                    SpatialReference.create(4326), SpatialReference.create(3857));
            mPoints.add(new_point);
        }

        return mPoints;
    }
}
