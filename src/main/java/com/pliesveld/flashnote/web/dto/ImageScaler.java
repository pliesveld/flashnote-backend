package com.pliesveld.flashnote.web.dto;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentCategory;
import com.pliesveld.flashnote.exception.AttachmentDownloadException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component("imageScaler")
public class ImageScaler {

    static public byte[] scaleAttachmentImage(AttachmentBinary attachmentBinary, int targetWidth) {
        assert attachmentBinary.getAttachmentType().isBinary();
        assert attachmentBinary.getAttachmentType().getCategory() == AttachmentCategory.IMAGE;

        if (targetWidth < 50) {
            targetWidth = 50;
        }

        BufferedImage origImage = null;
        try {
            origImage = ImageIO.read(new ByteArrayInputStream(attachmentBinary.getContents()));
        } catch (IOException e) {
            throw new AttachmentDownloadException("Could not read attachment", e);
        }

        byte[] scaledImageInByte = null;
        ByteArrayOutputStream baos = null;

        try {
            BufferedImage scaledImage = Scalr.resize(origImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
            baos = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, "jpg", baos);
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

            throw new AttachmentDownloadException("Could not convert attachment", e);
        }

        return scaledImageInByte;
    }

}
