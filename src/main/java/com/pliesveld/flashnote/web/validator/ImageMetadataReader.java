package com.pliesveld.flashnote.web.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Initializes ImageIO to support only JPEG and PNG images.
 * Provides wrapper methods for accessing meta data of images.
 * Used to validate the user supplied image data is actually
 * image data.
 */
public class ImageMetadataReader {
    private static final Logger LOG = LogManager.getLogger();

    private static final String[] IMAGE_FILE_SUFFIXES;
    private static final String[] IMAGE_MIME_TYPES;

    static {
        IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
        iioRegistry.getCategories().forEachRemaining((c) ->
                iioRegistry.getServiceProviders(c,true).forEachRemaining((it) ->

                {
                    LOG.trace("providers {} for category {}",it,c);

                    switch(it.getClass().getPackage().getName())
                    {
                        case "com.sun.imageio.plugins.bmp":
                        case "com.sun.imageio.plugins.gif":
                        case "com.sun.imageio.plugins.wbmp":
                            LOG.trace("removing provider {}", it);
                            iioRegistry.deregisterServiceProvider(it);
                            break;
                    }
                })
            );


        IMAGE_FILE_SUFFIXES = ImageIO.getReaderFileSuffixes();
        IMAGE_MIME_TYPES = ImageIO.getReaderMIMETypes();
    }


    /**
     *
     * @param name filename
     * @param contents image data as byte array
     * @param mime_type content type of image
     * @return Image metadata
     * @throws IOException
     */
    static public ImageMetaData readImageMetaData(String name, byte[] contents, String mime_type) throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contents);
        ImageInputStream iis = ImageIO.createImageInputStream(byteArrayInputStream);
        Iterator<ImageReader> readers = null;

        if(StringUtils.hasText(mime_type)) {
            for(String mime_supported : IMAGE_MIME_TYPES)
            {
                if(mime_supported.equalsIgnoreCase(mime_type))
                {
                    readers = ImageIO.getImageReadersByMIMEType(mime_type);
                    break;
                }
            }

        }

        if(readers == null && StringUtils.hasText(name)) {
            for(String suffix : IMAGE_FILE_SUFFIXES) {
                if(StringUtils.endsWithIgnoreCase(name,suffix))
                {
                    readers = ImageIO.getImageReadersBySuffix(suffix);
                    break;
                }
            }
        }

        if( readers == null ) {
            readers = ImageIO.getImageReaders(iis);
        }

        if(readers.hasNext())
        {
            //pick the first available ImageReader
            ImageReader reader = readers.next();

            //attach source to reader
            reader.setInput(iis,true);

            return new ImageMetaData(reader.getFormatName(),reader.getWidth(0),reader.getHeight(0),reader.getAspectRatio(0));

            // TODO: verify user supplied mime_type is mime type returned by image provider
            // TODO: verify user supplied file suffix is mime type returned by image provider
        }
        throw new IOException("Could not read file contents");
    }

    /**
     * Convience methods
     */
    static public ImageMetaData readImageMetaData(byte[] contents) throws IOException {
        return readImageMetaData(null,contents,null);
    }

    static public ImageMetaData readImageMetaData(String name, byte[] contents) throws IOException {
        return readImageMetaData(name,contents,null);
    }

    static public ImageMetaData readImageMetaData(byte[] contents, String mime_type) throws IOException {
        return readImageMetaData(null,contents,mime_type);
    }

}

class ImageMetaData
{
    final private String formatName;
    final private int width;
    final private int height;
    final private float aspectRatio;

    ImageMetaData(String formatName, int width, int height, float aspectRatio) {
        this.formatName = formatName;
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
    }

    public String getFormatName() {
        return formatName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public String toString() {
        return "ImageMetaData{" +
                "formatName='" + formatName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", aspectRatio=" + aspectRatio +
                '}';
    }
}