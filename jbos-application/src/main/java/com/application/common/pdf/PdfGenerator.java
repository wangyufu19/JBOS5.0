package com.application.common.pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static  void main(String[] args) throws IOException, DocumentException {
        String userDir = System.getProperty("user.dir");
        PdfGenerator pg=new PdfGenerator();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(userDir+"/差旅报销单.pdf"));
        document.open();
        String imagePath = userDir+"/zxing/1694136583670.jpg";
        Image image1 = Image.getInstance(imagePath);
        //设置图片位置的x轴和y周
        image1.setAbsolutePosition(50f, 740f);
        //设置图片的宽度和高度
        image1.scaleAbsolute(64, 64);
        document.add(image1);
        Paragraph bizTitle=new Paragraph("永赢基金管理有限公司",ContentFont.getZhFont(20,Font.NORMAL));
        bizTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(bizTitle);
        Paragraph bizTitle1=new Paragraph("差旅报销单",ContentFont.getZhFont(20,Font.NORMAL));
        bizTitle1.setAlignment(Element.ALIGN_CENTER);
        document.add(bizTitle1);
        Paragraph bizNo=new Paragraph("流水号 100002363",ContentFont.getZhFont());
        bizNo.setAlignment(Element.ALIGN_RIGHT);
        document.add(bizNo);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(5f); // 前间距
        table.setSpacingAfter(10f); // 后间距

        table.addCell(new TableCell("出差人",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("孟详宝",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("部门",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("信息技术部",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("出差事由",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("外出调研",BaseColor.WHITE,PdfPCell.ALIGN_CENTER,0,3).newCell());
        table.addCell(new TableCell("报销费用模板",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("营销差旅",BaseColor.WHITE,PdfPCell.ALIGN_CENTER,0,3).newCell());
        table.addCell(new TableCell("城市间交通费",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("0.00",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("城市内交通费",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("0.00",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("出差餐补",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("200.00",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("住宿费",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("2000.00",PdfPCell.ALIGN_CENTER).newCell());
        table.addCell(new TableCell("报销总额",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("2200.00",BaseColor.WHITE,PdfPCell.ALIGN_CENTER,0,3).newCell());
        table.addCell(new TableCell("审批流程",BaseColor.LIGHT_GRAY).newCell());
        table.addCell(new TableCell("单元格可以设置居左、居中、居右、上下居中、设置边框、设置边框颜色、设置单元格背景颜色等, 使用PdfCell对象设置，颜色比比较简单",BaseColor.WHITE,0,3).newCell());
        document.add(table);
        document.close();
        writer.close();
    }

}
