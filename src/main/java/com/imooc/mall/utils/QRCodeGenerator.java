package com.imooc.mall.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.imooc.mall.common.Constant;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 描述：工具类，根据订单号生成二维码；
 */
public class QRCodeGenerator {
    /**
     * 生成二维码
     *
     * @param text：二维码中包含的信息
     * @param width：二维码图片的宽度
     * @param height：二维码图片的高度
     * @param filePath：二维码图片的存放位置
     * @throws WriterException
     * @throws IOException
     */
    public static void generateQRCode(String text, int width, int height, String filePath) throws WriterException, IOException {
        //首先，实例化一个QRCodeWriter对象；
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //然后，调用encode()方法：生成一个编码后的结果；其中的第二个参数是格式，这儿我们使用QR_CODE；
        //这个方法，返回的结果是一个比特矩阵，即BitMatrix；
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        //然后，根据二维码图片将要存放的路径，得到一个Path类型的地址；
        Path path = FileSystems.getDefault().getPath(filePath);
        //利用matrix的工具，把上面的比特矩阵，转成二维码图片；   其中，第二个参数“PNG”，表示二维码图片是.png格式的；
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            generateQRCode("djhfiukhk", 350, 350, "E:/imooc-mall-upload-file/797.png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}