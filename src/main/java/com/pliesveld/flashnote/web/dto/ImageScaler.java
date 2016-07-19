package com.pliesveld.flashnote.web.dto;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentCategory;
import com.pliesveld.flashnote.exception.AttachmentNotSupportedException;
import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component("imageScaler")
public class ImageScaler {

    static public byte[] scaleImage(byte[] imageData, MediaType mimeType, int targetWidth) {

        targetWidth = boundedTargetWidth(targetWidth);
        BufferedImage origImage = null;
        try {
            origImage = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new AttachmentNotSupportedException("Could not read attachment", e);
        }

        String imageFormat = mimeType.getSubtype().toLowerCase();
        byte[] scaledImageInByte = null;
        ByteArrayOutputStream baos = null;

        try {
            BufferedImage scaledImage = Scalr.resize(origImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
            baos = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, imageFormat, baos);
            baos.flush();
            scaledImageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e1) {
                    e = e1;
                }

            throw new AttachmentNotSupportedException("Could not convert attachment", e);
        }

        return scaledImageInByte;
    }

    static public byte[] scaleAttachmentImage(AttachmentBinary attachmentBinary, int targetWidth) {
        assert attachmentBinary.getAttachmentType().isBinary();
        assert attachmentBinary.getAttachmentType().getCategory() == AttachmentCategory.IMAGE;
        targetWidth = boundedTargetWidth(targetWidth);
        return ImageScaler.scaleImage(attachmentBinary.getContents(), attachmentBinary.getAttachmentType().getMediatype(), targetWidth);
    }

    private static int boundedTargetWidth(int targetWidth) {
        if (targetWidth < 50) {
            return 50;
        } else if (targetWidth > 800) {
            return 800;
        }
        return targetWidth;
    }
}
