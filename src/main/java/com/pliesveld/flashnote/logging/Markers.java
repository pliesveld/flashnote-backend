package com.pliesveld.flashnote.logging;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

//TODO: https://logging.apache.org/log4j/2.0/manual/filters.html#MarkerFilter
//TODO: https://logging.apache.org/log4j/2.0/manual/markers.html

/*
    Markers are used to tag log statements with metadata allowing the filtering of log output.
 */
public class Markers {

    public static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");

    public static final Marker SQL_INIT = MarkerManager.getMarker("SQL_INIT").addParents(SQL_MARKER);
    public static final Marker SQL_PERSIST = MarkerManager.getMarker("SQL_PERSIST").addParents(SQL_MARKER);
    public static final Marker SQL_UPDATE = MarkerManager.getMarker("SQL_UPDATE").addParents(SQL_MARKER);
    public static final Marker SQL_QUERY = MarkerManager.getMarker("SQL_QUERY").addParents(SQL_MARKER);
    public static final Marker SQL_DELETE = MarkerManager.getMarker("SQL_DELETE").addParents(SQL_MARKER);;

    public static final Marker SECURITY = MarkerManager.getMarker("SECURITY");
    public static final Marker SECURITY_INIT = MarkerManager.getMarker("SECURITY_INIT").addParents(SECURITY);
    public static final Marker SECURITY_AUTH = MarkerManager.getMarker("SECURITY_AUTH").addParents(SECURITY);
    public static final Marker SECURITY_AUTH_TOKEN = MarkerManager.getMarker("SECURITY_AUTH_TOKEN").addParents(SECURITY_AUTH);

    public static final Marker SERVICE = MarkerManager.getMarker("SERVICE");
    public static final Marker SERVICE_ATTACHMENT = MarkerManager.getMarker("SERVICE_ATTACHMENT").addParents(SERVICE);

    public static final Marker AUDIT = MarkerManager.getMarker("AUDIT");

    public static final Marker OBJECT_MAPPER = MarkerManager.getMarker("OBJECT_MAPPER");
    public static final Marker OBJECT_MAPPER_INIT = MarkerManager.getMarker("OBJECT_MAPPER_INIT").addParents(OBJECT_MAPPER);
    public static final Marker OBJECT_MAPPER_ERROR = MarkerManager.getMarker("OBJECT_MAPPER_ERROR").addParents(OBJECT_MAPPER);

    public static final Marker OBJECT_MAPPER_WRITE = MarkerManager.getMarker("OBJECT_MAPPER_WRITE").addParents(OBJECT_MAPPER);
    public static final Marker OBJECT_MAPPER_READ = MarkerManager.getMarker("OBJECT_MAPPER_READ").addParents(OBJECT_MAPPER);

    public static final Marker REST_EXCEPTION = MarkerManager.getMarker("REST_EXCEPTION");
    public static final Marker REST_EXCEPTION_INTERNAL = MarkerManager.getMarker("REST_EXCEPTION_INTERNAL").addParents(REST_EXCEPTION);

    public static final Marker SERVLET = MarkerManager.getMarker("SERVLET");
    public static final Marker SERVLET_INIT = MarkerManager.getMarker("SERVLET_INIT").addParents(SERVLET);
}
