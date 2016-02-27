package com.pliesveld.flashnote.web.validator;


import com.pliesveld.spring.SpringTestConfig;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class ImageMetadataReaderTest {
    private static final Logger LOG = LogManager.getLogger();

    private static String[] IMAGES = { "1920x1080.bmp", "1920x1080.jpg", "1920x1080.png" };

    @Test
    public void ImageMetadataReaderTest() {
        URL baseURL = ImageMetadataReaderTest.class.getResource("");
        LOG.debug("Checking for test resources in {}",baseURL);

        for(String image : IMAGES)
        {
            URL fileURL = ImageMetadataReaderTest.class.getResource(image);
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
                byte[] contents = openToByteArray(file);
                ImageMetaData imd = ImageMetadataReader.readImageMetaData(file.getName(), contents);
                LOG.info(imd);
            } catch(IOException ex) {
                LOG.error("reader could not open {}",file.getName());
            }
        }

        logImageSupport();

    }



    byte[] openToByteArray(File file) throws IOException {
        return IOUtils.toByteArray(new FileInputStream(file));
    }

    void logImageSupport()
    {
        LOG.debug("Image reader supported formats {}",ImageIO.getReaderFormatNames());
        LOG.debug("Image reader supported file extensions {}",ImageIO.getReaderFileSuffixes());
        LOG.debug("Image reader supported MIME types {}",ImageIO.getReaderMIMETypes());
    }


    private void printMetadata(ImageReader reader) throws IOException {

        IIOMetadata imageMetadata = reader.getImageMetadata(reader.getMinIndex());
        //read metadata of first image
        IIOMetadata metadata = reader.getImageMetadata(reader.getMinIndex());

        if(metadata == null)
            throw new IOException("could not get Metadata");

        String[] metaDataFormatNames = metadata.getMetadataFormatNames();

        for(String meta : metaDataFormatNames)
        {
            IIOMetadataFormat iioMetadataFormat = metadata.getMetadataFormat(meta);
            LOG.debug("root " + iioMetadataFormat.getRootName());
            Node metaNode = metadata.getAsTree(meta);
            displayMetadata(metaNode);
        }
    }


    private void displayMetadata(Node node) {
        if(node == null)
            return;

        logNode(node);


        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();

        for(int i = 0; i < length; i++)
        {
            Node next = nodeList.item(i);
            displayMetadata(next);
        }

        displayMetadata(node.getNextSibling());
    }

    private void logNode(Node node) {
        String name = node.getNodeName();
        short type = node.getNodeType();
        String value = node.getNodeValue();

        boolean has_value = StringUtils.hasText(value) && !"null".equalsIgnoreCase(value);

        //LOG.debug("Node {}", has_value ? (name + " " + has_value) : name);
    }

}
