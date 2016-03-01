package com.pliesveld.flashnote.logging;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

//TODO: https://logging.apache.org/log4j/2.0/manual/filters.html#MarkerFilter
//TODO: https://logging.apache.org/log4j/2.0/manual/markers.html

public class Markers {

    public static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    public static final Marker UPDATE_MARKER = MarkerManager.getMarker("SQL_UPDATE").addParents(SQL_MARKER);
    public static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").addParents(SQL_MARKER);
    
}
