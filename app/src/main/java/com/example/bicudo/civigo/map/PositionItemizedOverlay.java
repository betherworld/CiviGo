package com.example.bicudo.civigo.map;

import com.example.bicudo.civigo.R;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
* Created by rudid on 08.03.2017.
*/
public class PositionItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

    public PositionItemizedOverlay(Drawable pDefaultMarker, Context context) {
        super(context, pDefaultMarker);
    }
    @Override
    public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
        return false;
    }
    @Override
    protected OverlayItem createItem(int arg0) {
        return mOverlays.get(arg0);
    }
    @Override
    public int size() {
        return mOverlays.size();
    }
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
}