package com.lims.core.utils.web;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * 二维码生成工具类
 * @author 菅志平
 */
public class QrCodeUtils {
    private static final Map<EncodeHintType, Object> encodeMap = new HashMap<EncodeHintType, Object>();
    private static final Map<DecodeHintType, ErrorCorrectionLevel> decodeMap=new HashMap<DecodeHintType, ErrorCorrectionLevel>();
    private static final String charset="UTF-8",format="png";
    private static final int offColor=0xFFFFFFFF;
    private QrCodeUtils() {}
    static {
        encodeMap.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);//设置容错率为最高
        encodeMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");//设置编码为utf-8
//        encodeMap.put(EncodeHintType.MARGIN,0);//设置空白区域为最小
    }
    /**
     *  生成二维码图片
     * @param data 内容
     * @param filepath  文件路径
     * @param width 宽度
     * @param height 高度
     * @param isflag 是否去白边
     * @Author Zhiping Jian
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCode(String data, String filepath,Integer width,Integer height,Boolean isflag) throws WriterException, IOException {
        BitMatrix bitMatrix=new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height, encodeMap);
        if(isflag){
            bitMatrix=deleteWhite(bitMatrix);
        }
        OutputStream os=new FileOutputStream(filepath);
        MatrixToImageWriter.writeToStream(bitMatrix,format,os);

    }

    /**
     * 删除白边
     * @param matrix
     * @return
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
         return resMatrix;
    }
    /**
     *  生成二维码图片(可定义颜色) 去白边可选
     * @param data 内容
     * @param filepath  文件路径q
     * @param width 宽度
     * @param height 高度
     * @param onColor 前景色
     * @param isflag 是否去白边
     * @Author Zhiping Jian
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCode(String data, String filepath,Integer width,Integer height,int onColor,Boolean isflag) throws WriterException, IOException {
        MatrixToImageConfig config = new MatrixToImageConfig(onColor, offColor);
        BitMatrix bitMatrix=new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height, encodeMap);
        if(isflag){
            bitMatrix=deleteWhite(bitMatrix);
        }
        OutputStream os=new FileOutputStream(filepath);
        MatrixToImageWriter.writeToStream(bitMatrix,format,os,config);
    }

    /**
     * 读取二维码
     * @param filePath
     * @Author Zhiping Jian
     * @return 二维码内容
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NotFoundException
     */
    public static String readQRCode(String filePath) throws FileNotFoundException, IOException, NotFoundException {
        return new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath))))),decodeMap).getText();
    }

}
