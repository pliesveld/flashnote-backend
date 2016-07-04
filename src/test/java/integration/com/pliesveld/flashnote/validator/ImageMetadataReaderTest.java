package com.pliesveld.flashnote.validator;


import com.pliesveld.flashnote.web.validator.ImageMetadata;
import com.pliesveld.flashnote.web.validator.ImageMetadataReader;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


public class ImageMetadataReaderTest {
    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void ImageMetadataReaderTest() throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("/scripts/tests/test-data/image/");
        if (!classPathResource.exists()) {
            LOG.error("Classpath not found {}", classPathResource);
            Assert.fail();
            return;
        }

        final ArrayList<Path> image_files = new ArrayList<>();

        Files.walkFileTree(classPathResource.getFile().toPath(), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                image_files.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return null;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        Assert.assertNotEquals(0, image_files.size());
        int found = 0;

        for (Path image : image_files) {
            File file = image.toFile();

            if (!file.exists()) {
                LOG.warn("File {0} does not exist.", file);
                continue;
            }

            try {
                byte[] contents = openToByteArray(file);
                ImageMetadata imd = ImageMetadataReader.readImageMetaData(file.getPath(), contents);
                LOG.info("image {} -> {}", file.getName(), imd);
                found++;
            } catch (IOException ex) {
                LOG.warn("Image reader could not open {}", file.getName());
            }
        }

        logImageSupport();
        Assert.assertNotEquals(0, found);
    }

    byte[] openToByteArray(File file) throws IOException {
        try {
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    void logImageSupport() {
        LOG.debug("Image reader supported formats {}", ImageIO.getReaderFormatNames());
        LOG.debug("Image reader supported file extensions {}", ImageIO.getReaderFileSuffixes());
        LOG.debug("Image reader supported MIME types {}", ImageIO.getReaderMIMETypes());
    }

    private void printMetadata(ImageReader reader) throws IOException {

        IIOMetadata imageMetadata = reader.getImageMetadata(reader.getMinIndex());
        //read metadata of first image
        IIOMetadata metadata = reader.getImageMetadata(reader.getMinIndex());

        if (metadata == null)
            throw new IOException("could not get Metadata");

        String[] metaDataFormatNames = metadata.getMetadataFormatNames();

        for (String meta : metaDataFormatNames) {
            IIOMetadataFormat iioMetadataFormat = metadata.getMetadataFormat(meta);
            LOG.debug("root " + iioMetadataFormat.getRootName());
            Node metaNode = metadata.getAsTree(meta);
            displayMetadata(metaNode);
        }
    }

    private void displayMetadata(Node node) {
        if (node == null)
            return;

        logNode(node);


        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();

        for (int i = 0; i < length; i++) {
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
