package com.application.common.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class ContentFont {
    public static Font getZhFont() throws IOException, DocumentException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        //BaseFont bfChinese = BaseFont.createFont("D:\\eclipse-workspace\\STXIHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//黑体
        Font font = new Font(bfChinese, 12,Font.NORMAL);
        return font;
    }
    public static Font getZhFont(int size) throws IOException, DocumentException {
        //BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont("D:\\eclipse-workspace\\STXIHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//黑体
        Font font = new Font(bfChinese, size,Font.NORMAL);
        return font;
    }
    public static Font getZhFont(int size,int style) throws IOException, DocumentException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        //BaseFont bfChinese = BaseFont.createFont("D:\\eclipse-workspace\\STXIHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//黑体
        Font font = new Font(bfChinese, size,style);
        return font;
    }
}
