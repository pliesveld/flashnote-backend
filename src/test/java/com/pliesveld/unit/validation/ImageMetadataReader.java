package com.pliesveld.unit.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by happs on 2/20/16.
 */
public class ImageMetadataReader {
    private static String[] IMAGES = { "1920x1080.bmp", "1920x1080.jpg", "1920x1080.png" };

    private static final Logger LOG = LogManager.getLogger();

    public ImageMetadataReader() {
        URL baseURL = ImageMetadataReader.class.getResource("");
        LOG.debug("Checking for test resources in {}",baseURL);

        for(String image : IMAGES)
        {
            URL fileURL = ImageMetadataReader.class.getResource(image);
            if(fileURL == null)
            {
                LOG.error("Could not find {}",image);
                continue;
            }

            File file = new File(fileURL.getFile());
            if(!file.exists())
            {
                LOG.warn("File {0} does not exist.",fileURL);
                continue;
            }

            try {
                ImageInputStream iis = ImageIO.createImageInputStream(file);
                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

                if(readers.hasNext())
                {
                    //pick the first available ImageReader
                    ImageReader reader = readers.next();

                    //attach source to reader
                    reader.setInput(iis,true);

                    LOG.info("Image format: {} dimensions {}x{}",reader.getFormatName(),
                            reader.getWidth(reader.getMinIndex()), reader.getHeight(reader.getMinIndex()));

                    //read metadata of first image
                    IIOMetadata metadata = reader.getImageMetadata(reader.getMinIndex());

                    if(metadata == null)
                        throw new IOException("could not get Metadata");

                    String[] names = metadata.getMetadataFormatNames();

                    for(String name : names) {
                        LOG.info("Format name: " + name);
                        displayMetadata(metadata.getAsTree(name));
                    }
                }
            } catch (IOException e) {
                LOG.warn("Error getting ImageInputStream", e.getMessage());
                e.printStackTrace();
            }




        }

    }

    private void displayMetadata(Node asTree) {
        if(asTree == null)
            return;

        LOG.info("Exaiming {} type {} value {}",asTree.getNodeName(), asTree.getNodeType(),asTree.getNodeValue());

        NodeList nodeList = asTree.getChildNodes();
        int length = nodeList.getLength();
        /*
        for(int i = 0; i < length; i++)
        {
            Node next = nodeList.item(i);
            displayMetadata(next);
        }*/

        displayMetadata(asTree.getNextSibling());
    }

    public static void main(String[] args) {
        ImageMetadataReader imr = new ImageMetadataReader();
    }

}
