package com.oocl.parkingreservationservice.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

/**
 * 生成二维码的工具类
 *
 * @author XUAL7
 */
public class QRCodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);
    public static final String IMAGE_TYPE = "jpg";

    private QRCodeUtil() {
        throw new IllegalStateException("QrcodeUtil class");
    }

    public static void getImage(String content, String path, int width, int height) {
        // 二维码的图片格式
        String format = "jpg";
        Map<EncodeHintType, ? super Object> hints = new HashMap<>(16);
        //设置二维码四周白色区域的大小
        hints.put(EncodeHintType.MARGIN, 1);
        //设置二维码的容错性
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            logger.error(e.getMessage());
        }
        // 生成二维码
        Path pathFile = Paths.get(path);
        try {
            MatrixToImageWriter.writeToPath(Objects.requireNonNull(bitMatrix), format, pathFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String getBase64QrCode(String content, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, ? super Object> hints = new HashMap<>(16);
        //设置二维码四周白色区域的大小
        //设置0-4之间
        hints.put(EncodeHintType.MARGIN, 3);
        //设置二维码的容错性
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //画二维码
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            logger.error(e.getMessage());
        }

        BufferedImage image = MatrixToImageWriter.toBufferedImage(Objects.requireNonNull(bitMatrix));
        //注意此处拿到字节数据
        byte[] bytes = imageToBytes(image);
        //Base64编码
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, IMAGE_TYPE, out);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return out.toByteArray();
    }


    public static BufferedImage toBufferedImageCustom(BitMatrix matrix) {
        //二维码边框的宽度，默认二维码的宽度是100，经过多次尝试后自定义的宽度
        int left = 3;
        int right = 4;
        int top = 2;
        int bottom = 2;

        //1、首先要自定义生成边框
        //获取二维码图案的属性
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + left + right;
        int resHeight = rec[3] + top + bottom;
        // 按照自定义边框生成新的BitMatrix
        BitMatrix matrix2 = new BitMatrix(resWidth, resHeight);
        matrix2.clear();
        //循环，将二维码图案绘制到新的bitMatrix中
        for (int i = left + 1; i < resWidth - right; i++) {
            for (int j = top + 1; j < resHeight - bottom; j++) {
                if (matrix.get(i - left + rec[0], j - top + rec[1])) {
                    matrix2.set(i, j);
                }
            }
        }

        int width = matrix2.getWidth();
        int height = matrix2.getHeight();

        //2、为边框设置颜色
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < left || x > width - right || y < top || y > height - bottom) {
                    //为了与Excel边框重合，设置二维码边框的颜色为黑色
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, matrix2.get(x, y) ? BLACK : WHITE);
                }
            }
        }
        return image;
    }
}